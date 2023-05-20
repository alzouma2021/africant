package com.africanb.africanb.helper.dto.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.helper.searchFunctions.SearchParam;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author Alzouma Moussa Mahamadou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ValeurCaracteristiqueOffreVoyageDTO {


	private Long id;
	private String designation;
	private String description;
	private String valeurTexte;

	private String offreVoyageDesignation;
	private String proprieteOffreVoyageDesignation;
	private String typeProprieteOffreVoyageDesignation;

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
