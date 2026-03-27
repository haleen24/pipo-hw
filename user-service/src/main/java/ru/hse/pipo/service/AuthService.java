package ru.hse.pipo.service;

import org.springframework.security.core.userdetails.User;

public interface AuthService {
    String generateJwtToken(String username, String password);

    String registerUser(User user);
}
