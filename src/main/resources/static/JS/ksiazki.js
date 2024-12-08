// const apiURL = "http://localhost:8080/books";

// async function getData() {
//   try {
//     const response = await fetch(apiURL);
//     if (!response.ok) {
//       throw new Error(`Response status: ${response.status}`);
//     }
//     const data = await response.json();
//     console.log(data);

//     fillTable(data);
//   } catch (error) {
//     console.error(error.message);
//   }
// }

// function fillTable(data) {
//   const tableBody = document.querySelector(".tabela");

//   if (!tableBody) {
//     console.log("Nie znaleziono elementu z klasą 'tabela'.");
//     return;
//   }

//   data.forEach((item) => {
//     const row = document.createElement("tr");

//     const titleCell = document.createElement("td");
//     titleCell.textContent = item.name;

//     const idCell = document.createElement("td");
//     idCell.textContent = item.id;

//     row.appendChild(titleCell);
//     row.appendChild(idCell);

//     tableBody.append(row);
//   });
// }

// getData();

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
    console.error("Błąd podczas pobierania danych:", error.message);
  }
}

function fillTable(data) {
  const tableBody = document.querySelector(".tabela");

  if (!tableBody) {
    console.error("Nie znaleziono elementu z klasą 'tabela'.");
    return;
  }

  tableBody.innerHTML = "";

  data.forEach((item) => {
    const row = document.createElement("tr");

    const titleCell = document.createElement("td");
    titleCell.classList.add("editable");
    titleCell.dataset.dataField = "name";
    titleCell.dataset.dataId = item.id;
    titleCell.textContent = item.name;

    const idCell = document.createElement("td");
    idCell.textContent = item.id;

    row.appendChild(titleCell);
    row.appendChild(idCell);

    tableBody.appendChild(row);
  });

  enableEditing();
}

function enableEditing() {
  document.querySelectorAll(".editable").forEach((cell) => {
    cell.addEventListener("click", function () {
      const originalValue = this.textContent;
      const field = this.dataset.dataField;
      const bookId = this.dataset.dataId;

      const input = document.createElement("input");
      input.type = "text";
      input.value = originalValue;

      input.addEventListener("blur", async () => {
        const newValue = input.value.trim();

        if (newValue === originalValue) {
          this.textContent = originalValue;
          return;
        }

        try {
          const response = await fetch(`${apiURL}/updateBook/${bookId}`, {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ [field]: newValue }),
          });

          // ============debug============ //
          console.log(
            "Dane wysyłane do API:",
            JSON.stringify({ [field]: newValue })
          );
          console.log("Endpoint:", `${apiURL}/updateBook/${bookId}`);
          // ============================= //

          if (!response.ok) {
            throw new Error("Błąd podczas aktualizacji danych.");
          }

          this.textContent = newValue;
        } catch (error) {
          console.error("Błąd podczas aktualizacji:", error.message);
          alert("Nie udało się zaktualizować danych.");
          this.textContent = originalValue;
        }
      });

      this.textContent = "";
      this.appendChild(input);
      input.focus();
    });
  });
}

getData();
