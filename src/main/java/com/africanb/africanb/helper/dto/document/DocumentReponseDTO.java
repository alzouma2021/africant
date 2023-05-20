package com.africanb.africanb.helper.dto.document;

import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Alzouma Moussa Mahamadou
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class DocumentReponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String linkedAttestionDocument;
}
