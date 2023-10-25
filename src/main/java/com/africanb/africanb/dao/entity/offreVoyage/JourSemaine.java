package com.africanb.africanb.dao.entity.offreVoyage;

import com.africanb.africanb.utils.Reference.Reference;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Alzouma Moussa Mahamadou
 * Cette classe permet de définir les jours de la semaine pour lesquels l'offre de voyage est programmée
 */
@Entity
@Table(name = "joursemaine")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JourSemaine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true , length = 50)
    @NotNull
    private String designation;
    private String description;

    @ManyToOne
    private OffreVoyage offreVoyage;
    @ManyToOne
    private Reference jourSemaine;  //FamilleReferenceJourSemaine

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
