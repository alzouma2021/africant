package com.africanb.africanb.dao.entity.offreVoyage;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name="valeurcaracteristiqueoffrevoyagestring")
@DiscriminatorValue("string")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ValeurCaracteristiqueOffreVoyageString extends ValeurCaracteristiqueOffreVoyage implements Serializable  {
	@Column(nullable = false)
	private String valeur ;
}
