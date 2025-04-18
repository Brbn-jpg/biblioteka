package com.api.biblioteka;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

import com.api.biblioteka.controllers.AuthorController;
import com.api.biblioteka.dto.AuthorDTO;
import com.api.biblioteka.model.Author;
import com.api.biblioteka.model.Book;
import com.api.biblioteka.repository.AuthorRepository;
import com.api.biblioteka.repository.BookRepository;

public class AuthorControllerTest {

    // Mock repositories for testing
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    // Inject mocks into the controller
    @InjectMocks
    private AuthorController authorController;

    // Test data
    private Author testAuthor;
    private Book testBook;

    @BeforeEach
    void setUp() {
        // Initialize mocks and test data
        MockitoAnnotations.openMocks(this);

        testAuthor = new Author();
        testAuthor.setAuthor_id(1L);
        testAuthor.setFirstName("John");
        testAuthor.setLastName("Pork");

        testBook = new Book();
        testBook.setId(1L);
        testBook.setName("Test Book");
        testBook.setAuthor(testAuthor);

        testAuthor.setBooks(Collections.singletonList(testBook));
    }

    @Test
    void getAllBooks_ShouldReturnAllAuthors() {
        // Test if all authors are returned
        List<Author> expectedAuthors = Arrays.asList(testAuthor);
        when(authorRepository.findAll()).thenReturn(expectedAuthors);

        List<Author> actualAuthors = authorController.getAllBooks();

        assertEquals(expectedAuthors, actualAuthors);
        verify(authorRepository).findAll();
    }

    @Test
    void getAuthorById_WhenAuthorExists_ShouldReturnAuthor() {
        // Test if the correct author is returned by ID
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));

        ResponseEntity<Optional<Author>> response = authorController.getAuthorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals(testAuthor, response.getBody().get());
        verify(authorRepository).findById(1L);
    }

    @Test
    void getAuthorById_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Test if NOT_FOUND is returned when author doesn't exist
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Optional<Author>> response = authorController.getAuthorById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authorRepository).findById(999L);
    }

    @Test
    void getAuthorDetailed_WhenAuthorExists_ShouldReturnAuthorDTO() {
        // Test if detailed author data is returned
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));

        ResponseEntity<AuthorDTO> response = authorController.getAuthorDetailed(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthorDTO authorDTO = response.getBody();
        assertNotNull(authorDTO);
        assertEquals(testAuthor.getAuthor_id(), authorDTO.getId());
        assertEquals(testAuthor.getFirstName(), authorDTO.getFirstName());
        assertEquals(testAuthor.getLastName(), authorDTO.getLastName());
        assertEquals(testAuthor.getBooks(), authorDTO.getBooks());
        verify(authorRepository).findById(1L);
    }

    @Test
    void getAuthorDetailed_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Test if NOT_FOUND is returned for detailed author data when author doesn't exist
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<AuthorDTO> response = authorController.getAuthorDetailed(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authorRepository).findById(999L);
    }

    @Test
    void addAuthor_ShouldSaveAndReturnAuthor() {
        // Test if a new author is saved and returned
        when(authorRepository.save(any(Author.class))).thenReturn(testAuthor);

        ResponseEntity<Author> response = authorController.addAuthor(testAuthor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testAuthor, response.getBody());
        verify(authorRepository).save(testAuthor);
    }

    @Test
    void addAuthor_WhenExceptionOccurs_ShouldReturnServerError() {
        // Test if INTERNAL_SERVER_ERROR is returned when an exception occurs
        when(authorRepository.save(any(Author.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Author> response = authorController.addAuthor(testAuthor);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(authorRepository).save(testAuthor);
    }

    @Test
    void updateAuthor_WhenAuthorExists_ShouldUpdateAndReturnAuthor() {
        // Test if an existing author is updated
        Author updatedAuthor = new Author();
        updatedAuthor.setFirstName("Tim");
        updatedAuthor.setLastName("Cheese");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = authorController.updateAuthor(1L, updatedAuthor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Author returnedAuthor = (Author) response.getBody();
        assertNotNull(returnedAuthor);
        assertEquals("Tim", returnedAuthor.getFirstName());
        assertEquals("Cheese", returnedAuthor.getLastName());
        verify(authorRepository).findById(1L);
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void updateAuthor_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Test if NOT_FOUND is returned when updating a non-existent author
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = authorController.updateAuthor(999L, new Author());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Author not found", response.getBody());
        verify(authorRepository).findById(999L);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void updateAuthor_WhenExceptionOccurs_ShouldReturnServerError() {
        // Test if INTERNAL_SERVER_ERROR is returned when an exception occurs during update
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(any(Author.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = authorController.updateAuthor(1L, new Author());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error: Database error"));
        verify(authorRepository).findById(1L);
    }

    @Test
    void deleteAuthor_WhenAuthorExists_ShouldDeleteBooksAndAuthorAndReturnNoContent() {
        // Test if an author and their books are deleted
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        doNothing().when(bookRepository).deleteAll(any());
        doNothing().when(authorRepository).deleteById(1L);

        ResponseEntity<Void> response = authorController.deleteAuthor(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(authorRepository).existsById(1L);
        verify(authorRepository).findById(1L);
        verify(bookRepository).deleteAll(testAuthor.getBooks());
        verify(authorRepository).deleteById(1L);
    }

    @Test
    void deleteAuthor_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Test if NOT_FOUND is returned when deleting a non-existent author
        when(authorRepository.existsById(999L)).thenReturn(false);

        ResponseEntity<Void> response = authorController.deleteAuthor(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authorRepository).existsById(999L);
        verify(bookRepository, never()).deleteAll(any());
        verify(authorRepository, never()).deleteById(any());
    }

    @Test
    void deleteAuthor_WhenExceptionOccurs_ShouldReturnServerError() {
        // Test if INTERNAL_SERVER_ERROR is returned when an exception occurs during delete
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(authorRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Void> response = authorController.deleteAuthor(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(authorRepository).existsById(1L);
        verify(authorRepository).findById(1L);
    }
}