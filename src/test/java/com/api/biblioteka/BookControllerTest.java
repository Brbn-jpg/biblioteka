package com.api.biblioteka;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.api.biblioteka.controllers.BookController;
import com.api.biblioteka.dto.BookDTO;
import com.api.biblioteka.model.Author;
import com.api.biblioteka.model.Book;
import com.api.biblioteka.repository.AuthorRepository;
import com.api.biblioteka.repository.BookRepository;

public class BookControllerTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookController bookController;

    private Book testBook;
    private Author testAuthor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testAuthor = new Author();
        testAuthor.setAuthor_id(1L);
        testAuthor.setFirstName("John");
        testAuthor.setLastName("Pork");

        testBook = new Book();
        testBook.setId(1L);
        testBook.setName("Test Book");
        testBook.setAuthor(testAuthor);
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() {
        // Test if all books are returned
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookController.getAllBooks();

        assertEquals(expectedBooks, actualBooks);
        verify(bookRepository).findAll();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        // Test if the correct book is returned by ID
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        ResponseEntity<Optional<Book>> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals(testBook, response.getBody().get());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldReturnNotFound() {
        // Test if NOT_FOUND is returned when book doesn't exist
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Optional<Book>> response = bookController.getBookById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookRepository).findById(999L);
    }

    @Test
    void getBookDetailed_WhenBookExists_ShouldReturnBookDTO() {
        // Test if detailed book data is returned
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        ResponseEntity<BookDTO> response = bookController.getBookDetailed(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookDTO bookDTO = response.getBody();
        assertNotNull(bookDTO);
        assertEquals(testBook.getId(), bookDTO.getId());
        assertEquals(testBook.getName(), bookDTO.getName());
        assertEquals(testBook.getAuthor().getAuthor_id(), bookDTO.getAuthor().getId());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookDetailed_WhenBookDoesNotExist_ShouldReturnNotFound() {
        // Test if NOT_FOUND is returned for detailed book data when book doesn't exist
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<BookDTO> response = bookController.getBookDetailed(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookRepository).findById(999L);
    }

    @Test
    void addBook_WhenAuthorExists_ShouldAddBookAndReturnBookDTO() {
        // Test if a new book is added when the author exists
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "New Book");
        payload.put("author_id", "1");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(2L);
            return savedBook;
        });

        ResponseEntity<BookDTO> response = bookController.addBook(payload);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookDTO bookDTO = response.getBody();
        assertNotNull(bookDTO);
        assertEquals(2L, bookDTO.getId());
        assertEquals("New Book", bookDTO.getName());
        assertEquals(1L, bookDTO.getAuthor().getId());
        verify(authorRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void addBook_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Test if NOT_FOUND is returned when the author doesn't exist
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "New Book");
        payload.put("author_id", "999");

        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<BookDTO> response = bookController.addBook(payload);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authorRepository).findById(999L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateBook_WhenBookAndAuthorExist_ShouldUpdateAndReturnBook() {
        // Test if an existing book is updated
        Book updatedBook = new Book();
        updatedBook.setName("Updated Book");

        Author newAuthor = new Author();
        newAuthor.setAuthor_id(2L);
        updatedBook.setAuthor(newAuthor);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(authorRepository.findById(2L)).thenReturn(Optional.of(newAuthor));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Book> response = bookController.updateBook(1L, updatedBook);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Book returnedBook = response.getBody();
        assertNotNull(returnedBook);
        assertEquals("Updated Book", returnedBook.getName());
        assertEquals(newAuthor, returnedBook.getAuthor());
        verify(bookRepository).findById(1L);
        verify(authorRepository).findById(2L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBook_WhenBookDoesNotExist_ShouldReturnNotFound() {
        // Test if NOT_FOUND is returned when updating a non-existent book
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookController.updateBook(999L, new Book());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookRepository).findById(999L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateBook_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Test if NOT_FOUND is returned when the new author doesn't exist
        Book updatedBook = new Book();
        updatedBook.setName("Updated Book");

        Author nonExistentAuthor = new Author();
        nonExistentAuthor.setAuthor_id(999L);
        updatedBook.setAuthor(nonExistentAuthor);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookController.updateBook(1L, updatedBook);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookRepository).findById(1L);
        verify(authorRepository).findById(999L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldDeleteAndReturnNoContent() {
        // Test if a book is deleted when it exists
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookRepository).existsById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_WhenBookDoesNotExist_ShouldReturnNotFound() {
        // Test if NOT_FOUND is returned when deleting a non-existent book
        when(bookRepository.existsById(999L)).thenReturn(false);

        ResponseEntity<Void> response = bookController.deleteBook(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookRepository).existsById(999L);
        verify(bookRepository, never()).deleteById(any());
    }
}