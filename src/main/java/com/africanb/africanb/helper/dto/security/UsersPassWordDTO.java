
package com.africanb.africanb.helper.dto.security;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString @JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class UsersPassWordDTO {
    private String email;
    private String oldPassWord;
    private String newPassWord;
}
