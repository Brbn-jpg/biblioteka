package com.api.biblioteka;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetBookById() throws Exception {
        mockMvc.perform(get("/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Książka A"));
    }

    @Test
    public void testGetBookByIdDetailed() throws Exception {
        mockMvc.perform(get("/books/3/details")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Książka C"))
                .andExpect(jsonPath("$.author.first_name").value("Piotr"))
                .andExpect(jsonPath("$.author.last_name").value("Zieliński"))
                .andExpect(jsonPath("$.author.author_id").value(3));
    }

    @Test
    public void testGetBookByIdNotFound() throws Exception {
        mockMvc.perform(get("/books/999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateBook() throws Exception {
        String updatedBookJson = "{\"name\":\"nowa ksiazka\",\"author\":{\"author_id\":2}}";
    
        mockMvc.perform(put("/books/updateBook/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("nowa ksiazka"))
                .andExpect(jsonPath("$.author.author_id").value(2))
                .andExpect(jsonPath("$.author.firstName").value("Anna"))
                .andExpect(jsonPath("$.author.lastName").value("Nowak"));
    }
    




    @Test
    public void testAddBook() throws Exception {
        String newBookJson = "{\"name\":\"ksiazka abc\",\"author_id\":\"1\"}";

        mockMvc.perform(post("/books/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.name").value("ksiazka abc"))
                .andExpect(jsonPath("$.author.first_name").value("Jan"))
                .andExpect(jsonPath("$.author.last_name").value("Kowalski"))
                .andExpect(jsonPath("$.author.author_id").value(1));
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/books/deleteBook/1"))
                .andExpect(status().isNoContent());
    }
}