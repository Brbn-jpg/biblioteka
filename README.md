
# 📖 System biblioteczny

System do zarządzania biblioteką za pomocą architektury REST API, umożliwia proste zarządzanie biblioteką




## 🛠️ Funkcje

### Zarządzanie książkami
* Pełen wgląd do bazy książek
* Pełen wgląd do bazy książek oraz ich szczegółów
* Dodawanie książek do bazy danych
* Aktualizowanie danych książek
* Usuwanie książek z bazy danych

### Zarządzanie autorami
* Pełen wgląd do bazy autorów
* Pełen wgląd do bazy autorów oraz ich książek
* Dodawanie autorów do bazy danych
* Aktualizowanie danych autorów
* Usuwanie autorów z bazy danych
##  Endpointy


### Książki
| Metoda  | Endpoint | Opis |
| ------------- | ------------- |
| GET | /books  |   Pokazuje wszystkie książki |
| GET | /books/{id}  |  Pokazuje daną książkę |
| GET | /books/{id}/details | Pokazuje daną książkę oraz autora |
| POST | /books/addBook | Dodaje książkę |
| PUT | books/updateBook/{id} | Aktualizuje dane książki |
| DELETE | books/deleteBook/{id} | Usuwa daną książkę |

### Autorzy
| Metoda  | Endpoint | Opis |
| ------------- | ------------- |
| GET | /author  |   Pokazuje wszystkich autorów |
| GET | /author/{id}  |  Pokazuje danego autora |
| GET | /author/{id}/details | Pokazuje danego autora oraz jego książki |
| POST | /author/addAuthor | Dodaje autora |
| PUT | author/updateAuthor/{id} | Aktualizuje dane autora |
| DELETE | author/deleteAuthor/{id} | Usuwa danego autora |


## Użyte technologie

* Backend: Spring boot
* Baza danych: PostgreSQL
* Testy: 
    * JUnit
    * Postman

## 🗿 Wymagania
* Java 11
* PostgreSQL

## Uruchomienie testów

Aby uruchomić testy JUnit, należy wprowadzić w terminalu polecenie:
```bash
    mvn test
```


## Przykładowe dane SQL
```bash
INSERT INTO author (first_name, last_name) VALUES ('Jan', 'Kowalski');
INSERT INTO author (first_name, last_name) VALUES ('Anna', 'Nowak');
INSERT INTO author (first_name, last_name) VALUES ('Piotr', 'Zieliński');
INSERT INTO author (first_name, last_name) VALUES ('Zbigniew', 'Baka');

INSERT INTO book (name, author_id) VALUES ('Książka A', 1);
INSERT INTO book (name, author_id) VALUES ('Książka B', 2);
INSERT INTO book (name, author_id) VALUES ('Książka C', 3);
INSERT INTO book (name, author_id) VALUES ('Książka D', 1);
INSERT INTO book (name, author_id) VALUES ('Książka E', 4);
```