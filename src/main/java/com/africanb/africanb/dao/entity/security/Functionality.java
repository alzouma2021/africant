package com.africanb.africanb.dao.entity.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Functionality implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
