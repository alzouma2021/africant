package com.africanb.africanb.dao.repository.security;


import com.africanb.africanb.dao.entity.security.Functionality;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

public interface FunctionalityRepository extends JpaRepository<Functionality, Integer> {

    @Query("select e from Functionality e where e.id= :id and e.isDeleted= :isDeleted")
    Functionality findOne(@Param("id") Integer id, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Functionality e where e.code= :code and e.isDeleted= :isDeleted")
    Functionality findByCode(@Param("code") String code, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Functionality e where e.libelle= :libelle and e.isDeleted= :isDeleted")
    Functionality findByLibelle(@Param("libelle") String libelle, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Functionality e where e.isDeleted= :isDeleted")
    List<Functionality> findByIsDeleted(@Param("isDeleted") Boolean isDeleted);

    @Query("select e from Functionality e where e.updatedAt = :updatedAt and e.isDeleted = :isDeleted")
    List<Functionality> findByUpdatedAt(@Param("updatedAt") Date updatedAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Functionality e where e.updatedBy = :updatedBy and e.isDeleted = :isDeleted")
    List<Functionality> findByUpdatedBy(@Param("updatedBy")Long updatedBy, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Functionality e where e.createdAt = :createdAt and e.isDeleted = :isDeleted")
    List<Functionality> findByCreatedAt(@Param("createdAt")Date createdAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Functionality e where e.createdBy = :createdBy and e.isDeleted = :isDeleted")
    List<Functionality> findByCreatedBy(@Param("createdBy")Long createdBy, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Functionality e where e.deletedAt = :deletedAt and e.isDeleted = :isDeleted")
    List<Functionality> findByDeletedAt(@Param("deletedAt")Date deletedAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Functionality e where e.deletedBy = :deletedBy and e.isDeleted = :isDeleted")
    List<Functionality> findByDeletedBy(@Param("deletedBy")Long deletedBy, @Param("isDeleted")Boolean isDeleted);
}
