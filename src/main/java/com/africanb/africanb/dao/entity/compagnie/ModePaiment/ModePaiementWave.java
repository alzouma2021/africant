package com.africanb.africanb.dao.entity.compagnie.ModePaiment;

import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.ModeAbonnement;
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
@Table(name="modepaiementwave")
@DiscriminatorValue("wave")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModePaiementWave extends ModePaiement implements Serializable {
	@Column(nullable = false)
	private String telephoneWave;
}
