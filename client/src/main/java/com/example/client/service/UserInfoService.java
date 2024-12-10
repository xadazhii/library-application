package com.example.client.service;

import com.example.client.dto.UserDTO;
import com.example.client.util.HttpUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

public class UserInfoService {

    private static final String USER_INFO_URL = "http://localhost:8080/me/info";
    private static final String USER_UPDATE_URL = "http://localhost:8080/client";

    public UserDTO fetchUserInfo() {
        try {
            return HttpUtils.executeRequest(HttpMethod.GET, USER_INFO_URL, null, new ParameterizedTypeReference<>() {});
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to fetch user data: " + e.getMessage(), e);
        }
    }

    public boolean updateUser(UserDTO user,String oldname) {
        String url = USER_UPDATE_URL + "/userUpdate/" +oldname;

        try {
            HttpUtils.executeRequest(HttpMethod.PUT, url, user, new ParameterizedTypeReference<Void>() {});
            return true;
        } catch (RuntimeException e) {
            System.err.println("Failed to update user: " + e.getMessage());
            return false;
        }
    }
}
