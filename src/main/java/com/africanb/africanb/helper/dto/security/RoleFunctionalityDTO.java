package com.africanb.africanb.helper.dto.security;

import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) @JsonPropertyOrder(alphabetic = true)
public class RoleFunctionalityDTO {

    private Integer id;

    private Integer roleId;
    private String roleLibelle;
    private String roleCode;
    private Integer functionalityId;
    private String functionalityLibelle;
    private String functionalityCode;

    private Boolean isDeleted;

    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    private Integer createdBy;
    private Integer updatedBy;
    private Integer deletedBy;

    /// SEARCH PARAM//

    private SearchParam<Integer> roleIdParam;
    private SearchParam<String>  roleLibelleParam;
    private SearchParam<String>  roleCodeParam;
    private SearchParam<Integer>  functionalityIdParam;
    private SearchParam<String>  functionalityLibelleParam;
    private SearchParam<String>  functionalityCodeParam;
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
