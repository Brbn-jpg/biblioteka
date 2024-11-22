package com.api.biblioteka.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.biblioteka.dto.AuthorDTO;
import com.api.biblioteka.model.Author;
import com.api.biblioteka.repository.AuthorRepository;

@RestController
@RequestMapping("/author")
public class AuthorController {
    
    private final AuthorRepository repository;

        public AuthorController(AuthorRepository repository) {
        this.repository = repository;
    }
        @GetMapping("")
        public List<Author> getAllBooks(){
            return repository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity <Optional<Author>> getAuthorById(@PathVariable Long id){
            Optional<Author> author = repository.findById(id);
            if(author.isPresent()){
                return ResponseEntity.ok(author);
            }else{
                return ResponseEntity.notFound().build();
            }
        }

        @GetMapping("/{id}/details")
        public ResponseEntity<AuthorDTO> getAuthorDetailed(@PathVariable Long id){
            Optional<Author> author = repository.findById(id);
            if(author.isPresent()){
                AuthorDTO authorDTO = new AuthorDTO();

                authorDTO.setId(author.get().getAuthor_id());
                authorDTO.setFirstName(author.get().getFirstName());
                authorDTO.setLastName(author.get().getLastName());
                authorDTO.setBooks(author.get().getBooks());

                return ResponseEntity.ok(authorDTO);
            }else{
                return ResponseEntity.notFound().build();
            }
        }
}
