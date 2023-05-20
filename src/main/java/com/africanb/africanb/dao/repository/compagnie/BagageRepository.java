package com.africanb.africanb.dao.repository.compagnie;

import com.africanb.africanb.dao.entity.compagnie.Bagage;
import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BagageRepository extends JpaRepository<Bagage,Long> {
    @Query("select b from Bagage b where b.id = :id and b.isDeleted= :isDeleted")
    Bagage findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select b from Bagage b where b.designation = :designation and b.isDeleted= :isDeleted")
    Bagage findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select b from Bagage b where b.compagnieTransport.raisonSociale = :compagnieTransportRaisonSociale and b.isDeleted= :isDeleted")
    List<Bagage> findByCompagnieTransportRaisonSociale(@Param("compagnieTransportRaisonSociale") String compagnieTransportRaisonSociale, @Param("isDeleted") Boolean isDeleted);

}
