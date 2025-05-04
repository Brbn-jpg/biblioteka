package com.api.biblioteka;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        // Arrange
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        // Act
        List<Book> actualBooks = bookController.getAllBooks();

        // Assert
        assertEquals(expectedBooks.size(), actualBooks.size(), "Should return the same number of books");
        assertEquals(expectedBooks.get(0).getId(), actualBooks.get(0).getId(), "Book IDs should match");
        assertEquals(expectedBooks.get(0).getName(), actualBooks.get(0).getName(), "Book names should match");
        assertEquals(expectedBooks.get(0).getAuthor().getAuthor_id(), actualBooks.get(0).getAuthor().getAuthor_id(), "Book author IDs should match");
        verify(bookRepository, times(1)).findAll();
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(authorRepository);
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // Act
        ResponseEntity<Optional<Book>> response = bookController.getBookById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertTrue(response.getBody().isPresent(), "Response body should be present");
        assertEquals(testBook.getId(), response.getBody().get().getId(), "Book IDs should match");
        assertEquals(testBook.getName(), response.getBody().get().getName(), "Book names should match");
        assertEquals(testBook.getAuthor().getAuthor_id(), response.getBody().get().getAuthor().getAuthor_id(), "Book author IDs should match");
        verify(bookRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(authorRepository);
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Optional<Book>> response = bookController.getBookById(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null");
        verify(bookRepository, times(1)).findById(999L);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(authorRepository);
    }

    @Test
    void getBookDetailed_WhenBookExists_ShouldReturnBookDTO() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // Act
        ResponseEntity<BookDTO> response = bookController.getBookDetailed(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        BookDTO bookDTO = response.getBody();
        assertNotNull(bookDTO, "BookDTO should not be null");
        assertEquals(testBook.getId(), bookDTO.getId(), "Book IDs should match");
        assertEquals(testBook.getName(), bookDTO.getName(), "Book names should match");
        assertEquals(testBook.getAuthor().getAuthor_id(), bookDTO.getAuthor().getId(), "Book author IDs should match");
        assertEquals(testBook.getAuthor().getFirstName(), bookDTO.getAuthor().getFirstName(), "Book author first names should match");
        assertEquals(testBook.getAuthor().getLastName(), bookDTO.getAuthor().getLastName(), "Book author last names should match");
        verify(bookRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(authorRepository);
    }

    @Test
    void getBookDetailed_WhenBookDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<BookDTO> response = bookController.getBookDetailed(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null");
        verify(bookRepository, times(1)).findById(999L);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(authorRepository);
    }

    @Test
    void addBook_WhenAuthorExists_ShouldAddBookAndReturnBookDTO() {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "New Book");
        payload.put("author_id", "1");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            assertEquals("New Book", savedBook.getName(), "Book name should match payload");
            assertEquals(testAuthor, savedBook.getAuthor(), "Book author should match");
            savedBook.setId(2L);
            return savedBook;
        });

        // Act
        ResponseEntity<BookDTO> response = bookController.addBook(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        BookDTO bookDTO = response.getBody();
        assertNotNull(bookDTO, "BookDTO should not be null");
        assertEquals(2L, bookDTO.getId(), "Book ID should be set");
        assertEquals("New Book", bookDTO.getName(), "Book name should match payload");
        assertNotNull(bookDTO.getAuthor(), "Book author should not be null");
        assertEquals(1L, bookDTO.getAuthor().getId(), "Book author ID should match");
        assertEquals("John", bookDTO.getAuthor().getFirstName(), "Book author first name should match");
        assertEquals("Pork", bookDTO.getAuthor().getLastName(), "Book author last name should match");
        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void addBook_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "New Book");
        payload.put("author_id", "999");

        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<BookDTO> response = bookController.addBook(payload);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null");
        verify(authorRepository, times(1)).findById(999L);
        verify(bookRepository, never()).save(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void updateBook_WhenBookAndAuthorExist_ShouldUpdateAndReturnBook() {
        // Arrange
        Book updatedBook = new Book();
        updatedBook.setName("Updated Book");

        Author newAuthor = new Author();
        newAuthor.setAuthor_id(2L);
        newAuthor.setFirstName("Jane");
        newAuthor.setLastName("Smith");
        updatedBook.setAuthor(newAuthor);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(authorRepository.findById(2L)).thenReturn(Optional.of(newAuthor));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            assertEquals(1L, savedBook.getId(), "Book ID should remain the same");
            assertEquals("Updated Book", savedBook.getName(), "Book name should be updated");
            assertEquals(newAuthor, savedBook.getAuthor(), "Book author should be updated");
            return savedBook;
        });

        // Act
        ResponseEntity<Book> response = bookController.updateBook(1L, updatedBook);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        Book returnedBook = response.getBody();
        assertNotNull(returnedBook, "Returned book should not be null");
        assertEquals(1L, returnedBook.getId(), "Book ID should remain the same");
        assertEquals("Updated Book", returnedBook.getName(), "Book name should be updated");
        assertEquals(newAuthor, returnedBook.getAuthor(), "Book author should be updated");
        verify(bookRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).findById(2L);
        verify(bookRepository, times(1)).save(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void updateBook_WhenBookDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Book> response = bookController.updateBook(999L, new Book());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null");
        verify(bookRepository, times(1)).findById(999L);
        verify(bookRepository, never()).save(any(Book.class));
        verifyNoInteractions(authorRepository);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void updateBook_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        Book updatedBook = new Book();
        updatedBook.setName("Updated Book");

        Author nonExistentAuthor = new Author();
        nonExistentAuthor.setAuthor_id(999L);
        updatedBook.setAuthor(nonExistentAuthor);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Book> response = bookController.updateBook(1L, updatedBook);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null");
        verify(bookRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).findById(999L);
        verify(bookRepository, never()).save(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    void deleteBook_WhenBookExists_ShouldDeleteAndReturnNoContent() {
        // Arrange
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Status code should be NO_CONTENT");
        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(authorRepository);
    }

    @Test
    void deleteBook_WhenBookDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(bookRepository.existsById(999L)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be NOT_FOUND");
        verify(bookRepository, times(1)).existsById(999L);
        verify(bookRepository, never()).deleteById(any());
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(authorRepository);
    }
    
    @Test
    void deleteBook_WhenExceptionOccurs_ShouldReturnServerError() {
        // Arrange
        when(bookRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(bookRepository).deleteById(1L);

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Status code should be INTERNAL_SERVER_ERROR");
        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(authorRepository);
    }
}