package com.africanb.africanb.dao.entity.compagnie.ModeAbonnement;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name="abonnementprelevement")
@DiscriminatorValue("prelevement")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AbonnementPrelevement extends ModeAbonnement implements Serializable {
	@Column(nullable = false)
	private long taux;
}
