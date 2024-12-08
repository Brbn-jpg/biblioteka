const updated = document.querySelector(".updated");
let authorsData = [];

async function populateAuthorSelect() {
  const selectElement = document.querySelector(".author-id");
  try {
    const response = await fetch("http://localhost:8080/author");
    console.log(response); // debug

    if (!response.ok) {
      throw new Error(`Błąd: ${response.status}`);
    }

    const authors = await response.json();
    authorsData = authors;

    authors.forEach((author) => {
      const option = document.createElement("option");
      option.value = author.author_id;
      option.textContent = `${author.firstName} ${author.lastName}`;
      selectElement.appendChild(option);
    });
  } catch (error) {
    console.error("Błąd podczas wczytywania autorów:", error.message);
    alert("Nie udało się załadować listy autorów.");
  }
}

function SelectChange(event) {
  const selectedAuthorId = event.target.value;
  const selectedAuthor = authorsData.find(
    (author) => author.author_id == selectedAuthorId
  );

  if (selectedAuthor) {
    document.querySelector(".firstName").value = selectedAuthor.firstName;
    document.querySelector(".lastName").value = selectedAuthor.lastName;
  }
}

document.addEventListener("DOMContentLoaded", populateAuthorSelect);
document.querySelector(".author-id").addEventListener("change", SelectChange);

document.querySelector("form").addEventListener("submit", async (event) => {
  event.preventDefault();

  const firstName = document.querySelector(".firstName").value;
  const lastName = document.querySelector(".lastName").value;
  const authorId = document.querySelector(".author-id").value;

  const apiURL = `http://localhost:8080/author/updateAuthor/${authorId}`;

  if (!firstName || !authorId || !lastName) {
    alert("Proszę podać wszystkie dane!");
    return;
  }

  const requestData = {
    firstName: firstName,
    lastName: lastName,
    books: [],
  };

  try {
    const response = await fetch(apiURL, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    });

    if (!response.ok) {
      throw new Error(`Błąd: ${response.status}`);
    }

    const updatedAuthor = await response.json();
    console.log(updatedAuthor);

    updated.textContent = `Zaktualizowano autora od ID: ${updatedAuthor.author_id}`;
    updated.classList.remove("invis");
  } catch (error) {
    console.error("Błąd:", error.message);
    alert("Wystąpił problem z aktualizacją książki.");
  }
});
