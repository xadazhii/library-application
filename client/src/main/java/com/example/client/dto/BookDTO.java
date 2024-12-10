package com.example.client.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Integer id;

    @NonNull
    private String title;

    @NonNull
    private String author;

    @NonNull
    private String isbn;

    @NonNull
    private Integer availableCopies;
}
