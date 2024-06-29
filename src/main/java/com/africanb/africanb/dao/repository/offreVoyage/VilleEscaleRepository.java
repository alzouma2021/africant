package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.VilleEscale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VilleEscaleRepository extends JpaRepository<VilleEscale,Long> {

    @Query("select ve from  VilleEscale ve where ve.offreVoyage.designation= :offreVoyageDesignation and ve.ville.designation= :villeDesignation and ve.isDeleted= :isDeleted")
    VilleEscale findByOffreVoyageAndVille(@Param("offreVoyageDesignation") String offreVoyageDesignation ,@Param("villeDesignation") String villeDesignation, @Param("isDeleted") Boolean isDeleted);

    @Query("select ve from  VilleEscale ve where ve.id = :id and ve.isDeleted= :isDeleted")
    VilleEscale findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select ve from  VilleEscale ve where ve.offreVoyage.designation = :offreVoyageDesignation and ve.isDeleted= :isDeleted")
    List<VilleEscale> getVilleByOffreVoyageDesignation(@Param("offreVoyageDesignation") String offreVoyageDesignation, @Param("isDeleted") Boolean isDeleted);

}
