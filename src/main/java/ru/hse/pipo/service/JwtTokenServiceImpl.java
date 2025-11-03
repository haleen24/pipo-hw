package ru.hse.pipo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    private final Duration expirationTime;
    private final Key privateKey;
    private final Key publicKey;

    public JwtTokenServiceImpl(@Value("${jwt.expiration-time}") Duration expirationTime,
                               @Value("${jwt.key.public}") String publicKey,
                               @Value("${jwt.key.private}") String privateKey) {
        this.expirationTime = expirationTime;
        this.publicKey = loadPublicKey(publicKey);
        this.privateKey = loadPrivateKey(privateKey);
    }

    @Override
    public String generate(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime.toMillis()))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        validateToken(claims);
        return claims.getSubject();
    }


    public void validateToken(Claims claims) {
        if (claims.getExpiration().before(new Date())) {
            throw new JwtException("token expired");
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @SneakyThrows
    private PrivateKey loadPrivateKey(String key) {
        byte[] encoded = Base64.getDecoder().decode(clearKey(key));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    @SneakyThrows
    private PublicKey loadPublicKey(String key) {
        byte[] encoded = Base64.getDecoder().decode(clearKey(key));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }

    private String clearKey(String key) {
        return key
                .replaceAll("-----BEGIN (PRIVATE|PUBLIC) KEY-----", "")
                .replaceAll("-----END (PRIVATE|PUBLIC) KEY-----", "")
                .replaceAll("\\s+", "");
    }
}
