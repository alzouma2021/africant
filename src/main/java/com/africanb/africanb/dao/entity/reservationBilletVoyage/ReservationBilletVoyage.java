package com.africanb.africanb.dao.entity.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.compagnie.Gare;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.Programme;
import com.africanb.africanb.dao.entity.security.Users;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Alzouma Moussa Mahamadou
 */
@Entity
@Table(name = "reservationbilletvoyage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReservationBilletVoyage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @Column(unique = true , length = 50)
    @NotNull
    private String designation;
    private String description;

    @NotNull
    private Boolean isCanceled;
    private Date dateReservation;
    private Date dateEffectiveDepart;
    private Double montantTotalReservation;
    private Integer nombrePlace;
    private String raisonAnnulation;
    private Boolean isOtherPerson;

    @ManyToOne
    private Gare gare;
    @ManyToOne
    private OffreVoyage offreVoyage;
    @OneToOne
    private Programme programme;
    @ManyToOne
    private Users users;
    @ManyToOne
    private StatusUtil statusUtilActual;
    @Embedded
    private  ClientDetails clientDetails;

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
