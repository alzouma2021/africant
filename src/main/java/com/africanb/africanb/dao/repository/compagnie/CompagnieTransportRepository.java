package com.africanb.africanb.dao.repository.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompagnieTransportRepository extends JpaRepository<CompagnieTransport,Long> {
    @Query("select ct from  CompagnieTransport ct where ct.id = :id and ct.isDeleted= :isDeleted")
    CompagnieTransport findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select ct from CompagnieTransport ct where ct.designation = :designation and ct.isDeleted= :isDeleted")
    CompagnieTransport findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select ct from CompagnieTransport ct where ct.raisonSociale = :raisonSociale and ct.isDeleted= :isDeleted")
    CompagnieTransport findByRaisonSociale(@Param("raisonSociale") String raisonSociale, @Param("isDeleted") Boolean isDeleted);

    @Query("select ct from CompagnieTransport ct where ct.statusUtilActual.designation = :designation and ct.isDeleted= :isDeleted")
    List<CompagnieTransport> getAllProcessingCompagnies(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted, Pageable pageable);

    @Query("select count(*) from CompagnieTransport ct where ct.statusUtilActual.designation = :designation and ct.isDeleted= :isDeleted")
    Long countAllProcessingCompagnies(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select ct from CompagnieTransport ct where ct.statusUtilActual.designation = :designation and ct.isDeleted= :isDeleted")
    List<CompagnieTransport> getAllValidedCompagnies(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted, Pageable pageable);

    @Query("select count(*) from CompagnieTransport ct where ct.statusUtilActual.designation = :designation and ct.isDeleted= :isDeleted")
    Long countAllValidedCompagnies(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);
}
