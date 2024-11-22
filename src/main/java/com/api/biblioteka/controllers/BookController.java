package com.api.biblioteka.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.api.biblioteka.dto.BookDTO;
import com.api.biblioteka.dto.BookDTO.AuthorDTO;
import com.api.biblioteka.model.Book;
import com.api.biblioteka.repository.BookRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/books")
public class BookController {
    
    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public List<Book> getAllBooks(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity <Optional<Book>> getBookById(@PathVariable Long id){
        Optional <Book> book = repository.findById(id);
        if(book.isPresent()){
           return ResponseEntity.ok(book);
        }else{
           return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/details")
    public ResponseEntity <BookDTO> getBookDetailed(@PathVariable Long id){
        Optional <Book> book = repository.findById(id);
        if(book.isPresent()){
            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(book.get().getId());
            bookDTO.setName(book.get().getName());
            bookDTO.setAuthor(new AuthorDTO(book.get().getAuthor().getAuthor_id(),book.get().getAuthor().getFirstName(), book.get().getAuthor().getLastName()));

            return ResponseEntity.ok(bookDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
