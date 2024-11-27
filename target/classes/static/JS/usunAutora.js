const deleted = document.querySelector(".deleted");

document.querySelector("form").addEventListener("submit", async (event) => {
  event.preventDefault();

  const authorId = document.querySelector(".author-id").value;

  const apiURL = `http://localhost:8080/author/deleteAuthor/${authorId}`;

  if (!authorId) {
    alert("Proszę wprowadzić id autora!");
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

    deleted.textContent = `Usunięto authora o ID: ${authorId}`;
    deleted.classList.remove("invis");
  } catch (error) {
    console.error("Błąd:", error.message);
    alert("Wystąpił problem z usuwaniem autora.");
  }
});
