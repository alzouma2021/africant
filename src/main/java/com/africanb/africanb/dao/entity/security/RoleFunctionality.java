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
@Table(name = "role_functionalities")
public class RoleFunctionality implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "functionalities_id")
    private Functionality functionality;

    private Boolean isDeleted;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Long createdBy;
    private Long updatedBy;
    private Long deletedBy;
}
