package com.api.biblioteka;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

import com.api.biblioteka.controllers.AuthorController;
import com.api.biblioteka.dto.AuthorDTO;
import com.api.biblioteka.model.Author;
import com.api.biblioteka.model.Book;
import com.api.biblioteka.repository.AuthorRepository;
import com.api.biblioteka.repository.BookRepository;

public class AuthorControllerTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AuthorController authorController;

    private Author testAuthor;
    private Book testBook;

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

        testAuthor.setBooks(Collections.singletonList(testBook));
    }

    @Test
    void getAllBooks_ShouldReturnAllAuthors() {
        // Arrange
        List<Author> expectedAuthors = Arrays.asList(testAuthor);
        when(authorRepository.findAll()).thenReturn(expectedAuthors);

        // Act
        List<Author> actualAuthors = authorController.getAllBooks();

        // Assert
        assertEquals(expectedAuthors.size(), actualAuthors.size(), "Should return the same number of authors");
        assertEquals(expectedAuthors.get(0).getAuthor_id(), actualAuthors.get(0).getAuthor_id(), "Author IDs should match");
        assertEquals(expectedAuthors.get(0).getFirstName(), actualAuthors.get(0).getFirstName(), "Author first names should match");
        assertEquals(expectedAuthors.get(0).getLastName(), actualAuthors.get(0).getLastName(), "Author last names should match");
        verify(authorRepository, times(1)).findAll();
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void getAuthorById_WhenAuthorExists_ShouldReturnAuthor() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));

        // Act
        ResponseEntity<Optional<Author>> response = authorController.getAuthorById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertTrue(response.getBody().isPresent(), "Response body should be present");
        assertEquals(testAuthor.getAuthor_id(), response.getBody().get().getAuthor_id(), "Author IDs should match");
        assertEquals(testAuthor.getFirstName(), response.getBody().get().getFirstName(), "Author first names should match");
        assertEquals(testAuthor.getLastName(), response.getBody().get().getLastName(), "Author last names should match");
        verify(authorRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void getAuthorById_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Optional<Author>> response = authorController.getAuthorById(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null");
        verify(authorRepository, times(1)).findById(999L);
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void getAuthorDetailed_WhenAuthorExists_ShouldReturnAuthorDTO() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));

        // Act
        ResponseEntity<AuthorDTO> response = authorController.getAuthorDetailed(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        AuthorDTO authorDTO = response.getBody();
        assertNotNull(authorDTO, "AuthorDTO should not be null");
        assertEquals(testAuthor.getAuthor_id(), authorDTO.getId(), "Author IDs should match");
        assertEquals(testAuthor.getFirstName(), authorDTO.getFirstName(), "Author first names should match");
        assertEquals(testAuthor.getLastName(), authorDTO.getLastName(), "Author last names should match");
        assertEquals(testAuthor.getBooks().size(), authorDTO.getBooks().size(), "Books count should match");
        assertEquals(testAuthor.getBooks().get(0).getId(), authorDTO.getBooks().get(0).getId(), "Book IDs should match");
        verify(authorRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void getAuthorDetailed_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<AuthorDTO> response = authorController.getAuthorDetailed(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null");
        verify(authorRepository, times(1)).findById(999L);
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void addAuthor_ShouldSaveAndReturnAuthor() {
        // Arrange
        when(authorRepository.save(any(Author.class))).thenReturn(testAuthor);

        // Act
        ResponseEntity<Author> response = authorController.addAuthor(testAuthor);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(testAuthor.getAuthor_id(), response.getBody().getAuthor_id(), "Author IDs should match");
        assertEquals(testAuthor.getFirstName(), response.getBody().getFirstName(), "Author first names should match");
        assertEquals(testAuthor.getLastName(), response.getBody().getLastName(), "Author last names should match");
        verify(authorRepository, times(1)).save(testAuthor);
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void addAuthor_WhenExceptionOccurs_ShouldReturnServerError() {
        // Arrange
        when(authorRepository.save(any(Author.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Author> response = authorController.addAuthor(testAuthor);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Status code should be INTERNAL_SERVER_ERROR");
        assertNull(response.getBody(), "Response body should be null");
        verify(authorRepository, times(1)).save(testAuthor);
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void updateAuthor_WhenAuthorExists_ShouldUpdateAndReturnAuthor() {
        // Arrange
        Author updatedAuthor = new Author();
        updatedAuthor.setFirstName("Tim");
        updatedAuthor.setLastName("Cheese");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> {
            Author savedAuthor = invocation.getArgument(0);
            assertEquals("Tim", savedAuthor.getFirstName(), "First name should be updated");
            assertEquals("Cheese", savedAuthor.getLastName(), "Last name should be updated");
            assertEquals(1L, savedAuthor.getAuthor_id(), "ID should remain the same");
            return savedAuthor;
        });

        // Act
        ResponseEntity<?> response = authorController.updateAuthor(1L, updatedAuthor);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        Author returnedAuthor = (Author) response.getBody();
        assertNotNull(returnedAuthor, "Returned author should not be null");
        assertEquals("Tim", returnedAuthor.getFirstName(), "First name should be updated");
        assertEquals("Cheese", returnedAuthor.getLastName(), "Last name should be updated");
        assertEquals(1L, returnedAuthor.getAuthor_id(), "ID should remain the same");
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(any(Author.class));
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void updateAuthor_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(authorRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authorController.updateAuthor(999L, new Author());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be NOT_FOUND");
        assertEquals("Author not found", response.getBody(), "Response body should indicate author not found");
        verify(authorRepository, times(1)).findById(999L);
        verify(authorRepository, never()).save(any(Author.class));
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void updateAuthor_WhenExceptionOccurs_ShouldReturnServerError() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(any(Author.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = authorController.updateAuthor(1L, new Author());

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Status code should be INTERNAL_SERVER_ERROR");
        assertTrue(response.getBody().toString().contains("Error: Database error"), "Response body should contain error message");
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(any(Author.class));
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void deleteAuthor_WhenAuthorExists_ShouldDeleteBooksAndAuthorAndReturnNoContent() {
        // Arrange
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        doNothing().when(bookRepository).deleteAll(any());
        doNothing().when(authorRepository).deleteById(1L);

        // Act
        ResponseEntity<Void> response = authorController.deleteAuthor(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Status code should be NO_CONTENT");
        verify(authorRepository, times(1)).existsById(1L);
        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).deleteAll(testAuthor.getBooks());
        verify(authorRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(authorRepository);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void deleteAuthor_WhenAuthorDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(authorRepository.existsById(999L)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = authorController.deleteAuthor(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status code should be NOT_FOUND");
        verify(authorRepository, times(1)).existsById(999L);
        verify(authorRepository, never()).findById(any());
        verify(bookRepository, never()).deleteAll(any());
        verify(authorRepository, never()).deleteById(any());
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    void deleteAuthor_WhenExceptionOccurs_ShouldReturnServerError() {
        // Arrange
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(authorRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Void> response = authorController.deleteAuthor(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Status code should be INTERNAL_SERVER_ERROR");
        verify(authorRepository, times(1)).existsById(1L);
        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, never()).deleteAll(any());
        verify(authorRepository, never()).deleteById(any());
        verifyNoMoreInteractions(authorRepository);
        verifyNoInteractions(bookRepository);
    }
}