package com.africanb.africanb.dao.repository.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementWave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModePaiementWaveRepository extends JpaRepository<ModePaiementWave,Long> {

    @Query("select mpw from  ModePaiementWave mpw where mpw.id = :id and mpw.isDeleted= :isDeleted")
    ModePaiementWave findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select mpw from ModePaiementWave mpw where mpw.designation = :designation and mpw.isDeleted= :isDeleted")
    ModePaiementWave findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select mpw from ModePaiementWave mpw where  mpw.compagnieTransport.raisonSociale = :compagnieTransportRaisonSociale  and mpw.isDeleted= :isDeleted")
    ModePaiementWave findByCompagnieTransportRaisonSociale(@Param("compagnieTransportRaisonSociale") String compagnieTransportRaisonSociale, @Param("isDeleted") Boolean isDeleted);

}
