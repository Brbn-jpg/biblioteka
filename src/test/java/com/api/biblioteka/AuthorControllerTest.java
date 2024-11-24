package com.api.biblioteka;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    public void testAddAuthor() throws Exception {
        String newAuthorJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"books\":[]}";

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
