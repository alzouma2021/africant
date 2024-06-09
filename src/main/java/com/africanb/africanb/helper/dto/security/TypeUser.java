package com.africanb.africanb.helper.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeUser {
    ADMINISTRATEUR("test"),
    UTILISATEUR_GARE("test");
    String value;
}
