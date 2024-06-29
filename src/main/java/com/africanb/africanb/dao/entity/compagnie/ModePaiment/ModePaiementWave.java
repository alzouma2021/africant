package com.africanb.africanb.dao.entity.compagnie.ModePaiment;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name="modepaiementwave")
@DiscriminatorValue("wave")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ModePaiementWave extends ModePaiement implements Serializable {
	@Column(nullable = false)
	private String telephoneWave;
}
