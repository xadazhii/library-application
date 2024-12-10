package com.example.server.controllers;

import com.example.server.dto.UserDTO;
import com.example.server.entity.UserEntity;
import com.example.server.services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/userAdd")
    public ResponseEntity<UserEntity> addUser(@RequestBody UserDTO userDTO) {
        UserEntity user = new UserEntity(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getRoleId());
        UserEntity createdUser = clientService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @DeleteMapping("/userDelete/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        boolean isDeleted = clientService.deleteUser(username);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/userUpdateRole/{username}")
    public ResponseEntity<UserEntity> updateUserRole(@PathVariable String username, @RequestParam String newRole) {
        UserEntity updatedUser = clientService.updateUserRole(username, Integer.valueOf(newRole));
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usersGet")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = clientService.getAllUsers();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/userUpdate/{username}")
    public ResponseEntity<UserEntity> updateUserByUsername(@PathVariable String username, @RequestBody UserDTO userDTO) {
        Optional<UserEntity> optionalUser = clientService.getUserByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserEntity user = optionalUser.get();

        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getRoleId() != null) {
            user.setRoleId(userDTO.getRoleId());
        }

        UserEntity updatedUser = clientService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

}
