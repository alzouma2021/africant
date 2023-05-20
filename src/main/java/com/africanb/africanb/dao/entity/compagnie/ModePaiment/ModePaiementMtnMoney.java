package com.africanb.africanb.dao.entity.compagnie.ModePaiment;

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
@Table(name="modepaiementmtnmoney")
@DiscriminatorValue("mtnmoney")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModePaiementMtnMoney extends ModePaiement implements Serializable {
	@Column(nullable = false)
	private String telephoneMtnMoney;
}
