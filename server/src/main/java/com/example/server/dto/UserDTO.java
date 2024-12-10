package com.example.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor @JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private String username;

    private String password;

    private String email;

    private Integer roleId;
}
