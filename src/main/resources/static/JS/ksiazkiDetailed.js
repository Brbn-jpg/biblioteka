document.querySelector(".check").addEventListener("click", async (event) => {
  event.preventDefault();

  const number = document.querySelector(".number-input").value;
  if (!number) {
    alert("Proszę wpisać numer!");
    return;
  }

  const apiURL = `http://localhost:8080/books/${number}/details`;

  try {
    const response = await fetch(apiURL);
    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }
    const data = await response.json();
    console.log(data);

    fillTable(data);
  } catch (error) {
    alert("Podane id ksiązki nie istnieje");
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

  const row = document.createElement("tr");

  const bookIdCell = document.createElement("td");
  bookIdCell.textContent = data.id || "-";

  const titleCell = document.createElement("td");
  titleCell.textContent = data.name || "-";

  const firstNameCell = document.createElement("td");
  firstNameCell.textContent = data.author.first_name || "-";

  const lastNameCell = document.createElement("td");
  lastNameCell.textContent = data.author.last_name || "-";

  const authorIdCell = document.createElement("td");
  authorIdCell.textContent = data.author.author_id || "-";

  row.appendChild(bookIdCell);
  row.appendChild(titleCell);
  row.appendChild(firstNameCell);
  row.appendChild(lastNameCell);
  row.appendChild(authorIdCell);

  tableBody.appendChild(row);
}
