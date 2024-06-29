package com.africanb.africanb.dao.entity.security;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import jakarta.persistence.Entity;
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
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false , length = 50)
    private String nom;
    @Column(nullable = false , length = 50)
    private String prenoms;
    private String matricule;
    @Column(nullable = false , length = 50)
    private String login;
    @Column(nullable = false , length = 100)
    private String password;
    @Column(nullable = false , length = 50)
    private String email;
    @Column(length = 20)
    private String telephone;
    private String gareDesignation;

    @ManyToOne
    private Role role;
    @ManyToOne(cascade = CascadeType.ALL)
    private CompagnieTransport compagnieTransport;

    private Boolean isDeleted;
    private Boolean isActif;
    private Boolean isFirst;
    private Long numberOfConnections;

    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Date lastConnectionDate;

    private Long createdBy;
    private Long updatedBy;
    private Long deletedBy;
}
