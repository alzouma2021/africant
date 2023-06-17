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

    private Integer id;
    private String code;
    private String libelle;

    private Boolean isDeleted;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    private Integer createdBy;
    private Integer updatedBy;
    private Integer deletedBy;

    /// SEARCH PARAM//

    private SearchParam<String> codeParam;
    private SearchParam<String>   libelleParam;
    private SearchParam<Boolean> isDeletedParam;
    private SearchParam<String> createdAtParam;
    private SearchParam<String> updatedAtParam;
    private SearchParam<String> deletedAtParam;
    private SearchParam<Integer> createdByParam;
    private SearchParam<Integer> updatedByParam;
    private SearchParam<Integer> deletedByParam;

    // order param
    private String orderField;
    private String orderDirection;
}
