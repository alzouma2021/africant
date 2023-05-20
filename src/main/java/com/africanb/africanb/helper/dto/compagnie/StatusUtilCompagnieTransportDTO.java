package com.africanb.africanb.helper.dto.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @Author Alzouma Moussa Mahamadou
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class StatusUtilCompagnieTransportDTO  {

    private Long id ;

    private Boolean  isDeleted;
    private String updatedAt;
    private Long  updatedBy;
    private String createdAt;
    private Long  createdBy;
    private String deletedAt;
    private Long  deletedBy;

    private Long compagnieTransportId;
    private Long statusUtilId;

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
