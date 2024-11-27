const apiURL = "http://localhost:8080/books/addBook";

const added = document.querySelector(".added");

document.querySelector("form").addEventListener("submit", async (event) => {
  event.preventDefault();

  const title = document.querySelector(".title").value.trim();
  const authorId = document.querySelector(".author-id").value.trim();

  if (!title || !authorId) {
    alert("Proszę podać tytuł książki i ID autora!");
    return;
  }

  const requestData = {
    name: title,
    author_id: authorId,
  };
  console.log("Dane wysyłane do API:", requestData);
  try {
    // Wysłanie żądania POST
    const response = await fetch(apiURL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    });

    if (!response.ok) {
      throw new Error(`Błąd: ${response.status}`);
    }

    const data = await response.json();
    console.log(data);

    added.textContent = `Dodano ksiązkę o ID: ${data.id}`;
    added.classList.remove("invis");
  } catch (error) {
    alert("Nie udało się dodać książki. Sprawdź dane i spróbuj ponownie.");
    console.error("Błąd:", error.message);
  }
});
