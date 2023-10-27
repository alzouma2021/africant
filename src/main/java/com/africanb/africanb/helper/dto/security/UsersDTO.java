
package com.africanb.africanb.helper.dto.security;


import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString @JsonInclude(JsonInclude.Include.NON_NULL) @JsonPropertyOrder(alphabetic = true)
public class UsersDTO {

    private Long id;

    private String nom;
    private String prenoms;
    private String login;
    private String password;
    private String email;
    private String telephone;

    //role information
    private Long roleId;
    private String  roleCode;
    private String  roleLibelle;

    private Long numberOfConnections; //Permet de compter le nombre de fois qu'un utilisateur s'est authentifié

    private Boolean isDeleted;
    private String  createdAt;
    private String  updatedAt;
    private String  deletedAt;

    private Long createdBy;
    private Long updatedBy;
    private Long deletedBy;
    private String  token;
    private Long  compagnieTransportId;
    private String compagnieTransportDesignation;
    private String compagnieTransportRaisonSociale;
    private String gareDesignation;

    private Boolean isActif;
    private Boolean isFirst;
    private String lastConnectionDate;
    private String lastConnection;


    /// SEARCH PARAM//
    private SearchParam<Integer> idParam;
    private SearchParam<String> matriculeParam;
    private SearchParam<String> loginParam;
    private SearchParam<String> codeParam;
    private SearchParam<String>  libelleParam;
    private SearchParam<String> roleCodeParam;
    private SearchParam<String> roleLibelleParam;
    private SearchParam<Boolean> isDeletedParam;
    private SearchParam<String> createdAtParam;
    private SearchParam<String> updatedAtParam;
    private SearchParam<String> deletedAtParam;
    private SearchParam<Integer> createdByParam;
    private SearchParam<Integer> updatedByParam;
    private SearchParam<Integer> deletedByParam;

    private SearchParam<String> compagnieTransportDesignationParam;
    private SearchParam<Integer> compagnieTransportRaisonSocialdParam;
    private SearchParam<Integer> isActifParam;
    private SearchParam<Boolean> isFirstParam;

    // order param
    private String orderField;
    private String orderDirection;
}
