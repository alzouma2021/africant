package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.JourSemaine;
import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourSemaineRepository extends JpaRepository<JourSemaine,Long> {

    @Query("select js from  JourSemaine js where js.id = :id and js.isDeleted= :isDeleted")
    JourSemaine findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select js from JourSemaine js where js.designation = :designation and js.isDeleted= :isDeleted")
    JourSemaine findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select js from JourSemaine js where js.offreVoyage.designation = :offreVoyageDesignation and js.isDeleted= :isDeleted")
    List<JourSemaine> findAllByOffreVoyageDesignation(@Param("offreVoyageDesignation") String offreVoyageDesignation, @Param("isDeleted") Boolean isDeleted);
}
