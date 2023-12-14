package com.africanb.africanb.dao.entity.reservationBilletVoyage;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class ClientDetails {
    private String nom;
    private String prenoms;
    private String email;
    private String telephone;
    private String addresse;
}
