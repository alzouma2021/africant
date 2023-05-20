package com.africanb.africanb.dao.repository.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.ModeAbonnement;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModePaiementRepository extends JpaRepository<ModePaiement,Long> {

    @Query("select mp from  ModePaiement mp where mp.compagnieTransport.raisonSociale = :compagnieTransportRaisonSociale and mp.isDeleted= :isDeleted")
    List<ModePaiement> findByCompagnieTransport(@Param("compagnieTransportRaisonSociale") String compagnieTransportRaisonSociale, @Param("isDeleted") Boolean isDeleted);

    @Query("select mp from  ModePaiement mp where mp.id.id = :id and mp.isDeleted= :isDeleted")
    ModePaiement findOne(@Param("id") long id, @Param("isDeleted") Boolean isDeleted);

}