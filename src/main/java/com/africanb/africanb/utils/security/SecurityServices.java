package com.africanb.africanb.utils.security;


import com.africanb.africanb.dao.entity.security.Users;
import com.africanb.africanb.dao.repository.security.UsersRepository;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log
public class SecurityServices {

    private static String	defaultTenant = "null";
    private static String defaultLanguage = "fr";

    @Autowired
    UsersRepository usersRepository;

    public static String generateToken(Users users){
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(ProjectConstants.SESSION_TOKEN_FIELD_SECRET_PHRASE),
                SignatureAlgorithm.HS256.getJcaName());
        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .claim(ProjectConstants.SESSION_TOKEN_FILED_USER_ID, users.getId() != null ? users.getId() : null)
                .claim(ProjectConstants.SESSION_TOKEN_FILED_USER_LOGIN, users.getLogin() != null ? users.getLogin() : null)
                .claim(ProjectConstants.SESSION_TOKEN_FILED_USER_LOGIN, users.getEmail() != null ? users.getEmail() : null)
                .claim(ProjectConstants.SESSION_TOKEN_FILED_USER_ROLE, users.getRole() != null ? users.getRole().getId() :  null)
                .setSubject(users.getNom())
                .setId(users.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(3000l, ChronoUnit.MINUTES)))
                .signWith(hmacKey)
                .compact();
        return jwtToken;
    }

    public static String valideToken(String token){
       try{
            Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(ProjectConstants.SESSION_TOKEN_FIELD_SECRET_PHRASE),
                    SignatureAlgorithm.HS256.getJcaName());
                    Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(token);
                    return ProjectConstants.VERIFY_TOKEN_VALIDE;
       }catch (ExpiredJwtException eje){
           return ProjectConstants.VERIFY_TOKEN_EXPIRE;
       }catch (SignatureException se){
           return ProjectConstants.VERIFY_TOKEN_INVALIDE;
       }catch (MalformedJwtException mje){
           return ProjectConstants.VERIFY_TOKEN_MAUVAIS;
       }
    }

    // Méthode pour générer automatiquement un mot de passe
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

    // Méthode pour crypter un mot de passe
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


    /*    public static String generateCode1(){
        String formatted = null;
        formatted = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        return formatted;
    }*/

    /*    public static String generateCode2(){
        String formatted = null;
        SecureRandom secureRandom = new SecureRandom();
        int num = secureRandom.nextInt(100000000);
        formatted = String.format("%05d", num);
        return formatted;
    }*/

    public  boolean authenticateUser(Jws<Claims> token) {
        Integer userId = Integer.valueOf(token.getBody().getId());
        Users user = usersRepository.findOne(userId,false);
        if(user == null){
            return false;
        }
        return true;
    }

    public static String extractToken(HttpServletRequest servletResquest){
        if(servletResquest==null) return null;
        String token = servletResquest.getHeader(AUTHORIZATION);
        if(token==null) return null;
        String rtn = token.substring(("Bearer ".length()));
        return rtn == null || rtn.isEmpty() ? null : rtn;
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

}
