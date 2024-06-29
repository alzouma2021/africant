package com.africanb.africanb.dao.repository.compagnie;

import com.africanb.africanb.dao.entity.compagnie.StatusUtilCompagnieTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusUtilCompagnieTransportRepository extends JpaRepository<StatusUtilCompagnieTransport,Long> {

    @Query("select suct from  StatusUtilCompagnieTransport suct where suct.compagnieTransport.id = :compagnieTransportId and suct.statusUtil.id= :statusUtilId and suct.isDeleted= :isDeleted")
    StatusUtilCompagnieTransport findByCompagnieTransportAndStatusUtil(@Param("compagnieTransportId") Long compagnieTransportId ,@Param("statusUtilId") Long statusUtilId, @Param("isDeleted") Boolean isDeleted);

    @Query("select suct from  StatusUtilCompagnieTransport suct where suct.id = :id and suct.isDeleted= :isDeleted")
    StatusUtilCompagnieTransport findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

}
