const apiURL = "http://localhost:8080/author/addAuthor";

const added = document.querySelector(".added");

document.querySelector("form").addEventListener("submit", async (event) => {
  event.preventDefault();

  const firstName = document.querySelector(".firstName").value.trim();
  const lastName = document.querySelector(".lastName").value.trim();

  if (!firstName || !lastName) {
    alert("Proszę podać imię i nazwisko autora!");
    return;
  }

  const requestData = {
    firstName: firstName,
    lastName: lastName,
    books: [],
  };
  console.log("Dane wysyłane do API:", requestData);
  try {
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
    added.textContent = `Dodano autora o ID: ${data.author_id}`;
    added.classList.remove("invis");
  } catch (error) {
    alert("Nie udało się dodać autora. Sprawdź dane i spróbuj ponownie.");
    console.error("Błąd:", error.message);
  }
});
