package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageBoolean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ValeurCaracteristiqueOffreVoyageBooleanRepository extends JpaRepository<ValeurCaracteristiqueOffreVoyageBoolean,Long> {

    @Query("select vcovb from  ValeurCaracteristiqueOffreVoyageBoolean vcovb where vcovb.id = :id and vcovb.isDeleted= :isDeleted")
    ValeurCaracteristiqueOffreVoyageBoolean findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select vcovb from ValeurCaracteristiqueOffreVoyageBoolean vcovb where vcovb.designation = :designation and vcovb.isDeleted= :isDeleted")
    ValeurCaracteristiqueOffreVoyageBoolean findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select vcovb from ValeurCaracteristiqueOffreVoyageBoolean vcovb where vcovb.designation = :designation and vcovb.offreVoyage.designation = :offreVoyageDesignation  and vcovb.isDeleted= :isDeleted")
    ValeurCaracteristiqueOffreVoyageBoolean findByDesignationByOffreVoyageDesignation(@Param("designation") String designation, @Param("offreVoyageDesignation") String offreVoyageDesignation, @Param("isDeleted") Boolean isDeleted);

}
