package com.example.server.controllers;

import com.example.server.dto.BookDTO;
import com.example.server.entity.BookEntity;
import com.example.server.services.LibraryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;

    @PostMapping("/booksAdd")
    public ResponseEntity<BookEntity> addBook(@RequestBody BookDTO bookDTO) {
        BookEntity book = new BookEntity();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setAvailableCopies(bookDTO.getAvailableCopies());
        BookEntity createdBook = libraryService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping("/booksGet")
    public ResponseEntity<List<BookEntity>> getAllBooks() {
        try {
            List<BookEntity> books = libraryService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/booksDelete/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Integer id) {
        boolean isDeleted = libraryService.deleteBookById(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/booksGet/{id}")
    public ResponseEntity<BookEntity> getBookById(@PathVariable Integer id) {
        BookEntity book = libraryService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PutMapping("/booksUpdate/{id}")
    public ResponseEntity<BookEntity> updateBook(@PathVariable Integer id, @RequestBody BookDTO bookDTO) {
        BookEntity book = libraryService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        if (bookDTO.getTitle() != null) {
            book.setTitle(bookDTO.getTitle());
        }
        if (bookDTO.getAuthor() != null) {
            book.setAuthor(bookDTO.getAuthor());
        }
        if (bookDTO.getIsbn() != null) {
            book.setIsbn(bookDTO.getIsbn());
        }
        if (bookDTO.getAvailableCopies() != null) {
            book.setAvailableCopies(bookDTO.getAvailableCopies());
        }
        BookEntity updatedBook = libraryService.updateBook(book);
        return ResponseEntity.ok(updatedBook);
    }

    @PostMapping("/myBookBorrow/{bookId}")
    public ResponseEntity<Void> borrowBook(@PathVariable Integer bookId, @RequestHeader("Authorization") String token) {
        String authToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        boolean isBorrowed = libraryService.borrowBook(bookId, authToken);
        if (isBorrowed) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/myBooksGet")
    public ResponseEntity<List<BookEntity>> getMyBooks(@RequestHeader("Authorization") String token) {
        String authToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        List<BookEntity> books = libraryService.getMyBooks(authToken);
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/myBookReturn/{bookId}")
    public ResponseEntity<Void> returnBook(@PathVariable Integer bookId, @RequestHeader("Authorization") String token) {
        String authToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        Integer userId = LibraryService.extractUserId(authToken);
        boolean isReturned = libraryService.returnBook(bookId, userId);
        if (isReturned) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
