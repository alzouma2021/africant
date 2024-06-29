package com.africanb.africanb.dao.entity.security;

import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Role implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;
    private String libelle;

    private Boolean isDeleted;

    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    private Long createdBy;
    private Long updatedBy;
    private Long deletedBy;
}
