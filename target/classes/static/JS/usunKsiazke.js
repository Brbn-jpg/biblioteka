const deleted = document.querySelector(".deleted");

document.querySelector("form").addEventListener("submit", async (event) => {
  event.preventDefault();

  const bookId = document.querySelector(".book-id").value;

  const apiURL = `http://localhost:8080/books/deleteBook/${bookId}`;

  if (!bookId) {
    alert("Proszę wprowadzić id książki!");
    return;
  }

  try {
    const response = await fetch(apiURL, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error(`Błąd: ${response.status}`);
    }

    deleted.textContent = `Usunięto książkę o ID: ${bookId}`;
    deleted.classList.remove("invis");
  } catch (error) {
    console.error("Błąd:", error.message);
    alert("Wystąpił problem z usuwaniem książki.");
  }
});
