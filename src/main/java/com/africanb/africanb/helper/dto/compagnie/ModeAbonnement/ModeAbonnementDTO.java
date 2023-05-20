package com.africanb.africanb.helper.dto.compagnie.ModeAbonnement;

import com.africanb.africanb.helper.searchFunctions.SearchParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Author Alzouma Moussa Mahamadou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModeAbonnementDTO {


	private Long id;
	private String designation;
	private String description;
	private String dateDebutAbonnement ;
	private String dateFinAbonnement ;
	private String compagnieTransportRaisonSociale;
	private String periodiciteAbonnementDesignation;
	private String typeModeAbonnementDesignation;

	//Si l'abonnement correspond à un mode d'abonnement periodique alors la propriété taux sera null ou 0
	//Si l'abonnement correspond à un mode d'abonnement prelevement alors les propriétés redevance et redevancePublicite seront null

	private long redevance;
	private long redevancePublicite;
	private long taux;

	private Boolean  isDeleted;
	private String updatedAt;
	private Long  updatedBy;
	private String createdAt;
	private Long  createdBy;
	private String deletedAt;
	private Long  deletedBy;

	private SearchParam<Boolean> isDeletedParam;
	private SearchParam<String>   designationParam;
	private SearchParam<String>   updatedAtParam;
	private SearchParam<Long>     updatedByParam;
	private SearchParam<String>   createdAtParam;
	private SearchParam<Long>     createdByParam;

	private String orderField;
	private String orderDirection;

}
