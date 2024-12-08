const updated = document.querySelector(".updated");
let booksData = [];

async function populateBooksSelect() {
  const selectElement = document.querySelector(".book-id");
  try {
    const response = await fetch("http://localhost:8080/books");

    if (!response.ok) {
      throw new Error(`Błąd: ${response.status}`);
    }

    const books = await response.json();
    booksData = books;

    books.forEach((book) => {
      const option = document.createElement("option");
      option.value = book.id;
      option.textContent = `${book.name}`;
      selectElement.appendChild(option);
    });
  } catch (error) {
    console.error("Błąd podczas wczytywania ksiązek:", error.message);
    alert("Nie udało się załadować listy ksiązek.");
  }
}

function SelectChange(event) {
  const selectedBookId = event.target.value;
  const selectedBook = booksData.find((book) => book.id == selectedBookId);

  if (selectedBook) {
    document.querySelector(".title").value = selectedBook.name;
  }
}

document.addEventListener("DOMContentLoaded", populateBooksSelect);
document.querySelector(".book-id").addEventListener("change", SelectChange);

document.querySelector("form").addEventListener("submit", async (event) => {
  event.preventDefault();

  const title = document.querySelector(".title").value;
  const authorId = document.querySelector(".author-id").value;
  const bookId = document.querySelector(".book-id").value;

  const apiURL = `http://localhost:8080/books/updateBook/${bookId}`;

  if (!title || !authorId || !bookId) {
    alert("Proszę podać wszystkie dane!");
    return;
  }

  const requestData = {
    name: title,
    author_id: authorId,
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

    const updatedBook = await response.json();
    console.log(updatedBook);

    updated.textContent = `Zaktualizowano ksiązkę od ID: ${updatedBook.id}`;
    updated.classList.remove("invis");
  } catch (error) {
    console.error("Błąd:", error.message);
    alert("Wystąpił problem z aktualizacją książki.");
  }
});
