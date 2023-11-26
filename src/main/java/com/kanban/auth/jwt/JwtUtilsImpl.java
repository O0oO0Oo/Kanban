package com.kanban.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtilsImpl implements JwtUtils{

    @Value("${token.access.key}")
    private String accessKey;

    @Value("${token.refresh.key}")
    private String refreshKey;

    @Value("${token.access.expiration.time}")
    private Long accessExpirationTime;

    @Value("${token.refresh.expiration.time}")
    private Long refreshExpirationTime;

    private final String REFRESH = "Refresh";
    private final String ACCESS = "Access";

    private Map<String, String> keyMap;

    @PostConstruct
    private void init() {
        keyMap = new HashMap<>();
        keyMap.put(REFRESH, refreshKey);
        keyMap.put(ACCESS, accessKey);
    }

    @Override
    public String extractUserName(String token) {
        String secretKey = getSecretKey((token));

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        String secretKey = getSecretKey((token));

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        if (claims.get("authorities", String.class) != null) {
            String[] authorities = claims
                    .get("authorities", String.class)
                    .split(",");

            for (String authority : authorities) {
                grantedAuthorityList.add(new SimpleGrantedAuthority(authority));
            }
        }

        return grantedAuthorityList;
    }

    @Override
    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, accessKey, accessExpirationTime, ACCESS)
                .compact();
    }


    @Override
    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, refreshKey, refreshExpirationTime, REFRESH)
                .compact();
    }

    private JwtBuilder generateToken(Authentication authentication, String key, Long expirationTime, String type) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("type", type + " Token")
                .claim("authorities", authoritiesToString(authentication))
                .setIssuer("Kanban")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
                .signWith(SignatureAlgorithm.HS256, key);
    }

    /**
     *
     * @param authentication
     * @return ex: ROLE_{Team id}_{LEADER/MEMBER} Team 에 팀장, 멤버 권한인지
     */
    private String authoritiesToString(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(authority -> "ROLE_" + authority.getAuthority())
                .collect(Collectors.joining(","));
    }

    @Override
    public boolean isTokenValid(String token) {
        String secretKey = getSecretKey((token));
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return true;
    }

    /**
     * Refresh, Access 키 중 어떤것을 사용할지 Token 내용에 따라 리턴
     * @param token
     * @return
     */
    private String getSecretKey(String token) {
        String tokenType = getTokenType(token);
        return keyMap.get(tokenType);
    }

    /**
     * token 의 내용의 타입에 따라 리턴
     * @param token
     * @return
     */
    private String getTokenType(String token) {
        String payload = token.split("\\.")[1];
        String decodedJson = new String(Base64.getDecoder().decode(payload));

        int i = decodedJson.indexOf("\"type\":\"");

        String type = decodedJson.substring(i + 8, i + 15).strip();

        return type;
    }
}
