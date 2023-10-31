package com.africanb.africanb.utils.emailService;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class EmailDTO {
    private String toAddress;
    private String subject;
    private String message;
    private Boolean isSent;
    private String attachment; //This property can be null
}
