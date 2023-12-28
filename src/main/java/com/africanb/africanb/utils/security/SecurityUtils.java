package com.africanb.africanb.utils.security;


import com.africanb.africanb.dao.entity.security.Users;
import com.africanb.africanb.dao.repository.security.UsersRepository;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log
public class SecurityUtils {

    private static String	defaultTenant = "null";
    private static String defaultLanguage = "fr";
    public static final String SESSION_TOKEN_FIELD_SECRET_PHRASE = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
    private static final long TOKEN_EXPIRATION_MINUTES = 3000L;
    private final UsersRepository usersRepository;
    public SecurityUtils(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public static String generateToken(Users users){

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SESSION_TOKEN_FIELD_SECRET_PHRASE),
                SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();

        JwtBuilder jwtBuilder = Jwts.builder()
                .claim(ProjectConstants.SESSION_TOKEN_FILED_USER_ID, users.getId())
                .claim(ProjectConstants.SESSION_TOKEN_FILED_USER_LOGIN, users.getLogin())
                .claim(ProjectConstants.SESSION_TOKEN_FILED_USER_MAIL, users.getEmail()) // Correction ici
                .claim(ProjectConstants.SESSION_TOKEN_FILED_USER_ROLE, users.getRole() != null ? users.getRole().getId() : null)
                .claim(ProjectConstants.SESSION_TOKEN_FILED_USER_COMPAGNIE,
                        users.getCompagnieTransport() != null ? users.getCompagnieTransport().getRaisonSociale() : null)
                .setSubject(users.getNom())
                .setId(users.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES)));

        return jwtBuilder.signWith(hmacKey).compact();
    }

    public static String valideToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(new SecretKeySpec(Base64.getDecoder().decode(SESSION_TOKEN_FIELD_SECRET_PHRASE),
                            SignatureAlgorithm.HS256.getJcaName()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return ProjectConstants.VERIFY_TOKEN_VALIDE;
        }catch (ExpiredJwtException e) {
            return ProjectConstants.VERIFY_TOKEN_EXPIRE;
        }catch (SignatureException e) {
            return ProjectConstants.VERIFY_TOKEN_INVALIDE;
        }catch (MalformedJwtException e) {
            return ProjectConstants.VERIFY_TOKEN_MAUVAIS;
        }
    }

    private static Claims parseToken(String token) {
        try {
            JwtParser parser = createJwtParser();
            return parser.parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            return null;
        }
    }

    private static JwtParser createJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(new SecretKeySpec(Base64.getDecoder().decode(SESSION_TOKEN_FIELD_SECRET_PHRASE),
                        SignatureAlgorithm.HS256.getJcaName()))
                .build();
    }

    public static TokenData decodeAndValidateToken(String token){
        return  TokenData.builder()
                    .status(valideToken(token))
                    .claims(parseToken(token))
                .build();
    }

    public static String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 10;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            password.append(chars.charAt(randomIndex));
        }
        return password.toString();
    }

    public static String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashedPassword = no.toString(16);
            while (hashedPassword.length() < 32) {
                hashedPassword = "0" + hashedPassword;
            }
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String extractToken(HttpServletRequest servletResquest){
        return  servletResquest != null
                ? Optional.ofNullable(servletResquest.getHeader(AUTHORIZATION))
                    .map(token -> token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : null)
                    .filter(token -> !token.isEmpty())
                    .orElse(null)
                : null;
    }

    public static void languageManager(HttpServletRequest req){
        String tenantValue = req.getHeader("tenantID");
        if (tenantValue != null) {
            req.setAttribute("CURRENT_TENANT_IDENTIFIER", tenantValue);
        } else {
            req.setAttribute("CURRENT_TENANT_IDENTIFIER", defaultTenant);
        }

        String langValue = req.getHeader("lang");
        if (langValue != null) {
            req.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", langValue);
        } else {
            req.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", defaultLanguage);
        }
    }

    @SneakyThrows
    public static boolean doesPathNotRequireAuthentication(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        String servletPath = servletRequest.getServletPath();
        String method = servletRequest.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method) || isSwaggerPath(servletPath) || isApiVersionPath(servletPath) || isLoginPath(servletPath)) {
            chain.doFilter(servletRequest, servletResponse);
            return true;
        }
        return false;
    }

    private static boolean isSwaggerPath(String servletPath) {
        return servletPath.contains("swagger");
    }

    private static boolean isApiVersionPath(String servletPath) {
        return servletPath.contains("/v2");
    }

    private static boolean isLoginPath(String servletPath) {
        return servletPath.contains("/users/login");
    }

}
