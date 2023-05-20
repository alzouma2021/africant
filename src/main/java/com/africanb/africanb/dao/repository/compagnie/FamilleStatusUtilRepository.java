package com.africanb.africanb.dao.repository.compagnie;

import com.africanb.africanb.dao.entity.compagnie.FamilleStatusUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilleStatusUtilRepository extends JpaRepository<FamilleStatusUtil,Long> {

    @Query("select fsu from  FamilleStatusUtil fsu where fsu.id = :id and fsu.isDeleted= :isDeleted")
    FamilleStatusUtil findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select fsu from FamilleStatusUtil fsu where fsu.designation = :designation and fsu.isDeleted= :isDeleted")
    FamilleStatusUtil findByDesignation(@Param("designation") String code, @Param("isDeleted") Boolean isDeleted);
}
