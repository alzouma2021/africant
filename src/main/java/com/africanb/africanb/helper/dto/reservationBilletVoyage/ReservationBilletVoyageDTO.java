package com.africanb.africanb.helper.dto.reservationBilletVoyage;


import com.africanb.africanb.dao.entity.reservationBilletVoyage.ClientDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class ReservationBilletVoyageDTO {

    private Long id ;
    private String designation;
    private String description;

    private Boolean isCanceled;
    private Date dateReservation;
    private Date dateEffectiveDepart;
    private Double montantTotalReservation;
    private Integer nombrePlace;
    private String raisonAnnulation;
    private ClientDetails clientDetails;
    private Boolean isOtherPerson;

    private String gareDesignation;
    private String offreVoyageDesignation;
    private String programmeDesignation;
    private String userEmail;
    private String categorieVoyageur;  // Enfant ou Adulte.
    private String statusActualDesignation;

    private Boolean isDeleted;
    private String updatedAt;
    private Long  updatedBy;
    private String createdAt;
    private Long  createdBy;
    private String deletedAt;
    private Long  deletedBy;

}
