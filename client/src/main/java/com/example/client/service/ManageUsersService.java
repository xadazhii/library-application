package com.example.client.service;

import com.example.client.dto.UserDTO;
import com.example.client.util.HttpUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ManageUsersService {

    private static final String BASE_URL = "http://localhost:8080/client";

    public boolean onAddUserButtonClick(UserDTO user) {
        String url = BASE_URL + "/userAdd";

        try {
            HttpUtils.executeRequest(HttpMethod.POST, url, user, new ParameterizedTypeReference<Void>() {});
            return true;
        } catch (RuntimeException e) {
            System.err.println("Failed to add user: " + e.getMessage());
            return false;
        }
    }
    public boolean onDeleteUserButtonClick(String username, int userId) {
        if (userId == 1) {
            System.err.println("Cannot delete user with ID 1.");
            return false;
        }

        String url = BASE_URL + "/userDelete/" + username;

        try {
            HttpUtils.executeRequest(HttpMethod.DELETE, url, null, new ParameterizedTypeReference<Void>() {});
            return true;
        } catch (RuntimeException e) {
            System.err.println("Failed to delete user: " + e.getMessage());
            return false;
        }
    }

    public boolean onUpdateUserRoleButtonClick(UserDTO user, String newRole) {
        if (user.getId() == 1) {
            System.err.println("Cannot update role for user with ID 1.");
            return false;
        }

        String url = BASE_URL + "/userUpdateRole/" + user.getUsername() + "?newRole=" + newRole;

        try {
            HttpUtils.executeRequest(HttpMethod.PUT, url, null, new ParameterizedTypeReference<Void>() {});
            return true;
        } catch (RuntimeException e) {
            System.err.println("Failed to update user role: " + e.getMessage());
            return false;
        }
    }


    public List<UserDTO> onGetAllUsersButtonClick() {
        String url = BASE_URL + "/usersGet";

        try {
            String response = HttpUtils.executeRequest(HttpMethod.GET, url, null, new ParameterizedTypeReference<>() {});
            if (response == null || response.isEmpty()) {
                return Collections.emptyList();
            }
            return convertJsonToUsers(response);
        } catch (RuntimeException e) {
            System.err.println("Failed to fetch users: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<UserDTO> convertJsonToUsers(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            System.err.println("Failed to convert JSON: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
