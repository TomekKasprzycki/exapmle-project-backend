package com.exampleproject.api.services;

import com.exampleproject.api.converters.UserDtoConverter;
import com.exampleproject.api.dto.UserDto;
import com.exampleproject.api.model.Book;
import com.exampleproject.api.model.User;
import com.exampleproject.api.model.VerificationToken;
import com.exampleproject.api.repositories.UserRepository;
import com.exampleproject.api.validators.UserValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserDtoConverter userDtoConverter;
    private final VerificationTokenService verificationTokenService;
    private final RoleService roleService;

    public UserService(UserRepository userRepository,
                       UserValidator userValidator,
                       UserDtoConverter userDtoConverter,
                       VerificationTokenService verificationTokenService,
                       MailingService mailingService,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.userDtoConverter = userDtoConverter;
        this.verificationTokenService = verificationTokenService;
        this.mailingService = mailingService;
        this.roleService = roleService;
    }

    private final MailingService mailingService;

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean registerUser(UserDto userDto) {

        long millis = System.currentTimeMillis();
        if (userValidator.isTheUserValid(userDto)) {
            User user = userDtoConverter.convertFromDto(userDto);
            user.setPassword(userDto.getPassword());
            user.setCreated(new Date(millis));
            user.setRole(roleService.getRole(1L).get());

            VerificationToken verificationToken = verificationTokenService.createToken();

            user.setVerificationToken(verificationToken);

            try {
                verificationToken = verificationTokenService.save(verificationToken);
                user = userRepository.save(user);
                verificationToken.setUser(user);
                verificationTokenService.save(verificationToken);

                String emailBody = "<p>Zakończ rejestrację klikając w poniższy link: <p><br/>" +
                        "<a href='http://localhost:8080/api/authentication/anonymous/confirm?token="
                        + verificationToken.getToken() + "' >WERYFIKACJA KONTA</a>";
                mailingService.sendMail(user.getEmail(), "Weryfikacja użytkownika", emailBody, true);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }

        } else {
            return false;
        }
    }

    public Optional<List<User>> getAllLimited(int limit, int offset) {
        return userRepository.findAllWithLimitAndOffset(limit, offset);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deactivate(User user) {
        userRepository.setUserNotActive(user);
    }

    public void activate(User user) {
        userRepository.setUserActive(user);
    }

    public boolean isNameTaken(String name) {
        return userRepository.isUserNameTaken(name) != 0;
    }

    public boolean sendVerificationToken(String email) {

        VerificationToken verificationToken;

        Optional<User> optionalUser = findUserByEmail(email);
        User user;
        if (optionalUser.isEmpty()) {
            return false;
        }
        user = optionalUser.get();

        Optional<VerificationToken> optionalVerificationToken = verificationTokenService.getByUser(user);

        if (optionalVerificationToken.isPresent()) {
            verificationToken = optionalVerificationToken.get();

            verificationToken.setActive(true);
            verificationToken.setExpirationDate();
            verificationToken.setToken();
        } else {
            verificationToken = verificationTokenService.createToken();
            verificationToken.setUser(user);
        }

        verificationToken = verificationTokenService.save(verificationToken);

        user.setVerificationToken(verificationToken);

        String emailBody = "<p>Otrzymaliśmy prośbę o zresetowanie hasła do Twojego konta. Aby zresetować hasło kliknij na poniższy link:</p><br/><br/>" +
                "<a href='http://localhost:8080/api/authentication/anonymous/confirmPasswordRecovery?token="
                + verificationToken.getToken() + "?email=" + email + "' >ZRESETUJ MOJE HASŁO</a><br/><br/>" +
                "Jeśli prośba nie była wysłana przez Ciebie poinformuje nas o tym wysyłając mail na adres: admin@hipotrofia.info";
        try {
            mailingService.sendMail(email, "Odzyskiwanie hasła", emailBody, true);
        } catch (MessagingException ex) {
            return false;
        }
        return true;

    }


    public boolean recoverPassword(String token, String email) {

        Optional<User> optional = findUserByEmail(email);

        optional.ifPresent(user -> {
            VerificationToken verificationToken = verificationTokenService.getByUser(user).orElseThrow();

            if (token.equals(verificationToken.getToken())) {
                String newPassword = createPassword();
                String emailBody = "<p>Twoje nowe hasło, to: " + newPassword + "</p><br/><br/>";
                try {
                    mailingService.sendMail(email, "Odzyskiwanie hasła", emailBody, true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

        });


        return true;
    }

    protected String createPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 38, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public boolean changePassword(UserDto userDto) {

        if (userValidator.isTheUserValid(userDto)) {
            Optional<User> optionalUser = findUserByEmail(userDto.getLogin());
            if (optionalUser.isEmpty()) {
                return false;
            }
            User user = optionalUser.get();
            user.setPassword(userDto.getPassword());
            save(user);
        }
        return true;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public boolean verifyToken(String token) {

        long millis = System.currentTimeMillis();
        Date now = new Date(millis);
        boolean result = false;

        Optional<VerificationToken> optionalVerificationToken = verificationTokenService.getByToken(token);

        if(optionalVerificationToken.isEmpty()) { return false; }

            VerificationToken verificationToken = optionalVerificationToken.get();
            final User user = verificationToken.getUser();
            if (now.before(user.getVerificationToken().getExpirationDate()) && verificationToken.isActive()) {
                user.setActive(true);
                save(user);
                verificationToken.setActive(false);
                verificationTokenService.save(verificationToken);
                result = true;
            }

        return result;
    }

}
