package com.africanb.africanb.helper.dto.offreVoyage;

import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class OffreVoyageDTO {

    //Proprietes
    private Long id ;
    private String designation;
    private String description;
    private Boolean isActif;


    private Boolean isDeleted;
    private String updatedAt;
    private Long  updatedBy;
    private String createdAt;
    private Long  createdBy;
    private String deletedAt;
    private Long  deletedBy;

    //relationShip
    private String compagnieTransportRaisonSociale;
    private String typeOffreVoyageDesignation;
    private String villeDepartDesignation;
    private String villeDestinationDesignation;

    private List<PrixOffreVoyageDTO> prixOffreVoyageDTOList;
    private List<JourSemaineDTO> jourSemaineDTOList;
    private List<VilleEscaleDTO> villeEscaleDTOList;
    private List<ValeurCaracteristiqueOffreVoyageDTO> valeurCaracteristiqueOffreVoyageDTOList;

    // Search param
    private SearchParam<String> designationParam;
    private SearchParam<String>   compagnieTransportDesignationDesignationParam ;
    private SearchParam<String>   typeOffreVoyageDesignationParam;
    private SearchParam<String>   villeDepartDesignationParam;
    private SearchParam<String>   villeDstinationDesignationParam;
    private SearchParam<Boolean>  isDeletedParam;
    private SearchParam<String>   updatedAtParam        ;
    private SearchParam<Long>     updatedByParam        ;
    private SearchParam<String>   createdAtParam        ;
    private SearchParam<Long>     createdByParam        ;

    private String orderField;
    private String orderDirection;

}
