package com.africanb.africanb.dao.repository.compagnie;

import com.africanb.africanb.dao.entity.compagnie.Bagage;
import com.africanb.africanb.dao.entity.compagnie.Gare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GareRepository extends JpaRepository<Gare,Long> {
    @Query("select g from Gare g where g.id = :id and g.isDeleted= :isDeleted")
    Gare findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select g from Gare g where g.designation = :designation and g.isDeleted= :isDeleted")
    Gare findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select g from Gare g where g.compagnieTransport.raisonSociale = :compagnieTransportRaisonSociale and g.isDeleted= :isDeleted")
    List<Gare> findByCompagnieTransportRaisonSociale(@Param("compagnieTransportRaisonSociale") String compagnieTransportRaisonSociale, @Param("isDeleted") Boolean isDeleted);

}
