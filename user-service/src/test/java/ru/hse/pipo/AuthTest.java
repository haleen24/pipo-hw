package ru.hse.pipo;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.hse.pipo.service.AuthService;
import ru.hse.user.model.AuthResponse;
import ru.hse.user.model.LoginRequest;
import ru.hse.user.model.RegisterRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthTest extends CommonTestConfiguration {
    final String REGISTER_URL = "/public/auth/register";
    final String LOGIN_URL = "/public/auth/login";

    @Autowired
    AuthService authService;

    @Test
    void registerUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword(generateString());
        registerRequest.setUsername(generateString());

        ResponseEntity<AuthResponse> response =
            restClient.post().uri(REGISTER_URL).body(registerRequest).retrieve().toEntity(AuthResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
    }

    @Test
    void loginUser() {
        User user = new User(generateString(), generateString(), List.of(new SimpleGrantedAuthority("USER")));
        authService.registerUser(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(user.getPassword());
        loginRequest.setUsername(user.getUsername());

        ResponseEntity<AuthResponse> response =
            restClient.post().uri(LOGIN_URL).body(loginRequest).retrieve().toEntity(AuthResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
    }
}
