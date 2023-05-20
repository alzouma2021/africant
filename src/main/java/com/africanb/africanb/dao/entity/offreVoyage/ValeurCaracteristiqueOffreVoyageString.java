package com.africanb.africanb.dao.entity.offreVoyage;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Alzouma Moussa Mahamadou
 */
@Entity
@Table(name="valeurcaracteristiqueoffrevoyagestring")
@DiscriminatorValue("string")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ValeurCaracteristiqueOffreVoyageString extends ValeurCaracteristiqueOffreVoyage implements Serializable  {
	@Column(nullable = false)
	private String valeur ;
}
