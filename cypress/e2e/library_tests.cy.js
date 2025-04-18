describe("Library Management System", () => {
  beforeEach(() => {
    cy.visit("http://localhost:8080/landingPage.html");
    cy.intercept("GET", "/api/books", { fixture: "books.json" }).as("getBooks");
    cy.intercept("GET", "/api/authors", { fixture: "authors.json" }).as(
      "getAuthors"
    );
    cy.intercept("POST", "/api/books", {}).as("addBook");
    cy.intercept("POST", "/api/authors", {}).as("addAuthor");
    cy.intercept("PUT", "/api/books/*", {}).as("updateBook");
    cy.intercept("PUT", "/api/authors/*", {}).as("updateAuthor");
    cy.intercept("DELETE", "/api/books/*", {}).as("deleteBook");
    cy.intercept("DELETE", "/api/authors/*", {}).as("deleteAuthor");
  });

  it("should load the landing page", () => {
    cy.visit("/landingPage.html");
    cy.get("h1").should("contain", "Strona startowa do Spring Boot REST Api");
    cy.get("h2").should("contain", "System biblioteczny");
    cy.get(".funkcje").should("be.visible");
  });

  it("should navigate between pages", () => {
    cy.visit("/landingPage.html");

    // Navigate to books page
    cy.get('a[href="ksiazki.html"]').click();
    cy.url().should("include", "/ksiazki.html");
    cy.get("h1").should("contain", "Wszystkie Książki");

    // Navigate to authors page
    cy.get('a[href="autorzy.html"]').click();
    cy.url().should("include", "/autorzy.html");
    cy.get("h1").should("contain", "Wszyscy autorzy");

    // Navigate back to landing page
    cy.get(".start-page").click();
    cy.url().should("include", "/landingPage.html");
  });

  describe("Book Management", () => {
    it("should load books page with table", () => {
      cy.visit("/ksiazki.html");
      cy.get(".output-table").should("be.visible");
      cy.get("thead").should("contain", "Tytuł");
      cy.get("thead").should("contain", "Id ksiązki");
    });

    it("should load book details page", () => {
      cy.visit("/ksiazkiDetailed.html");
      cy.get("select.book-id").should("be.visible");
      cy.get("button.check").should("be.visible");
      cy.get(".output-table").should("be.visible");
    });

    it("should validate add book form", () => {
      cy.visit("/dodajKsiazke.html");

      // Submit without required fields
      cy.get("button.Add").click();
      cy.url().should("include", "/dodajKsiazke.html");

      // Fill required fields and submit
      cy.get("input.title").type("Test Book Title");
      cy.get("select.author-id").select(1);
      cy.get("button.Add").click();
      cy.get(".added")
        .should("not.have.class", "invis")
        .and("contain", "Dodano ksiązkę");
    });

    it("should test book update functionality", () => {
      cy.visit("/zaktualizujKsiazke.html");

      // Select book and update fields
      cy.get("select.book-id").select(1);
      cy.get("input.title").type("Updated Book Title");
      cy.get("input.author-id").type("1");

      // Submit and check update message
      cy.get('button[type="submit"]').click();
      cy.get(".updated").should("not.have.class", "invis");
    });

    it("should test book deletion", () => {
      cy.visit("/usunKsiazke.html");

      // Select book and delete
      cy.get("select.book-id").select(1);
      cy.get('button[type="submit"]').click();
      cy.get(".deleted").should("not.have.class", "invis");
    });
  });

  describe("Author Management", () => {
    it("should load authors page with table", () => {
      cy.visit("/autorzy.html");
      cy.get(".output-table").should("be.visible");
      cy.get("thead").should("contain", "Imie");
      cy.get("thead").should("contain", "Nazwisko");
      cy.get("thead").should("contain", "Id autora");
    });

    it("should load author details page", () => {
      cy.visit("/autorzyDetailed.html");
      cy.get("select.author-id").should("be.visible");
      cy.get("button.check").should("be.visible");
      cy.get(".output-table").should("be.visible");
    });

    it("should validate add author form", () => {
      cy.visit("/dodajAutora.html");

      // Submit without required fields
      cy.get("button.Add").click();
      cy.url().should("include", "/dodajAutora.html");

      // Fill required fields and submit
      cy.get("input.firstName").type("Test");
      cy.get("input.lastName").type("Author");
      cy.get("button.Add").click();
      cy.get(".added")
        .should("not.have.class", "invis")
        .and("contain", "Dodano autora");
    });

    it("should test author update functionality", () => {
      cy.visit("/zaktualizujAutora.html");

      // Select author and update fields
      cy.get("select.author-id").select(1);
      cy.get("input.firstName").type("Updated");
      cy.get("input.lastName").type("AuthorName");

      // Submit and check update message
      cy.get('button[type="submit"]').click();
      cy.get(".updated").should("not.have.class", "invis");
    });

    it("should test author deletion", () => {
      cy.visit("/usunAutora.html");

      // Select author and delete
      cy.get("select.author-id").select(1);
      cy.get('button[type="submit"]').click();
      cy.get(".deleted").should("not.have.class", "invis");
    });
  });

  describe("UI Elements Visibility", () => {
    it("should ensure all navigation buttons are visible", () => {
      cy.visit("/landingPage.html");
      cy.get("nav table").should("be.visible");
      cy.get("nav a").should("have.length", 11);
      cy.get("nav a").each(($a) => {
        cy.wrap($a).should("be.visible");
      });
    });

    it("should verify form elements are accessible", () => {
      // Check add book form
      cy.visit("/dodajKsiazke.html");
      cy.get("input.title").should("be.visible").and("be.enabled");
      cy.get("select.author-id").should("be.visible").and("be.enabled");
      cy.get("button.Add").should("be.visible").and("be.enabled");

      // Check add author form
      cy.visit("/dodajAutora.html");
      cy.get("input.firstName").should("be.visible").and("be.enabled");
      cy.get("input.lastName").should("be.visible").and("be.enabled");
      cy.get("button.Add").should("be.visible").and("be.enabled");
    });

    it("should check for visual bugs in tables", () => {
      cy.visit("/ksiazki.html");
      cy.get(".output-table").should("be.visible");
      cy.get("thead td").should("have.length", 3);

      cy.visit("/autorzy.html");
      cy.get(".output-table").should("be.visible");
      cy.get("thead td").should("have.length", 4);
    });
  });
});
