package com.africanb.africanb.dao.repository.compagnie;

import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPeriodique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonnementPeriodiqueRepository extends JpaRepository<AbonnementPeriodique,Long> {

    @Query("select ap from  AbonnementPeriodique ap where ap.id = :id and ap.isDeleted= :isDeleted")
    AbonnementPeriodique findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select ap from AbonnementPeriodique ap where ap.designation = :designation and ap.isDeleted= :isDeleted")
    AbonnementPeriodique findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select ap from AbonnementPeriodique ap where  ap.compagnieTransport.raisonSociale = :compagnieTransportRaisonSociale  and ap.isDeleted= :isDeleted")
    AbonnementPeriodique findByCompagnieTransportRaisonSociale(@Param("compagnieTransportRaisonSociale") String compagnieTransportRaisonSociale, @Param("isDeleted") Boolean isDeleted);

}
