package com.africanb.africanb.dao.entity.reservationBilletVoyage;

import lombok.*;

import jakarta.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ClientDetails {
    private String nom;
    private String prenoms;
    private String email;
    private String telephone;
    private String addresse;
}
