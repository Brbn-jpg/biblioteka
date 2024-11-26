const apiURL = "http://localhost:8080/author";

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

    const firstNameCell = document.createElement("td");
    firstNameCell.textContent = item.firstName;

    const lastNameCell = document.createElement("td");
    lastNameCell.textContent = item.lastName;

    const idCell = document.createElement("td");
    idCell.textContent = item.author_id;

    row.appendChild(firstNameCell);
    row.appendChild(lastNameCell);
    row.appendChild(idCell);

    tableBody.append(row);
  });
}

getData();
