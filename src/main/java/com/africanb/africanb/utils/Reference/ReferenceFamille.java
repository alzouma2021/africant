package com.africanb.africanb.utils.Reference;

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
@Table(name = "referencefamille")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReferenceFamille implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    @Column(unique = true , length = 50)
    @NotNull
    private String designation;
    private String description;
    @Column(name="is_deleted")
    private Boolean isDeleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt ;
    @Column(name="updated_by")
    private Long  updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private Long createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
    private Long deletedBy;
}
