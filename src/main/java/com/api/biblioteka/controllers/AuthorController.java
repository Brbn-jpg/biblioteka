package com.api.biblioteka.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.biblioteka.dto.AuthorDTO;
import com.api.biblioteka.model.Author;
import com.api.biblioteka.repository.AuthorRepository;
import com.api.biblioteka.repository.BookRepository;

@RestController
@RequestMapping("/author")
@CrossOrigin(origins = "http://localhost:8080")
public class AuthorController {
    
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

        public AuthorController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        }

        @GetMapping("")
        public List<Author> getAllBooks(){
            return authorRepository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity <Optional<Author>> getAuthorById(@PathVariable Long id){
            Optional<Author> author = authorRepository.findById(id);
            if(author.isPresent()){
                return ResponseEntity.ok(author);
            }else{
                return ResponseEntity.notFound().build();
            }
        }

        @GetMapping("/{id}/details")
        public ResponseEntity<AuthorDTO> getAuthorDetailed(@PathVariable Long id){
            Optional<Author> author = authorRepository.findById(id);
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

        @PostMapping("/addAuthor")
        public ResponseEntity<Author> addAuthor(@RequestBody Author author) {
            try {
                Author savedAuthor = authorRepository.save(author);
                return ResponseEntity.ok(savedAuthor);
            } catch (Exception e) {
                return ResponseEntity.status(500).build();
            }
        }

        @PutMapping("/updateAuthor/{id}")
        public ResponseEntity<?> updateAuthor(@PathVariable Long id, @RequestBody Author updatedAuthor) {
            try {
                Optional<Author> authorOptional = authorRepository.findById(id);
                if (authorOptional.isEmpty()) {
                    return ResponseEntity.status(404).body("Author not found");
                }

                Author author = authorOptional.get();
                if (updatedAuthor.getFirstName() != null && !updatedAuthor.getFirstName().isEmpty()) {
                    author.setFirstName(updatedAuthor.getFirstName());
                }
                if (updatedAuthor.getLastName() != null && !updatedAuthor.getLastName().isEmpty()) {
                    author.setLastName(updatedAuthor.getLastName());
                }
                if (updatedAuthor.getBooks() != null) {
                    author.setBooks(updatedAuthor.getBooks());
                }

                Author savedAuthor = authorRepository.save(author);
                return ResponseEntity.ok(savedAuthor);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error: " + e.getMessage());
            }
        }

        @DeleteMapping("/deleteAuthor/{id}")
        public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
            try {
                if (authorRepository.existsById(id)) {
                    Author author = authorRepository.findById(id).orElseThrow();
                    bookRepository.deleteAll(author.getBooks());
                    authorRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                } else {
                    return ResponseEntity.status(404).build();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).build();
            }
        }
}
