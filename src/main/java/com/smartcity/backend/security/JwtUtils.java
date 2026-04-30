package com.smartcity.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
	// Read secret and expiration from environment for production safety
	private final String jwtSecret = System.getenv().getOrDefault("JWT_SECRET", "mysmartcitysecretkeyformyprojectinsupportofsmartcity");
	private final int jwtExpirationMs = parseEnvInt("JWT_EXPIRATION_MS", 86400000);
	private final Key signingKey = buildSigningKey();

	private Key buildSigningKey() {
		try {
			return Keys.hmacShaKeyFor(jwtSecret.getBytes());
		} catch (Exception e) {
			System.err.println("[WARN] Invalid JWT secret length or format; falling back to generated key. Set JWT_SECRET env var to a secure 256+ bit key.");
			return Keys.secretKeyFor(SignatureAlgorithm.HS256);
		}
	}

	private int parseEnvInt(String name, int defaultValue) {
		String raw = System.getenv(name);
		if (raw == null || raw.trim().isEmpty()) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(raw.trim());
		} catch (NumberFormatException ex) {
			System.err.println("[WARN] Invalid " + name + " value; using default: " + defaultValue);
			return defaultValue;
		}
	}

	private Key key() {
		return signingKey;
	}

	public String generateJwtToken(String username) {
		return Jwts.builder()
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
			.signWith(key(), SignatureAlgorithm.HS256)
			.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}