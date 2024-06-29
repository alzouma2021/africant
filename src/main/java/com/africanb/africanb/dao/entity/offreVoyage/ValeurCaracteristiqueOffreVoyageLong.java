package com.africanb.africanb.dao.entity.offreVoyage;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name="valeurcaracteristiqueoffrevoyagelong")
@DiscriminatorValue("long")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ValeurCaracteristiqueOffreVoyageLong extends ValeurCaracteristiqueOffreVoyage implements Serializable {
	@Column(nullable = false)
	private Long valeur ;
}
