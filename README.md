
# <div align="center">📖 Library System</div>

A library management system using REST API architecture, allowing easy management of a library.

## 🗿 Requirements

- Java 11  
- PostgreSQL

## 🛠️ Features

### Book Management

- Full access to the book database  
- Full access to the book database with detailed information  
- Adding books to the database  
- Updating book information  
- Deleting books from the database  

### Author Management

- Full access to the author database  
- Full access to the author database with their books  
- Adding authors to the database  
- Updating author information  
- Deleting authors from the database  

## 📡 Endpoints

### 📚 Books

| Method | Endpoint                | Description                           |
|--------|-------------------------|---------------------------------------|
| GET    | `/books`                | Shows all books                       |
| GET    | `/books/{id}`           | Shows a specific book                 |
| GET    | `/books/{id}/details`   | Shows a specific book with its author |
| POST   | `/books/addBook`        | Adds a book                           |
| PUT    | `/books/updateBook/{id}`| Updates book information              |
| DELETE | `/books/deleteBook/{id}`| Deletes a specific book               |

### 🖋️ Authors

| Method | Endpoint                    | Description                                  |
|--------|-----------------------------|----------------------------------------------|
| GET    | `/author`                   | Shows all authors                            |
| GET    | `/author/{id}`              | Shows a specific author                      |
| GET    | `/author/{id}/details`      | Shows a specific author with their books     |
| POST   | `/author/addAuthor`         | Adds an author                               |
| PUT    | `/author/updateAuthor/{id}` | Updates author information                   |
| DELETE | `/author/deleteAuthor/{id}` | Deletes a specific author                    |

## 🧪 Technologies Used

- **Backend:** Spring Boot  
- **Database:** PostgreSQL  
- **Testing:**
  - JUnit  
  - Postman  
  - Cypress  

## ✅ Running Tests

### JUnit Tests

To run JUnit tests, enter the following command in the terminal:

```bash
mvn test
```

### Postman Tests

To run Postman tests, visit the workspace via this [link](https://www.postman.com/kk0000-9147/biblioteka-workspace/collection/9ruf0ph/biblioteka?action=share&creator=39909708)

### Cypress Tests

To run Cypress tests, enter in the terminal:

```bash
npx cypress open
```

Or in headless mode:

```bash
npx cypress run
```

## 🗃️ Example SQL Data

```sql
INSERT INTO author (first_name, last_name) VALUES ('Jan', 'Kowalski');
INSERT INTO author (first_name, last_name) VALUES ('Anna', 'Nowak');
INSERT INTO author (first_name, last_name) VALUES ('Piotr', 'Zieliński');
INSERT INTO author (first_name, last_name) VALUES ('Zbigniew', 'Baka');

INSERT INTO book (name, author_id) VALUES ('Book A', 1);
INSERT INTO book (name, author_id) VALUES ('Book B', 2);
INSERT INTO book (name, author_id) VALUES ('Book C', 3);
INSERT INTO book (name, author_id) VALUES ('Book D', 1);
INSERT INTO book (name, author_id) VALUES ('Book E', 4);
```
