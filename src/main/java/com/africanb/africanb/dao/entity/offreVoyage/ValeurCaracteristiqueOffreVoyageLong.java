package com.africanb.africanb.dao.entity.offreVoyage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author Alzouma Moussa Mahamadou
 */
@Entity
@Table(name="valeurcaracteristiqueoffrevoyagelong")
@DiscriminatorValue("long")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ValeurCaracteristiqueOffreVoyageLong extends ValeurCaracteristiqueOffreVoyage implements Serializable {
	@Column(nullable = false)
	private Long valeur ;
}
