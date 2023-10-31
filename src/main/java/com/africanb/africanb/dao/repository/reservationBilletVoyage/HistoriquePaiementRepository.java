package com.africanb.africanb.dao.repository.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.reservationBilletVoyage.HistoriquePaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface HistoriquePaiementRepository extends JpaRepository<HistoriquePaiement,Long> {

    @Query("select hp from HistoriquePaiement hp where hp.identifiantUnique = :identifiantUnique ")
    HistoriquePaiement findByIdentifiantUnique(@Param("identifiantUnique") String identifiantUnique);
    @Query("select hp from HistoriquePaiement hp where hp.reservationBilletVoyage.designation = :reservationBilletVoyageDesignation ")
    HistoriquePaiement findByReservationBilletVoyage(@Param("reservationBilletVoyageDesignation") String reservationBilletVoyageDesignation);

}
