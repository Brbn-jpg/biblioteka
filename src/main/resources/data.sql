-- Insert Authors without specifying id (it will be generated automatically)
INSERT INTO author (author_id, first_name, last_name) VALUES (1,'Jan', 'Kowalski');
INSERT INTO author (author_id, first_name, last_name) VALUES (2, 'Anna', 'Nowak');
INSERT INTO author (author_id, first_name, last_name) VALUES (3, 'Piotr', 'Zieliński');


-- Insert Books
INSERT INTO book (book_id, name, author_id) VALUES (1,'Książka A', 1);
INSERT INTO book (book_id, name, author_id) VALUES (2,'Książka B', 2);
INSERT INTO book (book_id, name, author_id) VALUES (3,'Książka C', 3);
INSERT INTO book (book_id, name, author_id) VALUES (4,'Książka D', 1);