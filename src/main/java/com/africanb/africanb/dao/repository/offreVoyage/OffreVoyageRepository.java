package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreVoyageRepository extends JpaRepository<OffreVoyage,Long> {
    @Query("select ov from  OffreVoyage ov where ov.id = :id and ov.isDeleted= :isDeleted")
    OffreVoyage findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select ov from OffreVoyage ov where ov.designation = :designation and ov.isDeleted= :isDeleted")
    OffreVoyage findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select ov from OffreVoyage ov where ov.compagnieTransport.raisonSociale = :raisonSocialeCompagnieTransport and ov.isDeleted= :isDeleted")
    List<OffreVoyage> getTravelOfferByCompagnieTransport(@Param("raisonSocialeCompagnieTransport") String raisonSocialeCompagnieTransport, @Param("isDeleted") Boolean isDeleted);

    @Query("select ov from OffreVoyage ov where ov.villeDepart.designation = :villeDepart and ov.villeDestination.designation = :villeDestination and ov.isDeleted= :isDeleted")
    List<OffreVoyage> getOffreVoyageByCriteria(@Param("villeDepart") String villeDepart, @Param("villeDestination") String villeDestination, @Param("isDeleted") Boolean isDeleted);
}
