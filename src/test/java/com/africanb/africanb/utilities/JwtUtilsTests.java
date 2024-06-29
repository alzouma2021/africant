package com.africanb.africanb.utilities;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.security.Role;
import com.africanb.africanb.dao.entity.security.Users;
import com.africanb.africanb.dao.repository.security.UsersRepository;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import com.africanb.africanb.utils.security.JwtUtils;
import com.africanb.africanb.utils.security.TokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Disabled
@SpringBootTest
public class JwtUtilsTests {

    private static String validToken;
    private static String expiredToken;
    private static String invalidToken;

    private static final String SECRET_PHRASE = "your_secret_phrase_here"; // Replace with your actual secret phrase

    private static JwtUtils jwtUtils;

    private static Users createUserForTesting() {
        Users user = new Users();
        user.setId(1L);
        user.setLogin("testUser");
        user.setEmail("testuser@example.com");
        Role role = new Role();
        role.setId(1L);
        role.setCode("USER");
        user.setRole(role);
        CompagnieTransport compagnieTransport = new CompagnieTransport();
        compagnieTransport.setId(1L);
        compagnieTransport.setRaisonSociale("TestCompagnie");
        compagnieTransport.setEmail("tesgt@gmail.com");
        user.setCompagnieTransport(compagnieTransport);
        user.setNom("TestUser");
        return user;
    }

    private static String generateValidToken() {
        Users user = createUserForTesting();
        return JwtUtils.generateToken(user);
    }

    private static String generateExpiredToken() {
        Users user = createUserForTesting();
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(JwtUtils.SESSION_TOKEN_FIELD_SECRET_PHRASE),
                SignatureAlgorithm.HS256.getJcaName());

        Claims claims = new DefaultClaims();
        claims.setSubject(user.getNom());
        claims.setExpiration(new Date(System.currentTimeMillis() - 1000)); // Expired one second ago

        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .signWith(hmacKey)
                .compact();

        return expiredToken;
    }

    @BeforeAll
    public static void setup() {
        validToken = generateValidToken();
        expiredToken = generateExpiredToken();
        invalidToken = generateValidToken().replace("A", "B");
        jwtUtils = new JwtUtils(mock(UsersRepository.class));
    }

    @Test
    public void testGenerateToken() {
        Users user = createUserForTesting();
        String token = JwtUtils.generateToken(user);
        assertNotNull(token);
    }

    @Test
    public void testValideToken_ValidToken() {
        String result = JwtUtils.valideToken(validToken);
        assertEquals(ProjectConstants.VERIFY_TOKEN_VALIDE, result);
    }

    @Test
    public void testValideToken_ExpiredToken() {
        String result = JwtUtils.valideToken(expiredToken);
        assertEquals(ProjectConstants.VERIFY_TOKEN_EXPIRE, result);
    }

    @Test
    public void testValideToken_InvalidToken() {
        String result = JwtUtils.valideToken(invalidToken);
        assertEquals(ProjectConstants.VERIFY_TOKEN_MAUVAIS, result);
    }

    @Test
    public void testDecodeAndValidateToken_ValidToken() {
        TokenData tokenDataInstance = JwtUtils.decodeAndValidateToken(validToken);
        assertNotNull(tokenDataInstance);
        assertNotNull(ProjectConstants.VERIFY_TOKEN_MAUVAIS, tokenDataInstance.getStatus());
        assertNotNull(tokenDataInstance.getClaims());
    }

    @Test
    public void testDecodeAndValidateToken_ExpiredToken() {
        TokenData tokenDataInstance = JwtUtils.decodeAndValidateToken(expiredToken);
        assertNotNull(tokenDataInstance);
        assertEquals(ProjectConstants.VERIFY_TOKEN_EXPIRE, tokenDataInstance.getStatus());
        assertNull(tokenDataInstance.getClaims());
    }

    @Test
    public void testDecodeAndValidateToken_InvalidToken() {
        TokenData tokenDataInstance = JwtUtils.decodeAndValidateToken(invalidToken);
        assertNotNull(tokenDataInstance);
        assertEquals(ProjectConstants.VERIFY_TOKEN_MAUVAIS, tokenDataInstance.getStatus());
        assertNull(tokenDataInstance.getClaims());
    }

    @Test
    public void testGeneratePassword() {
        String password = JwtUtils.generatePassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    public void testEncryptPassword() {
        String password = "password123";
        String hashedPassword = JwtUtils.encryptPassword(password);
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

}