package com.africanb.africanb.dao.entity.offreVoyage;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Alzouma Moussa Mahamadou
 */
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="valeurcaracteristiqueoffrevoyage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ValeurCaracteristiqueOffreVoyage implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true , length = 50)
	@NotNull
	private String designation;
	private String description;
	@Transient
	private String valeurTexte; //Contient la valeur de la propriété.En fonction,du type de la propriété,elle sera convertie dans le bon type

	@ManyToOne
	private OffreVoyage offreVoyage;
	@ManyToOne
	private ProprieteOffreVoyage proprieteOffreVoyage;

	@Column(name="is_deleted")
	private Boolean    isDeleted ;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt ;
	@Column(name="updated_by")
	private Long  updatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	private Long  createdBy;
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;
	private Long  deletedBy;

}
