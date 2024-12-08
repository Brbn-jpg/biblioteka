const deleted = document.querySelector(".deleted");

async function populateAuthorsDetailedSelect() {
  const selectElement = document.querySelector(".author-id");
  try {
    const response = await fetch("http://localhost:8080/author");

    if (!response.ok) {
      throw new Error(`Błąd: ${response.status}`);
    }

    const authors = await response.json();

    authors.forEach((author) => {
      const option = document.createElement("option");
      option.value = author.author_id;
      option.textContent = `${author.firstName} ${author.lastName}`;
      selectElement.appendChild(option);
    });
  } catch (error) {
    console.error("Błąd podczas wczytywania ksiązek:", error.message);
    alert("Nie udało się załadować listy ksiązek.");
  }
}

document.addEventListener("DOMContentLoaded", populateAuthorsDetailedSelect);

document.querySelector("form").addEventListener("submit", async (event) => {
  event.preventDefault();

  const selectedAuthorId = document.querySelector(".author-id").value;

  if (!selectedAuthorId) {
    alert("Proszę wybrać autora z listy.");
    return;
  }

  const apiURL = `http://localhost:8080/author/deleteAuthor/${selectedAuthorId}`;

  try {
    const response = await fetch(apiURL, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });

    console.log("Status odpowiedzi:", response.status);

    if (response.status !== 204 && !response.ok) {
      throw new Error(`Błąd: ${response.status}`);
    }

    deleted.textContent = `Usunięto autora o ID: ${selectedAuthorId}`;
    deleted.classList.remove("invis");

    console.log("Autor usunięty pomyślnie.");
  } catch (error) {
    console.error("Błąd podczas usuwania autora:", error.message);
    alert("Wystąpił problem z usuwaniem autora.");
  }
});
