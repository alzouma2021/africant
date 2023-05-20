package com.africanb.africanb.dao.repository.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementEnEspece;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementMoovMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModePaiementEnEspeceRepository extends JpaRepository<ModePaiementEnEspece,Long> {

    @Query("select mpee from  ModePaiementEnEspece mpee where mpee.id = :id and mpee.isDeleted= :isDeleted")
    ModePaiementEnEspece findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select mpee from ModePaiementEnEspece mpee where mpee.designation = :designation and mpee.isDeleted= :isDeleted")
    ModePaiementEnEspece findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select mpee from ModePaiementEnEspece mpee where  mpee.compagnieTransport.raisonSociale = :compagnieTransportRaisonSociale  and mpee.isDeleted= :isDeleted")
    ModePaiementEnEspece findByCompagnieTransportRaisonSociale(@Param("compagnieTransportRaisonSociale") String compagnieTransportRaisonSociale, @Param("isDeleted") Boolean isDeleted);

}
