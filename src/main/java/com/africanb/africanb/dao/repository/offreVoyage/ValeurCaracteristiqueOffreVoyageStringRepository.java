package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.JourSemaine;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageLong;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValeurCaracteristiqueOffreVoyageStringRepository extends JpaRepository<ValeurCaracteristiqueOffreVoyageString,Long> {

    @Query("select vcovs from  ValeurCaracteristiqueOffreVoyageString vcovs where vcovs.id = :id and vcovs.isDeleted= :isDeleted")
    ValeurCaracteristiqueOffreVoyageString findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select vcovs from ValeurCaracteristiqueOffreVoyageString vcovs where vcovs.designation = :designation and vcovs.isDeleted= :isDeleted")
    ValeurCaracteristiqueOffreVoyageString findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select vcovs from ValeurCaracteristiqueOffreVoyageString vcovs where vcovs.designation = :designation and vcovs.offreVoyage.designation = :offreVoyageDesignation  and vcovs.isDeleted= :isDeleted")
    ValeurCaracteristiqueOffreVoyageString findByDesignationByOffreVoyageDesignation(@Param("designation") String designation, @Param("offreVoyageDesignation") String offreVoyageDesignation, @Param("isDeleted") Boolean isDeleted);

}
