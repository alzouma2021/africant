package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.Programme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgrammeRepository extends JpaRepository<Programme,Long> {
    @Query("select p from  Programme p where p.id = :id and p.isDeleted= :isDeleted")
    Programme findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select p from Programme p where p.designation = :designation and p.isDeleted= :isDeleted")
    Programme findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select p from Programme p where p.jourSemaine.designation = :jourSemaineDesignation and p.isDeleted= :isDeleted")
    List<Programme> findByJourSemaine(@Param("jourSemaineDesignation") String jourSemaineDesignation, @Param("isDeleted") Boolean isDeleted);
}
