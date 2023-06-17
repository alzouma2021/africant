package com.africanb.africanb.helper.dto.security;

import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class RoleDTO {

    private Integer id;
    private String code;
    private String libelle;

    private List<FunctionalityDTO> datasFunctionalities;

    private Boolean isDeleted;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    private Integer createdBy;
    private Integer updatedBy;
    private Integer deletedBy;

    /// SEARCH PARAM//

    private SearchParam<Integer> idParam;
    private SearchParam<String> codeParam;
    private SearchParam<String> libelleParam;
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
