package com.africanb.africanb.dao.entity.compagnie.ModePaiment;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.utils.Reference.Reference;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="modepaiement")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ModePaiement implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true , length = 50)
	@NotNull
	private String designation;
	private String description;

	@ManyToOne
	private CompagnieTransport compagnieTransport;
	@ManyToOne
	private Reference typeModePaiement;

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
