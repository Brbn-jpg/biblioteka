package com.api.biblioteka;

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
    public void testAddBook() throws Exception {
        String newBookJson = "{\"name\":\"ksiazka abc\",\"author_id\":\"1\"}";

        mockMvc.perform(post("/books/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ksiazka abc"))
                .andExpect(jsonPath("$.author.author_id").value(1));
    }

    @Test
    public void testUpdateBook() throws Exception {
        String updatedBookJson = "{\"name\":\"Nowa nazwa ksiazki\",\"author_id\":\"1\"}";

        mockMvc.perform(put("/books/updateBook/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nowa nazwa ksiazki"))
                .andExpect(jsonPath("$.author.author_id").value(1));
    }
}