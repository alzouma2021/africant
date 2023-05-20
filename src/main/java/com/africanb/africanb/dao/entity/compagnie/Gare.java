package com.africanb.africanb.dao.entity.compagnie;

import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
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
 */
@Entity
@Table(name = "gare")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Gare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true , length = 50)
    @NotNull
    private String designation;
    private String description;

    @NotNull
    private String email;
    @NotNull
    private String telephone1;
    @NotNull
    private String telephone2;
    @NotNull
    private String adresseLocalisation;

    @ManyToOne
    private CompagnieTransport compagnieTransport;  //FamilleReferenceMode

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
