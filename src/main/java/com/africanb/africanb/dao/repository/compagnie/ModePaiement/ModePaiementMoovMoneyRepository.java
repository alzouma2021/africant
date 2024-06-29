package com.africanb.africanb.dao.repository.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementMoovMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModePaiementMoovMoneyRepository extends JpaRepository<ModePaiementMoovMoney,Long> {

    @Query("select mpmom from  ModePaiementMoovMoney mpmom where mpmom.id = :id and mpmom.isDeleted= :isDeleted")
    ModePaiementMoovMoney findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select mpmom from ModePaiementMoovMoney mpmom where mpmom.designation = :designation and mpmom.isDeleted= :isDeleted")
    ModePaiementMoovMoney findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select mpmom from ModePaiementMoovMoney mpmom where  mpmom.compagnieTransport.raisonSociale = :compagnieTransportRaisonSociale  and mpmom.isDeleted= :isDeleted")
    ModePaiementMoovMoney findByCompagnieTransportRaisonSociale(@Param("compagnieTransportRaisonSociale") String compagnieTransportRaisonSociale, @Param("isDeleted") Boolean isDeleted);

}
