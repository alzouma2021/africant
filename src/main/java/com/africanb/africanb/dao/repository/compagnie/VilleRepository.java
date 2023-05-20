package com.africanb.africanb.dao.repository.compagnie;

import com.africanb.africanb.dao.entity.compagnie.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VilleRepository extends JpaRepository<Ville,Long> {

    @Query("select v from  Ville v where v.id = :id and v.isDeleted= :isDeleted")
    Ville findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select v from Ville v where v.designation = :designation and v.isDeleted= :isDeleted")
    Ville findByDesignation(@Param("designation") String code, @Param("isDeleted") Boolean isDeleted);

    @Query("select v from Ville v where v.isDeleted= :isDeleted")
    List<Ville> getAllCities(@Param("isDeleted") Boolean isDeleteds);

    @Query("select count(*) from Ville v where  v.isDeleted= :isDeleted")
    Long countAllCities(@Param("isDeleted") Boolean isDeleted);

}
