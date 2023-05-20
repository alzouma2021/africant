package com.africanb.africanb.helper.dto.compagnie;

import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class PaysDTO {

    private Long id ;
    private String designation;
    private String description;

    private Boolean isDeleted;
    private String updatedAt;
    private Long  updatedBy;
    private String createdAt;
    private Long  createdBy;
    private String deletedAt;
    private Long  deletedBy;


    private SearchParam<String> designationParam        ;
    private SearchParam<Boolean>  isDeletedParam        ;
    private SearchParam<String>   updatedAtParam        ;
    private SearchParam<Long>     updatedByParam        ;
    private SearchParam<String>   createdAtParam        ;
    private SearchParam<Long>     createdByParam        ;

    private String orderField;
    private String orderDirection;
}
