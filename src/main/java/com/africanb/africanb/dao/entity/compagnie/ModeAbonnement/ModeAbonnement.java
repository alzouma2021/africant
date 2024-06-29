package com.africanb.africanb.dao.entity.compagnie.ModeAbonnement;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.utils.Reference.Reference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="modeabonnement")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ModeAbonnement implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true , length = 50)

	private String designation;
	private String description;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDebutAbonnement ;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateFinAbonnement ;


	@ManyToOne
	private CompagnieTransport compagnieTransport;
	@ManyToOne
	private Reference periodiciteAbonnement;
	@ManyToOne
	private Reference typeModeAbonnement;

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
