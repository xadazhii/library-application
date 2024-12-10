package com.example.server.services;

import com.example.server.entity.UserEntity;
import com.example.server.entity.RoleEntity;
import com.example.server.repositories.UserRepo;
import com.example.server.repositories.RoleRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ClientService {

    private PasswordEncoder passwordEncoder;
    private UserRepo userRepo;
    private RoleRepo roleRepo;

    public UserEntity addUser(UserEntity user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepo.save(user);
    }

    public boolean deleteUser(String username) {
        UserEntity user = userRepo.findByUsername(username).orElse(null);
        if (user != null) {
            userRepo.delete(user);
            return true;
        }
        return false;
    }

    public UserEntity updateUserRole(String username, Integer roleId) {
        UserEntity user = userRepo.findByUsername(username).orElse(null);
        if (user != null) {
            RoleEntity role = roleRepo.findById(roleId).orElse(null);
            if (role != null) {
                user.setRoleId(roleId);
                return userRepo.save(user);
            }
        }
        return null;
    }

    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    public UserEntity updateUser(UserEntity user) {
        if (userRepo.existsById(user.getId())) {
            return userRepo.save(user);
        }
        return null;
    }
    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}

