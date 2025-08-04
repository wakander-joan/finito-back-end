package com.management.finito.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private static final String SECRET_KEY = "uma-chave-secreta-super-segura-de-pelo-menos-32-caracteres";

    public String gerarToken(UUID idPessoa, String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("id", idPessoa.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 dia
                .signWith(getChaveAssinatura())
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getChaveAssinatura())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extrairEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getChaveAssinatura())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private Key getChaveAssinatura() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
