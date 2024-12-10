package com.example.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionsDTO {

    @JsonProperty("id")
    private Integer transactionId;

    @JsonProperty("user")
    @NonNull
    private UserDTO userId;

    @JsonProperty("book")
    @NonNull
    private BookDTO bookId;

    @NonNull
    private String action;

    @NonNull
    private LocalDate date;
}
