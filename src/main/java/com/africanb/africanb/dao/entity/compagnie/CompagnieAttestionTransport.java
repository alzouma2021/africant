package com.africanb.africanb.dao.entity.compagnie;

import com.africanb.africanb.dao.entity.document.Document;
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
@Table(name = "compagnieattestiontransport")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompagnieAttestionTransport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @ManyToOne
    private CompagnieTransport compagnie;
    @ManyToOne
    private Document attestionTransport;

    @Column(name="is_deleted")
    private Boolean    isDeleted;
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
}
