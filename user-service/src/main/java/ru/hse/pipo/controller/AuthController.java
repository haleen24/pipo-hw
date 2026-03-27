package ru.hse.pipo.controller;//package ru.hse.pipo.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.pipo.mapper.UserMapper;
import ru.hse.pipo.service.AuthService;
import ru.hse.user.controller.AuthApi;
import ru.hse.user.model.AuthResponse;
import ru.hse.user.model.LoginRequest;
import ru.hse.user.model.RegisterRequest;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private static final String ROLE_USER = "USER";
    private static final List<GrantedAuthority> DEFAULT_AUTHORITIES = List.of(new SimpleGrantedAuthority(ROLE_USER));

    private final AuthService authService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<AuthResponse> loginUser(LoginRequest loginRequest) {
        String jwtToken = authService.generateJwtToken(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new AuthResponse().token(jwtToken));
    }

    @Override
    public ResponseEntity<AuthResponse> registerUser(RegisterRequest registerRequest) {
        User user = userMapper.fromRegisterRequest(registerRequest, DEFAULT_AUTHORITIES);
        String jwtToken = authService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse().token(jwtToken));
    }
}
