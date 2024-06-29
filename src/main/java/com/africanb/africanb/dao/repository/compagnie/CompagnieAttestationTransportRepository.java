package com.africanb.africanb.dao.repository.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieAttestionTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CompagnieAttestationTransportRepository extends JpaRepository<CompagnieAttestionTransport,Long> {
    @Query("select cat from  CompagnieAttestionTransport cat where cat.id = :id and cat.isDeleted= :isDeleted")
    CompagnieAttestionTransport findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select cat from CompagnieAttestionTransport cat where cat.compagnie.raisonSociale = :raisonSociale and cat.isDeleted= :isDeleted")
    CompagnieAttestionTransport findByRaisonSociale(@Param("raisonSociale") String raisonSociale, @Param("isDeleted") Boolean isDeleted);
}
