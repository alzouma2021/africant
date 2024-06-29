package com.africanb.africanb.dao.repository.security;


import com.africanb.africanb.dao.entity.security.Functionality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface FunctionalityRepository extends JpaRepository<Functionality, Integer> {

    @Query("select e from Functionality e where e.id= :id and e.isDeleted= :isDeleted")
    Functionality findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Functionality e where e.code= :code and e.isDeleted= :isDeleted")
    Functionality findByCode(@Param("code") String code, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Functionality e where e.libelle= :libelle and e.isDeleted= :isDeleted")
    Functionality findByLibelle(@Param("libelle") String libelle, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Functionality e where e.isDeleted= :isDeleted")
    List<Functionality> findByIsDeleted(@Param("isDeleted") Boolean isDeleted);

}
