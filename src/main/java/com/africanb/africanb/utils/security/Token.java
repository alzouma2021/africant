package com.africanb.africanb.utils.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Data;

/**
 * @author ALZOUMA MOUSSA MAHAMADOU
 */
@Data
public class Token {
    private String status;
    private Jws<Claims> jwt = null ;
}
