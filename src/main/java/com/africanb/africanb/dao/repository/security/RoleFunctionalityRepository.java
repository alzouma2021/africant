package com.africanb.africanb.dao.repository.security;


import com.africanb.africanb.dao.entity.security.Functionality;
import com.africanb.africanb.dao.entity.security.RoleFunctionality;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

public interface RoleFunctionalityRepository extends JpaRepository<RoleFunctionality, Integer> {

    @Query("select e from RoleFunctionality e where e.id= :id and e.isDeleted= :isDeleted")
    RoleFunctionality findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.role.id= :roleId and e.functionality.id= :functionalityId and e.isDeleted= :isDeleted")
    RoleFunctionality findByRoleAndFunctionalityId(@Param("roleId") Integer roleId,@Param("functionalityId") Integer functionalityId, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.role.code= :roleCode and e.functionality.code= :functionalityCode and e.isDeleted= :isDeleted")
    RoleFunctionality findByRoleAndFunctionalityCode(@Param("roleCode") String roleCode,@Param("functionalityCode") String functionalityCode, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.role.id= :roleId and e.isDeleted= :isDeleted")
    List<RoleFunctionality> findByRoleId(@Param("roleId") Long roleId, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.functionality.id= :functionalityId and e.isDeleted= :isDeleted")
    List<RoleFunctionality> findByFunctionalityId(@Param("functionalityId") Long functionalityId, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.role.code= :roleCode and e.isDeleted= :isDeleted")
    List<RoleFunctionality> findByRoleCode(@Param("roleCode") String roleCode, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.functionality.code= :functionalityCode and e.isDeleted= :isDeleted")
    List<RoleFunctionality> findByFunctionalityCode(@Param("functionalityCode") String functionalityCode, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.isDeleted= :isDeleted")
    List<RoleFunctionality> findByIsDeleted(@Param("isDeleted") Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.updatedAt = :updatedAt and e.isDeleted = :isDeleted")
    List<RoleFunctionality> findByUpdatedAt(@Param("updatedAt") Date updatedAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.updatedBy = :updatedBy and e.isDeleted = :isDeleted")
    List<RoleFunctionality> findByUpdatedBy(@Param("updatedBy")Long updatedBy, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.createdAt = :createdAt and e.isDeleted = :isDeleted")
    List<RoleFunctionality> findByCreatedAt(@Param("createdAt")Date createdAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.createdBy = :createdBy and e.isDeleted = :isDeleted")
    List<RoleFunctionality> findByCreatedBy(@Param("createdBy")Long createdBy, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.deletedAt = :deletedAt and e.isDeleted = :isDeleted")
    List<RoleFunctionality> findByDeletedAt(@Param("deletedAt")Date deletedAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from RoleFunctionality e where e.deletedBy = :deletedBy and e.isDeleted = :isDeleted")
    List<RoleFunctionality> findByDeletedBy(@Param("deletedBy")Long deletedBy, @Param("isDeleted")Boolean isDeleted);

    @Query("select e.functionality from RoleFunctionality e where e.role.id= :roleId and e.isDeleted= :isDeleted")
    List<Functionality> findFunctionalityByRoleId(@Param("roleId") Long roleId, @Param("isDeleted") Boolean isDeleted);

    @Query("select e.functionality from RoleFunctionality e where e.role.code= :roleCode and e.isDeleted= :isDeleted")
    List<Functionality> findFunctionalityByRoleCode(@Param("roleCode") String roleCode, @Param("isDeleted") Boolean isDeleted);

     //criteria search

}
