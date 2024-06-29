package com.africanb.africanb.helper.dto.security;

import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class RoleFunctionalityDTO {
    private Long id;

    private Long roleId;
    private String roleLibelle;
    private String roleCode;
    private Long functionalityId;
    private String functionalityLibelle;
    private String functionalityCode;

    private Boolean isDeleted;

    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    private Long createdBy;
    private Long updatedBy;
    private Long deletedBy;

    private SearchParam<Long> roleIdParam;
    private SearchParam<String>  roleLibelleParam;
    private SearchParam<String>  roleCodeParam;
    private SearchParam<Long>  functionalityIdParam;
    private SearchParam<String>  functionalityLibelleParam;
    private SearchParam<String>  functionalityCodeParam;
    private SearchParam<Boolean> isDeletedParam;
    private SearchParam<String> createdAtParam;
    private SearchParam<String> updatedAtParam;
    private SearchParam<String> deletedAtParam;
    private SearchParam<Long> createdByParam;
    private SearchParam<Long> updatedByParam;
    private SearchParam<Long> deletedByParam;

    private String orderField;
    private String orderDirection;
}
