package com.africanb.africanb.utils.Reference;

import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

/**
 * @author Alzouma Moussa Mahamadou
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class RechercherReferenceDTO {
    private String referenceFamilleDesignation;

}
