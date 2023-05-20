package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageBoolean;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageLong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ValeurCaracteristiqueOffreVoyageLongRepository extends JpaRepository<ValeurCaracteristiqueOffreVoyageLong,Long> {

    @Query("select vcovl from  ValeurCaracteristiqueOffreVoyageLong vcovl where vcovl.id = :id and vcovl.isDeleted= :isDeleted")
    ValeurCaracteristiqueOffreVoyageLong findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select vcovl from ValeurCaracteristiqueOffreVoyageLong vcovl where vcovl.designation = :designation and vcovl.isDeleted= :isDeleted")
    ValeurCaracteristiqueOffreVoyageLong findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);
    @Query("select vcovl from ValeurCaracteristiqueOffreVoyageLong vcovl where vcovl.designation = :designation and vcovl.offreVoyage.designation = :offreVoyageDesignation  and vcovl.isDeleted= :isDeleted")
    ValeurCaracteristiqueOffreVoyageLong findByDesignationByOffreVoyageDesignation(@Param("designation") String designation, @Param("offreVoyageDesignation") String offreVoyageDesignation,@Param("isDeleted") Boolean isDeleted);

}
