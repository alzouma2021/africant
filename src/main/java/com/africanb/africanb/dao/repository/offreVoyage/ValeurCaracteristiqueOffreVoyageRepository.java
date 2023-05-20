package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageBoolean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValeurCaracteristiqueOffreVoyageRepository extends JpaRepository<ValeurCaracteristiqueOffreVoyage,Long> {

    @Query("select vcovb from  ValeurCaracteristiqueOffreVoyage vcovb where vcovb.offreVoyage.designation = :offreVoyageDesignation and vcovb.isDeleted= :isDeleted")
    List<ValeurCaracteristiqueOffreVoyage> findAllByOffreVoyageDesignation(@Param("offreVoyageDesignation") String offreVoyageDesignation, @Param("isDeleted") Boolean isDeleted);

}
