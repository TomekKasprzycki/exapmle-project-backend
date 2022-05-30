# ROLE

insert into role (id, name) values (1, "ROLE_USER");
insert into role (id, name) values (2, "ROLE_ADMIN");

# CATEGORY

insert into category (id, name) values (1, "fantastyka");
insert into category (id, name) values (2, "since-fiction");
insert into category (id, name) values (3, "dla dzici");
insert into category (id, name) values (4, "obyczajowe");
insert into category (id, name) values (5, "literatura faktu");
insert into category (id, name) values (6, "historyczne");

# Authors

insert into Author (id, first_name, second_name, last_name) values (1, "Andrzej", "", "Sapkowski") ;
insert into Author (id, first_name, second_name, last_name) values (2, "Adam", "", "Mickiewicz") ;
insert into Author (id, first_name, second_name, last_name) values (3, "Aleksander", "Alan", "Milne") ;
insert into Author (id, first_name, second_name, last_name) values (4, "Jane", "", "Austin") ;
insert into Author (id, first_name, second_name, last_name) values (5, "John", "RR", "Tolkien") ;
insert into Author (id, first_name, second_name, last_name) values (6, "Stanisław", "", "Lem") ;
insert into Author (id, first_name, second_name, last_name) values (7, "Olga", "", "Tokarczuk") ;
insert into Author (id, first_name, second_name, last_name) values (8, "Mery", "", "Spolsky") ;
insert into Author (id, first_name, second_name, last_name) values (9, "Zadie", "", "Smith") ;
insert into Author (id, first_name, second_name, last_name) values (10, "Agnieszka", "", "Graf") ;

# Books

insert into book (id, book_lended, title, category_id, user_id) values (1, false, "Wiedźmin - Ostatnie życzenie", 1, 2);
insert into book (id, book_lended, title, category_id, user_id) values (2, false, "Drużyna Pierścienia", 1, 2);
insert into book (id, book_lended, title, category_id, user_id) values (3, false, "Solaris", 2, 10);
insert into book (id, book_lended, title, category_id, user_id) values (4, false, "Jestem Marysia i chyba się zabiję dzisiaj", 4, 14);
insert into book (id, book_lended, title, category_id, user_id) values (5, false, "Duma i uprzedzenie", 4, 12);
insert into book (id, book_lended, title, category_id, user_id) values (6, false, "Perswazje", 4, 10);
insert into book (id, book_lended, title, category_id, user_id) values (7, false, "Kubuś Puchatek. Chatka Puchatka", 3, 2);
insert into book (id, book_lended, title, category_id, user_id) values (8, false, "Bieguni", 6, 2);
insert into book (id, book_lended, title, category_id, user_id) values (9, false, "Księgi Jakubowe", 6, 10);
insert into book (id, book_lended, title, category_id, user_id) values (10, false, "Przebłyski", 4, 2);
insert into book (id, book_lended, title, category_id, user_id) values (11, false, "O pięknie", 4, 12);
insert into book (id, book_lended, title, category_id, user_id) values (12, false, "Jestem stąd", 5, 2);
insert into book (id, book_lended, title, category_id, user_id) values (13, false, "Świat bez kobiet", 5, 14);
insert into book (id, book_lended, title, category_id, user_id) values (14, false, "Silmarilion", 1, 12);
insert into book (id, book_lended, title, category_id, user_id) values (15, false, "Dzienniki gwiazdowe", 2, 2);
insert into book (id, book_lended, title, category_id, user_id) values (16, false, "Nowe przygody Kubusia Puchatka", 3, 14);
insert into book (id, book_lended, title, category_id, user_id) values (17, false, "Pan Tadeusz", 6, 14);
insert into book (id, book_lended, title, category_id, user_id) values (18, false, "Dziady, część II", 6, 12);
insert into book (id, book_lended, title, category_id, user_id) values (19, false, "Wiedźmin - Krew Elfów", 1, 10);
insert into book (id, book_lended, title, category_id, user_id) values (20, false, "Wiedźmin - Chrzest ognia", 1, 2);

# Books Authors

insert into book_authors (books_id, authors_id) values (1, 1);
insert into book_authors (books_id, authors_id) values (2, 5);
insert into book_authors (books_id, authors_id) values (3, 6);
insert into book_authors (books_id, authors_id) values (4, 8);
insert into book_authors (books_id, authors_id) values (5, 4);
insert into book_authors (books_id, authors_id) values (6, 4);
insert into book_authors (books_id, authors_id) values (7, 3);
insert into book_authors (books_id, authors_id) values (8, 7);
insert into book_authors (books_id, authors_id) values (9, 7);
insert into book_authors (books_id, authors_id) values (10, 9);
insert into book_authors (books_id, authors_id) values (11, 9);
insert into book_authors (books_id, authors_id) values (12, 10);
insert into book_authors (books_id, authors_id) values (13, 10);
insert into book_authors (books_id, authors_id) values (14, 5);
insert into book_authors (books_id, authors_id) values (15, 6);
insert into book_authors (books_id, authors_id) values (16, 3);
insert into book_authors (books_id, authors_id) values (17, 2);
insert into book_authors (books_id, authors_id) values (18, 2);
insert into book_authors (books_id, authors_id) values (19, 1);
insert into book_authors (books_id, authors_id) values (20, 1);

