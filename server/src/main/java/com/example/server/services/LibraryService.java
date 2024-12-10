package com.example.server.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.server.entity.BookEntity;
import com.example.server.entity.TransactionEntity;
import com.example.server.entity.UserEntity;
import com.example.server.repositories.BookRepo;
import com.example.server.repositories.TransactionRepo;
import com.example.server.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LibraryService {

    private final TransactionRepo transactionRepository;
    private final BookRepo bookRepository;
    private final UserRepo userRepository;

    public BookEntity addBook(BookEntity book) {
        Optional<BookEntity> existingBook = bookRepository.findByIsbn(book.getIsbn());
        if (existingBook.isPresent()) {
            throw new RuntimeException("A book with this ISBN already exists.");
        }
        return bookRepository.save(book);
    }
    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public boolean deleteBookById(Integer id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public BookEntity getBookById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    public BookEntity updateBook(BookEntity book) {
        if (bookRepository.existsById(book.getId())) {
            return bookRepository.save(book);
        }
        return null;
    }

    public static Integer extractUserId(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return Integer.valueOf(decodedJWT.getSubject());
    }

    @Transactional
    public boolean borrowBook(Integer bookId, String authToken) {
        Integer userId = extractUserId(authToken);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));

        if (user.getBorrowedBooks().contains(book)) {
            throw new IllegalArgumentException("You have already borrowed this book.");
        }
        if (book.getAvailableCopies() > 0) {
            user.getBorrowedBooks().add(book);
            book.setAvailableCopies(book.getAvailableCopies() - 1);

            TransactionEntity transaction = TransactionEntity.builder()
                    .user(user)
                    .book(book)
                    .action("borrow")
                    .date(LocalDate.now())
                    .build();
            transactionRepository.save(transaction);

            bookRepository.save(book);
            userRepository.save(user);

            return true;
        }
        return false;
    }

    @Transactional
    public List<BookEntity> getMyBooks(String authToken) {
        Integer userId = extractUserId(authToken);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        return user.getBorrowedBooks();
    }

    @Transactional
    public boolean returnBook(Integer bookId, Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));

        if (user.getBorrowedBooks().contains(book)) {
            user.getBorrowedBooks().remove(book);

            book.setAvailableCopies(book.getAvailableCopies() + 1);

            TransactionEntity transaction = TransactionEntity.builder()
                    .user(user)
                    .book(book)
                    .action("return")
                    .date(LocalDate.now())
                    .build();

            transactionRepository.save(transaction);

            bookRepository.save(book);
            userRepository.save(user);

            return true;
        }
        return false;
    }
}
