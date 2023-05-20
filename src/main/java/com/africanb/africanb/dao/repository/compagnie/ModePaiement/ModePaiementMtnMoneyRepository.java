package com.africanb.africanb.dao.repository.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementMtnMoney;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementOrangeMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModePaiementMtnMoneyRepository extends JpaRepository<ModePaiementMtnMoney,Long> {

    @Query("select mpmm from  ModePaiementMtnMoney mpmm where mpmm.id = :id and mpmm.isDeleted= :isDeleted")
    ModePaiementMtnMoney findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select mpmm from ModePaiementMtnMoney mpmm where mpmm.designation = :designation and mpmm.isDeleted= :isDeleted")
    ModePaiementMtnMoney findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select mpmm from ModePaiementMtnMoney mpmm where  mpmm.compagnieTransport.raisonSociale = :compagnieTransportRaisonSociale  and mpmm.isDeleted= :isDeleted")
    ModePaiementMtnMoney findByCompagnieTransportRaisonSociale(@Param("compagnieTransportRaisonSociale") String compagnieTransportRaisonSociale, @Param("isDeleted") Boolean isDeleted);
}
