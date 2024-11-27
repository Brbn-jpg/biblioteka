const updated = document.querySelector(".updated");

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
