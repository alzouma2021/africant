package com.africanb.africanb.dao.repository.security;


import com.africanb.africanb.dao.entity.security.Role;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("select e from Role e where e.id= :id and e.isDeleted= :isDeleted")
    Role findOne(@Param("id") Integer id, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Role e where e.code= :code and e.isDeleted= :isDeleted")
    Role findByCode(@Param("code") String code, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Role e where e.libelle= :libelle and e.isDeleted= :isDeleted")
    Role findByLibelle(@Param("libelle") String libelle, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Role e where e.isDeleted= :isDeleted")
    List<Role> findByIsDeleted(@Param("isDeleted") Boolean isDeleted);

    @Query("select e from Role e where e.updatedAt = :updatedAt and e.isDeleted = :isDeleted")
    List<Role> findByUpdatedAt(@Param("updatedAt")Date updatedAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Role e where e.updatedBy = :updatedBy and e.isDeleted = :isDeleted")
    List<Role> findByUpdatedBy(@Param("updatedBy")Long updatedBy, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Role e where e.createdAt = :createdAt and e.isDeleted = :isDeleted")
    List<Role> findByCreatedAt(@Param("createdAt")Date createdAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Role e where e.createdBy = :createdBy and e.isDeleted = :isDeleted")
    List<Role> findByCreatedBy(@Param("createdBy")Long createdBy, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Role e where e.deletedAt = :deletedAt and e.isDeleted = :isDeleted")
    List<Role> findByDeletedAt(@Param("deletedAt")Date deletedAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Role e where e.deletedBy = :deletedBy and e.isDeleted = :isDeleted")
    List<Role> findByDeletedBy(@Param("deletedBy")Long deletedBy, @Param("isDeleted")Boolean isDeleted);
}
