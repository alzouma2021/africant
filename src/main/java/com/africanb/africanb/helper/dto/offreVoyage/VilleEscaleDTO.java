package com.africanb.africanb.helper.dto.offreVoyage;

import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

/**
 * @Author Alzouma Moussa Mahamadou
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class VilleEscaleDTO {

    private Long id ;
    private String offreVoyageDesignation;
    private String villeDesignation;
    private Long position;
    private Boolean  isDeleted;
    private String updatedAt;
    private Long  updatedBy;
    private String createdAt;
    private Long  createdBy;
    private String deletedAt;
    private Long  deletedBy;

    private SearchParam<Boolean>  isDeletedParam;
    private SearchParam<String>   compagnieTransportDesignationParam;
    private SearchParam<String>   statusUtilDesignationParam;
    private SearchParam<String>   updatedAtParam;
    private SearchParam<Long>     updatedByParam;
    private SearchParam<String>   createdAtParam;
    private SearchParam<Long>     createdByParam;

    private String orderField;
    private String orderDirection;
}
