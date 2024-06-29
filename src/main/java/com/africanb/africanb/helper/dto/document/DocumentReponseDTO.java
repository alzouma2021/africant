package com.africanb.africanb.helper.dto.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class DocumentReponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String linkedAttestionDocument;
}
