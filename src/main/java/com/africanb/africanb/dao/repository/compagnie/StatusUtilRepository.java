package com.africanb.africanb.dao.repository.compagnie;

import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusUtilRepository extends JpaRepository<StatusUtil,Long> {

    @Query("select ss from  StatusUtil ss where ss.id = :id and ss.isDeleted= :isDeleted")
    StatusUtil findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select ss from StatusUtil ss where ss.designation = :designation and ss.isDeleted= :isDeleted")
    StatusUtil findByDesignation(@Param("designation") String code, @Param("isDeleted") Boolean isDeleted);

}
