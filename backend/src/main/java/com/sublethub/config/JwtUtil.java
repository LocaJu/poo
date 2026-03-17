package com.sublethub.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类，用于生成和解析 token
 */
@Component
public class JwtUtil {

  private final JwtProperties jwtProperties;
  private final SecretKey key;

  public JwtUtil(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(Long userId, String openId) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + jwtProperties.getExpirationMs());
    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim("openId", openId)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateAdminToken(Long adminId) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + jwtProperties.getExpirationMs());
    return Jwts.builder()
        .setSubject("admin:" + adminId)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = parseToken(token);
    if (claims == null) return null;
    String sub = claims.getSubject();
    if (sub != null && sub.startsWith("admin:")) return null;
    try {
      return Long.parseLong(sub);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public Long getAdminIdFromToken(String token) {
    Claims claims = parseToken(token);
    if (claims == null) return null;
    String sub = claims.getSubject();
    if (sub == null || !sub.startsWith("admin:")) return null;
    try {
      return Long.parseLong(sub.substring(6));
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public boolean isAdminToken(String token) {
    Claims claims = parseToken(token);
    return claims != null && claims.getSubject() != null && claims.getSubject().startsWith("admin:");
  }

  private Claims parseToken(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      return null;
    }
  }
}
