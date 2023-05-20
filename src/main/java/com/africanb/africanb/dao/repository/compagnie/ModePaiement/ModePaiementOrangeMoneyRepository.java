package com.africanb.africanb.dao.repository.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementOrangeMoney;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementWave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModePaiementOrangeMoneyRepository extends JpaRepository<ModePaiementOrangeMoney,Long> {

    @Query("select mpom from  ModePaiementOrangeMoney mpom where mpom.id = :id and mpom.isDeleted= :isDeleted")
    ModePaiementOrangeMoney findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select mpom from ModePaiementOrangeMoney mpom where mpom.designation = :designation and mpom.isDeleted= :isDeleted")
    ModePaiementOrangeMoney findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select mpom from ModePaiementOrangeMoney mpom where  mpom.compagnieTransport.raisonSociale = :compagnieTransportRaisonSociale  and mpom.isDeleted= :isDeleted")
    ModePaiementOrangeMoney findByCompagnieTransportRaisonSociale(@Param("compagnieTransportRaisonSociale") String compagnieTransportRaisonSociale, @Param("isDeleted") Boolean isDeleted);

}
