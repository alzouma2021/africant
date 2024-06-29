package com.africanb.africanb.dao.entity.compagnie.ModeAbonnement;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name="abonnementperiodique")
@DiscriminatorValue("periodique")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AbonnementPeriodique extends ModeAbonnement implements Serializable {
	@Column(nullable = false)
	private long redevance ;
	private long redevancePublicite ;
}
