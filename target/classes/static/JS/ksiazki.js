const apiURL = "http://localhost:8080/books";

async function getData() {
  try {
    const response = await fetch(apiURL);
    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }
    const data = await response.json();
    console.log(data);

    fillTable(data);
  } catch (error) {
    console.error(error.message);
  }
}

function fillTable(data) {
  const tableBody = document.querySelector(".tabela");

  if (!tableBody) {
    console.log("Nie znaleziono elementu z klasą 'tabela'.");
    return;
  }

  data.forEach((item) => {
    const row = document.createElement("tr");

    const titleCell = document.createElement("td");
    titleCell.textContent = item.name;

    const idCell = document.createElement("td");
    idCell.textContent = item.id;

    row.appendChild(titleCell);
    row.appendChild(idCell);

    tableBody.append(row);
  });
}

getData();
