package dev.Pedro.controle_gastos.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HexFormat;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long expiracaoMs = 3600000;

    // Gera o token a partir do email do usuário
    public String gerarToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiracaoMs))
                .signWith(getSigningKey())
                .compact();
    }

    // Lê o email de dentro do token
    public String extrairEmail(String token) {
        return getClaims(token).getSubject();
    }

    // Confere se o token é válido (assinatura correta e não expirado)
    public boolean tokenValido(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Extrai todas as informações (claims) de dentro do token, validando a assinatura
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Transforma a secretKey (hex) num objeto SecretKey que a biblioteca entende
    private SecretKey getSigningKey() {
        byte[] keyBytes = HexFormat.of().parseHex(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}