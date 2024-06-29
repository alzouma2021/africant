package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ValeurCaracteristiqueOffreVoyageStringRepository extends JpaRepository<ValeurCaracteristiqueOffreVoyageString,Long> {

    @Query("select vcovs from  ValeurCaracteristiqueOffreVoyageString vcovs where vcovs.id = :id and vcovs.isDeleted= :isDeleted")
    ValeurCaracteristiqueOffreVoyageString findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select vcovs from ValeurCaracteristiqueOffreVoyageString vcovs where vcovs.designation = :designation and vcovs.isDeleted= :isDeleted")
    ValeurCaracteristiqueOffreVoyageString findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

}
