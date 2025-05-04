import http from "k6/http";
import { check, sleep, group } from "k6";
import { randomString } from "https://jslib.k6.io/k6-utils/1.2.0/index.js";

export let options = {
  scenarios: {
    ten_users: {
      executor: "constant-vus",
      vus: 10,
      duration: "30s",
      tags: { test_type: "10_users" },
    },
    fifty_users: {
      executor: "constant-vus",
      vus: 50,
      duration: "30s",
      startTime: "35s",
      tags: { test_type: "50_users" },
    },
    hundred_users: {
      executor: "constant-vus",
      vus: 100,
      duration: "30s",
      startTime: "70s",
      tags: { test_type: "100_users" },
    },
  },
  thresholds: {
    http_req_duration: ["p(95)<500"], // 95% of requests should be below 500ms
    "http_req_duration{endpoint:get_all_authors}": ["p(95)<400"],
    "http_req_duration{endpoint:get_all_books}": ["p(95)<400"],
    "http_req_duration{endpoint:author_details}": ["p(95)<300"],
    "http_req_duration{endpoint:book_details}": ["p(95)<300"],
    "http_req_duration{endpoint:add_author}": ["p(95)<600"],
    "http_req_duration{endpoint:add_book}": ["p(95)<600"],
    "http_req_duration{endpoint:update_author}": ["p(95)<600"],
    "http_req_duration{endpoint:update_book}": ["p(95)<600"],
    "http_req_duration{endpoint:delete_author}": ["p(95)<500"],
    "http_req_duration{endpoint:delete_book}": ["p(95)<500"],
  },
};

const baseUrl = "http://localhost:8080";

let createdAuthors = [];
let createdBooks = [];

export default function () {
  let authorId;
  let bookId;

  group("Author API Tests", function () {
    group("GET all authors", function () {
      let authorsResponse = http.get(`${baseUrl}/author`, {
        tags: { endpoint: "get_all_authors" },
      });

      check(authorsResponse, {
        "status is 200": (r) => r.status === 200,
        "response is an array": (r) => Array.isArray(r.json()),
      });

      if (authorsResponse.json().length > 0) {
        authorId = authorsResponse.json()[0].author_id;
      }
    });

    group("GET author details", function () {
      if (authorId) {
        let authorDetailsResponse = http.get(
          `${baseUrl}/author/${authorId}/details`,
          {
            tags: { endpoint: "author_details" },
          }
        );

        check(authorDetailsResponse, {
          "status is 200": (r) => r.status === 200,
          "author details returned": (r) => r.json().firstName !== undefined,
        });
      }
    });

    group("POST add author", function () {
      let randomFirstName = `Test${randomString(5)}`;
      let randomLastName = `Author${randomString(5)}`;

      let authorPayload = JSON.stringify({
        firstName: randomFirstName,
        lastName: randomLastName,
      });

      let addAuthorResponse = http.post(
        `${baseUrl}/author/addAuthor`,
        authorPayload,
        {
          headers: { "Content-Type": "application/json" },
          tags: { endpoint: "add_author" },
        }
      );

      check(addAuthorResponse, {
        "add author status is 200": (r) => r.status === 200,
        "author was added": (r) => r.json().author_id !== undefined,
      });

      if (addAuthorResponse.status === 200) {
        authorId = addAuthorResponse.json().author_id;
        createdAuthors.push(authorId);
      }
    });

    if (authorId) {
      group("PUT update author", function () {
        let updateAuthorPayload = JSON.stringify({
          firstName: `Updated${randomString(5)}`,
          lastName: `Author${randomString(5)}`,
        });

        let updateAuthorResponse = http.put(
          `${baseUrl}/author/updateAuthor/${authorId}`,
          updateAuthorPayload,
          {
            headers: { "Content-Type": "application/json" },
            tags: { endpoint: "update_author" },
          }
        );

        check(updateAuthorResponse, {
          "update author status is 200": (r) => r.status === 200,
          "author was updated": (r) =>
            r.json().firstName.startsWith("Updated") &&
            r.json().author_id === authorId,
        });
      });
    }
  });

  group("Book API Tests", function () {
    group("GET all books", function () {
      let booksResponse = http.get(`${baseUrl}/books`, {
        tags: { endpoint: "get_all_books" },
      });

      check(booksResponse, {
        "status is 200": (r) => r.status === 200,
        "response is an array": (r) => Array.isArray(r.json()),
      });

      if (booksResponse.json().length > 0) {
        bookId = booksResponse.json()[0].id;
      }
    });

    group("GET book details", function () {
      if (bookId) {
        let bookDetailsResponse = http.get(
          `${baseUrl}/books/${bookId}/details`,
          {
            tags: { endpoint: "book_details" },
          }
        );

        check(bookDetailsResponse, {
          "status is 200": (r) => r.status === 200,
          "book details returned": (r) => r.json().name !== undefined,
        });
      }
    });

    group("POST add book", function () {
      if (authorId) {
        let bookPayload = JSON.stringify({
          name: `Book${randomString(8)}`,
          author_id: authorId.toString(),
        });

        let addBookResponse = http.post(
          `${baseUrl}/books/addBook`,
          bookPayload,
          {
            headers: { "Content-Type": "application/json" },
            tags: { endpoint: "add_book" },
          }
        );

        check(addBookResponse, {
          "add book status is 200": (r) => r.status === 200,
          "book was added": (r) => r.json().id !== undefined,
        });

        if (addBookResponse.status === 200) {
          bookId = addBookResponse.json().id;
          createdBooks.push(bookId);
        }
      }
    });

    if (bookId && authorId) {
      group("PUT update book", function () {
        let updateBookPayload = JSON.stringify({
          name: `UpdatedBook${randomString(5)}`,
          author: {
            author_id: authorId,
          },
        });

        let updateBookResponse = http.put(
          `${baseUrl}/books/updateBook/${bookId}`,
          updateBookPayload,
          {
            headers: { "Content-Type": "application/json" },
            tags: { endpoint: "update_book" },
          }
        );

        check(updateBookResponse, {
          "update book status is 200": (r) => r.status === 200,
          "book was updated": (r) =>
            r.json().name.startsWith("UpdatedBook") && r.json().id === bookId,
        });
      });
    }
  });

  // Cleanup
  if (Math.random() < 0.3 && bookId) {
    // 30% chance to delete a book
    group("DELETE book", function () {
      let deleteBookResponse = http.del(
        `${baseUrl}/books/deleteBook/${bookId}`,
        null,
        {
          tags: { endpoint: "delete_book" },
        }
      );

      check(deleteBookResponse, {
        "delete book status is 204": (r) => r.status === 204,
      });

      if (deleteBookResponse.status === 204) {
        const index = createdBooks.indexOf(bookId);
        if (index > -1) {
          createdBooks.splice(index, 1);
        }
      }
    });
  }

  // Less frequently delete authors (should delete books first)
  if (Math.random() < 0.15 && authorId) {
    // 15% chance to delete an author
    group("DELETE author", function () {
      let deleteAuthorResponse = http.del(
        `${baseUrl}/author/deleteAuthor/${authorId}`,
        null,
        {
          tags: { endpoint: "delete_author" },
        }
      );

      check(deleteAuthorResponse, {
        "delete author status is 204": (r) => r.status === 204,
      });

      // Remove from tracking array if deleted
      if (deleteAuthorResponse.status === 204) {
        const index = createdAuthors.indexOf(authorId);
        if (index > -1) {
          createdAuthors.splice(index, 1);
        }
      }
    });
  }

  // Add a small sleep to prevent server overload
  sleep(1);
}
