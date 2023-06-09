package com.africanb.africanb.helper.dto.compagnie;

import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class GareDTO {

    private Long id ;
    private String designation;
    private String description;

    private String email;
    private String telephone1;
    private String telephone2;
    private String adresseLocalisation;
    private String compagnieTransportRaisonSociale;

    private Boolean isDeleted;
    private String updatedAt;
    private Long  updatedBy;
    private String createdAt;
    private Long  createdBy;
    private String deletedAt;
    private Long  deletedBy;


    // Search param
    private SearchParam<String> designationParam;
    private SearchParam<String>   modeDesignationParam ;
    private SearchParam<String>   categorieVoyageurDesignationParam;
    private SearchParam<String>   offreVoyageDesignationParam;
    private SearchParam<Boolean>  isDeletedParam;
    private SearchParam<String>   updatedAtParam        ;
    private SearchParam<Long>     updatedByParam        ;
    private SearchParam<String>   createdAtParam        ;
    private SearchParam<Long>     createdByParam        ;


    private String orderField;
    private String orderDirection;
}
