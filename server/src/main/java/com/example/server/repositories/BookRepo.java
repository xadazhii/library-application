package com.example.server.repositories;

import com.example.server.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lombok.NonNull;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<BookEntity, Integer> {
    boolean existsById(@NonNull Integer id);
    void deleteById(@NonNull Integer id);
    Optional<BookEntity> findByIsbn(String isbn);
}
