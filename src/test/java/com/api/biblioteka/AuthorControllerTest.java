package com.api.biblioteka;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAuthorByIdNotFound() throws Exception {
        mockMvc.perform(get("/author/999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAuthorById() throws Exception {
        mockMvc.perform(get("/author/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author_id").value(1))
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"));
    }

    @Test
    public void testGetAuthorByIdDetailed() throws Exception {
        mockMvc.perform(get("/author/2/details")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Nowak"))
                .andExpect(jsonPath("$.books").isArray())
                .andExpect(jsonPath("$.books[0].name").value("Książka B"))
                .andExpect(jsonPath("$.books[0].id").value(2));
    }

    @Test
    public void testAddAuthor() throws Exception {
        String newAuthorJson = "{\"firstName\":\"Zbyszek\",\"lastName\":\"Kowalski\",\"books\":[]}";

        mockMvc.perform(post("/author/addAuthor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newAuthorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Zbyszek"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"));
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        String updatedAuthorJson = "{\"firstName\":\"Nowe imie\",\"lastName\":\"Nowe nazwisko\",\"books\":[]}";

        mockMvc.perform(put("/author/updateAuthor/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedAuthorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Nowe imie"))
                .andExpect(jsonPath("$.lastName").value("Nowe nazwisko"));
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/author/deleteAuthor/1"))
                .andExpect(status().isNoContent());
    }
}
