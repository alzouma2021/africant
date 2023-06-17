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
@Table(name = "role_functionalities")
public class RoleFunctionality implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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
    private Integer createdBy;
    private Integer updatedBy;
    private Integer deletedBy;
}
