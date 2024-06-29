package com.africanb.africanb.dao.entity.offreVoyage;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name="valeurcaracteristiqueoffrevoyageboolean")
@DiscriminatorValue("boolean")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ValeurCaracteristiqueOffreVoyageBoolean extends ValeurCaracteristiqueOffreVoyage implements Serializable {
	@Column(nullable = false)
	private Boolean valeur ;
}
