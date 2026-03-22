package ru.hse.pipo.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {
    String generate(UserDetails userDetails);

    String extractUsername(String token);
}
