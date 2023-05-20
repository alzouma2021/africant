package com.africanb.africanb.dao.entity.offreVoyage;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.compagnie.Ville;
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
@Entity
@Table(name = "villeescale")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VilleEscale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    private Long position;
    @Column(name="is_deleted")
    private Boolean   isDeleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name="updated_by")
    private Long  updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private Long  createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
    private Long  deletedBy;
    @ManyToOne
    private OffreVoyage offreVoyage;
    @ManyToOne
    private Ville ville;
}
