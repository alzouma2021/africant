package com.africanb.africanb.dao.entity.compagnie.ModeAbonnement;

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
@Table(name="abonnementperiodique")
@DiscriminatorValue("periodique")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AbonnementPeriodique extends ModeAbonnement implements Serializable {
	@Column(nullable = false)
	private long redevance ;
	private long redevancePublicite ;
}
