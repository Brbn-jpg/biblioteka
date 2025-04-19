import http from "k6/http";
import { check, sleep } from "k6";

// Test configuration
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
  },
};

// Base URL
const baseUrl = "http://localhost:8080";

export default function () {
  // Test fetching all authors
  let authorsResponse = http.get(`${baseUrl}/author`);
  check(authorsResponse, {
    "status is 200": (r) => r.status === 200,
    "author list returned": (r) => r.json().length > 0,
  });

  // Test fetching all books
  let booksResponse = http.get(`${baseUrl}/books`);
  check(booksResponse, {
    "status is 200": (r) => r.status === 200,
    "books list returned": (r) => r.json().length > 0,
  });

  // If there are authors in the system, fetch details
  if (authorsResponse.json().length > 0) {
    let authorId = authorsResponse.json()[0].author_id;
    let authorDetailsResponse = http.get(
      `${baseUrl}/author/${authorId}/details`
    );
    check(authorDetailsResponse, {
      "author details status is 200": (r) => r.status === 200,
      "author details returned": (r) => r.json().firstName !== undefined,
    });
  }

  // If there are books in the system, fetch details
  if (booksResponse.json().length > 0) {
    let bookId = booksResponse.json()[0].id;
    let bookDetailsResponse = http.get(`${baseUrl}/books/${bookId}/details`);
    check(bookDetailsResponse, {
      "book details status is 200": (r) => r.status === 200,
      "book details returned": (r) => r.json().name !== undefined,
    });
  }

  // Add a new author
  let authorPayload = JSON.stringify({
    firstName: `Test${Math.floor(Math.random() * 1000)}`,
    lastName: `Author${Math.floor(Math.random() * 1000)}`,
  });

  let addAuthorResponse = http.post(
    `${baseUrl}/author/addAuthor`,
    authorPayload,
    {
      headers: { "Content-Type": "application/json" },
    }
  );

  check(addAuthorResponse, {
    "add author status is 200": (r) => r.status === 200,
    "author was added": (r) => r.json().author_id !== undefined,
  });

  sleep(1);
}
