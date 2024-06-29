package com.africanb.africanb.dao.repository.compagnie;

import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.ModeAbonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeAbonnementRepository extends JpaRepository<ModeAbonnement,Long> {

    @Query("select ma from  ModeAbonnement ma where ma.compagnieTransport.raisonSociale = :compagnieTransportRaisonSociale and ma.isDeleted= :isDeleted")
    List<ModeAbonnement> findByCompagnieTransport(@Param("compagnieTransportRaisonSociale") String compagnieTransportRaisonSociale, @Param("isDeleted") Boolean isDeleted);

}
