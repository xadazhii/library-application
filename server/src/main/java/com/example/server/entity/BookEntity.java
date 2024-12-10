package com.example.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column
    private String title;

    @NonNull
    @Column
    private String author;

    @NonNull
    @Column
    private String isbn;

    @NonNull
    @Column
    private Integer availableCopies;

    @ManyToMany
    @JoinTable(
            name = "borrowed_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference
    List<UserEntity> borrowedByUsers;

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<TransactionEntity> transactions;
}
