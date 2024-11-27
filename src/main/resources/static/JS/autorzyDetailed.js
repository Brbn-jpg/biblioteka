document.querySelector(".check").addEventListener("click", async (event) => {
  event.preventDefault();

  const number = document.querySelector(".number-input").value;
  if (!number) {
    alert("Proszę wpisać numer!");
    return;
  }

  const apiURL = `http://localhost:8080/author/${number}/details`;

  try {
    const response = await fetch(apiURL);
    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }
    const data = await response.json();
    console.log(data);

    fillTable(data);
  } catch (error) {
    alert("Podane id autora nie istnieje");
    console.error("Błąd:", error.message);
  }
});

function fillTable(data) {
  const tableBody = document.querySelector(".tabela");

  if (!tableBody) {
    console.log("Nie znaleziono elementu z klasą 'tabela'.");
    return;
  }

  tableBody.innerHTML = "";

  const authorRow = document.createElement("tr");

  const authorIdCell = document.createElement("td");
  authorIdCell.textContent = data.id || "-";

  const firstNameCell = document.createElement("td");
  firstNameCell.textContent = data.firstName || "-";

  const lastNameCell = document.createElement("td");
  lastNameCell.textContent = data.lastName || "-";

  authorRow.appendChild(authorIdCell);
  authorRow.appendChild(firstNameCell);
  authorRow.appendChild(lastNameCell);

  if (data.books && data.books.length > 0) {
    const firstBookCell = document.createElement("td");
    firstBookCell.textContent = data.books[0].name || "-";
    const firstBookIdCell = document.createElement("td");
    firstBookIdCell.textContent = data.books[0].id;
    authorRow.appendChild(firstBookCell);
    authorRow.appendChild(firstBookIdCell);
  }

  tableBody.appendChild(authorRow);

  data.books.slice(1).forEach((book, index) => {
    const bookRow = document.createElement("tr");

    const emptyCell1 = document.createElement("td");
    const emptyCell2 = document.createElement("td");
    const emptyCell3 = document.createElement("td");

    const bookCell = document.createElement("td");
    bookCell.textContent = book.name || "-";
    const idCell = document.createElement("td");
    idCell.textContent = book.id || "-";

    bookRow.appendChild(emptyCell1);
    bookRow.appendChild(emptyCell2);
    bookRow.appendChild(emptyCell3);
    bookRow.appendChild(bookCell);
    bookRow.appendChild(idCell);

    tableBody.appendChild(bookRow);
  });
}
