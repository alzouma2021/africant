package com.africanb.africanb.helper.dto.security;

import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) @JsonPropertyOrder(alphabetic = true)
public class FunctionalityDTO {

    private Long id;
    private String code;
    private String libelle;

    private Boolean isDeleted;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    private Long createdBy;
    private Long updatedBy;
    private Long deletedBy;

    /// SEARCH PARAM//

    private SearchParam<String> codeParam;
    private SearchParam<String>   libelleParam;
    private SearchParam<Boolean> isDeletedParam;
    private SearchParam<String> createdAtParam;
    private SearchParam<String> updatedAtParam;
    private SearchParam<String> deletedAtParam;
    private SearchParam<Long> createdByParam;
    private SearchParam<Long> updatedByParam;
    private SearchParam<Long> deletedByParam;

    // order param
    private String orderField;
    private String orderDirection;
}
