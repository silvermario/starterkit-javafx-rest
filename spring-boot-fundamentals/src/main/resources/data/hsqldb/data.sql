INSERT INTO Greeting (text) VALUES ('Hello World!');
INSERT INTO Greeting (text) VALUES ('Hola Mundo!');

--insert into library (id, name) values (1, 'Biblioteka Wroclawska');
--insert into library (id, name) values (2, 'Narodowa Biblioteka');
--insert into library (id, name) values (3, 'Biblioteka Ossolineum');
--
--insert into book (id, title, library_id) values (1, 'Pierwsza książka', 1);
--insert into book (id, title, library_id) values (2, 'Druga książka', 2);
--insert into book (id, title, library_id) values (3, 'Trzecia książka', 1);

insert into book (id, title) values (1, 'Pierwsza ksiazka');
insert into book (id, title) values (2, 'Druga ksiazka');
insert into book (id, title) values (3, 'Trzecia ksiazka');

insert into author (id, firstName, lastName) values (7, 'Jan', 'Kowalski');
insert into author (id, firstName, lastName) values (8, 'Zbigniew', 'Nowak');
insert into author (id, firstName, lastName) values (9, 'Janusz', 'Jankowski');

insert into book_author(book_id, author_id) values (1, 7);
insert into book_author(book_id, author_id) values (1, 8);
insert into book_author(book_id, author_id) values (2, 8);
insert into book_author(book_id, author_id) values (3, 9);
