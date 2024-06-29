package com.africanb.africanb.dao.repository.security;


import com.africanb.africanb.dao.entity.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("select e from Role e where e.id= :id and e.isDeleted= :isDeleted")
    Role findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Role e where e.code= :code and e.isDeleted= :isDeleted")
    Role findByCode(@Param("code") String code, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Role e where e.libelle= :libelle and e.isDeleted= :isDeleted")
    Role findByLibelle(@Param("libelle") String libelle, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Role e where e.isDeleted= :isDeleted")
    List<Role> findByIsDeleted(@Param("isDeleted") Boolean isDeleted);
}
