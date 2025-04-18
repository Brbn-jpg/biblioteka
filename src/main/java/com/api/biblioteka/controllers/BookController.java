package com.api.biblioteka.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.api.biblioteka.dto.BookDTO;
import com.api.biblioteka.dto.BookDTO.AuthorDTO;
import com.api.biblioteka.model.Author;
import com.api.biblioteka.model.Book;
import com.api.biblioteka.repository.AuthorRepository;
import com.api.biblioteka.repository.BookRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "http://localhost:8080")
public class BookController {
    
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping("")
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity <Optional<Book>> getBookById(@PathVariable Long id){
        Optional <Book> book = bookRepository.findById(id);
        if(book.isPresent()){
           return ResponseEntity.ok(book);
        }else{
           return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/details")
    public ResponseEntity <BookDTO> getBookDetailed(@PathVariable Long id){
        Optional <Book> book = bookRepository.findById(id);
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

    @PostMapping("/addBook")
    public ResponseEntity<BookDTO> addBook(@RequestBody Map<String, Object> payload) {
        try {
            String name = (String) payload.get("name");
            Long authorId = Long.valueOf((String) payload.get("author_id"));
            Optional<Author> authorOptional = authorRepository.findById(authorId);
    
            if (authorOptional.isPresent()) {
                Author author = authorOptional.get();
                Book book = new Book();
                book.setName(name);
                book.setAuthor(author);
                Book savedBook = bookRepository.save(book);
    
                BookDTO bookDTO = new BookDTO();
                bookDTO.setId(savedBook.getId());
                bookDTO.setName(savedBook.getName());
                bookDTO.setAuthor(
                    new BookDTO.AuthorDTO(
                        author.getAuthor_id(),
                        author.getFirstName(),
                        author.getLastName()
                    )
                );
    
                return ResponseEntity.ok(bookDTO);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    

    @PutMapping("/updateBook/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            // book.setName("abc");
            book.setName(bookDetails.getName());

            if (bookDetails.getAuthor() != null && bookDetails.getAuthor().getAuthor_id() != null) {
                Optional<Author> authorOptional = authorRepository.findById(bookDetails.getAuthor().getAuthor_id());
                if (authorOptional.isPresent()) {
                    book.setAuthor(authorOptional.get());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            }

            bookRepository.save(book);
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            if (bookRepository.existsById(id)) {
                bookRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}