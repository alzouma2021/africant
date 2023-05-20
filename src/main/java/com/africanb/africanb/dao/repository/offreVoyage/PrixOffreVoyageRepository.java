package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrixOffreVoyageRepository extends JpaRepository<PrixOffreVoyage,Long> {
    @Query("select pov from  PrixOffreVoyage pov where pov.id = :id and pov.isDeleted= :isDeleted")
    PrixOffreVoyage findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select pov from PrixOffreVoyage pov where pov.designation = :designation and pov.isDeleted= :isDeleted")
    PrixOffreVoyage findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select pov from PrixOffreVoyage pov where pov.offreVoyage.designation = :offreVoyageDesignation and pov.isDeleted= :isDeleted")
    List<PrixOffreVoyage> findAllByOffreVoyageDesignation(@Param("offreVoyageDesignation") String offreVoyageDesignation, @Param("isDeleted") Boolean isDeleted);
}
