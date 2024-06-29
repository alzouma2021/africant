package com.africanb.africanb.dao.entity.offreVoyage;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="valeurcaracteristiqueoffrevoyage")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ValeurCaracteristiqueOffreVoyage implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true , length = 50)
	@NotNull
	private String designation;
	private String description;
	@Transient
	private String valeurTexte;

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
