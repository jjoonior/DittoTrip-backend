package site.dittotrip.ditto_trip.utils;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.*;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import site.dittotrip.ditto_trip.auth.domain.enums.TokenType;
import site.dittotrip.ditto_trip.exception.common.AccessTokenExpiredException;
import site.dittotrip.ditto_trip.exception.common.RefreshTokenExpiredException;

import java.security.Key;
import java.util.Collection;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

  @Value("${jwt.token.secret-key}")
  private String SECRET_KEY;

  @Value("${jwt.token.access-expire-time}")
  private long ACCESS_EXPIRE_TIME;

  @Value("${jwt.token.refresh-expire-time}")
  private long REFRESH_EXPIRE_TIME;

  private final RedisService redisService;

  public void saveRefreshToken(String userId, String refreshToken) {
    redisService.setex("refreshToken:" + userId, refreshToken, REFRESH_EXPIRE_TIME);
  }

  public String getRefreshToken(String userId) {
    return redisService.get("refreshToken:" + userId);
  }

  public void deleteRefreshToken(String userId) {
    redisService.delete("refreshToken:" + userId);
  }

  public Key getSigningKey() {
    byte[] keyBytes = SECRET_KEY.getBytes();
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(Authentication authentication, TokenType tokenType) {
    return generateToken(authentication.getName(), authentication.getAuthorities(), tokenType);
  }

  public String generateToken(String username, Collection<? extends GrantedAuthority> authorities, TokenType tokenType) {
    return Jwts.builder()
        .setSubject(username)
        .claim("type", tokenType.toString())
        .claim("role", authorities.stream().findFirst().get().toString())
        .setExpiration(getExpireDate(tokenType))
        .signWith(getSigningKey())
        .compact();
  }

  public String resolveToken(HttpServletRequest request) {
    return request.getHeader("Authorization");
  }

  public boolean validateToken(String token) {
    if (token == null) {
      return false;
    }

    try {
      return Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getExpiration()
          .after(new Date());
    } catch (ExpiredJwtException e) {
      Claims claims = e.getClaims();  // 만료된 토큰의 클레임을 가져옴
      TokenType tokenType = TokenType.valueOf(claims.get("type", String.class));

      if (tokenType == TokenType.ACCESS_TOKEN) {
        throw new AccessTokenExpiredException("만료된 엑세스 토큰");
      } else if (tokenType == TokenType.REFRESH_TOKEN) {
        throw new RefreshTokenExpiredException("만료된 리프레시 토큰");
      } else {
        throw new IllegalArgumentException("유효하지 않은 토큰 타입입니다.");
      }
    }
  }

  public String getUserId(String accessToken) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(accessToken)
        .getBody()
        .getSubject();
  }

  public String getRole(String accessToken) {
    return (String) Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(accessToken)
        .getBody()
        .get("role", String.class);

  }

  private Date getExpireDate(TokenType tokenType) {
    Date now = new Date();
    long EXPIRE_TIME = tokenType == TokenType.ACCESS_TOKEN ? ACCESS_EXPIRE_TIME : REFRESH_EXPIRE_TIME;
    return new Date(now.getTime() + EXPIRE_TIME);
  }
}