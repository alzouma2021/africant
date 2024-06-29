package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProprieteOffreVoyageRepository extends JpaRepository<ProprieteOffreVoyage,Long> {

    @Query("select pov from  ProprieteOffreVoyage pov where pov.id = :id and pov.isDeleted= :isDeleted")
    ProprieteOffreVoyage findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select pov from ProprieteOffreVoyage pov where pov.designation = :designation and pov.isDeleted= :isDeleted")
    ProprieteOffreVoyage findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select pov from ProprieteOffreVoyage pov where pov.isDeleted= :isDeleted")
    List<ProprieteOffreVoyage> findAll(@Param("isDeleted") Boolean isDeleted);

}
