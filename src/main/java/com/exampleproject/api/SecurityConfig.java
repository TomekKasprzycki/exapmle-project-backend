package com.exampleproject.api;

import com.exampleproject.api.filters.JwtAuthenticationFilter;
import com.exampleproject.api.filters.JwtAuthorizationFilter;
import com.exampleproject.api.services.MyUserDetailService;
import com.exampleproject.api.services.TokenService;
import com.exampleproject.api.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailService myUserDetailService;
    private final UserService userService;
    private final TokenService tokenService;

    public SecurityConfig(UserService userService,
                          MyUserDetailService myUserDetailService,
                          TokenService tokenService){
        this.myUserDetailService=myUserDetailService;
        this.tokenService=tokenService;
        this.userService=userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests(authorizeRequests -> {
                    authorizeRequests
                            .antMatchers(HttpMethod.GET, "/api/*/anonymous/**").permitAll()
                            .anyRequest().authenticated();
                })
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), tokenService, userService))
                .addFilterAfter(new JwtAuthorizationFilter(authenticationManager(), tokenService), JwtAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
