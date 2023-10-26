package com.africanb.africanb.dao.entity.offreVoyage;

import com.africanb.africanb.utils.Reference.Reference;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * @Authir Alzouma Moussa Mahamadou
 * Cette classe consiste à programme une offre de voyage
 */
@Entity
@Table(name = "programme")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Programme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true , length = 50)
    @NotNull
    private String designation;
    private String description;
    @ManyToOne
    JourSemaine jourSemaine;
    private Date dateDepart;
    private Date dateArrivee;
    private String HeureDepart;
    private String HeureArrivee;
    private Integer nombrePlaceDisponible; //Ce nombre de place sera impacté par le nombre de réservatioo

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
