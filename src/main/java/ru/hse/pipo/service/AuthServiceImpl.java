package ru.hse.pipo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String generateJwtToken(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        UserDetails user = userDetailsManager.loadUserByUsername(username);
        return jwtTokenService.generate(user);
    }

    @Override
    public String registerUser(User user) {
        UserDetails userDetails = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .authorities(user.getAuthorities())
                .build();
        try {
            userDetailsManager.createUser(userDetails);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(CONFLICT.value()), e.getMessage());
        }
        UserDetails createdUser = userDetailsManager.loadUserByUsername(userDetails.getUsername());
        return jwtTokenService.generate(createdUser);
    }
}
