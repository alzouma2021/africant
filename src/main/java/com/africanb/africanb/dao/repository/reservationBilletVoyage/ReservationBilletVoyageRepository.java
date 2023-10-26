package com.africanb.africanb.dao.repository.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.ReservationBilletVoyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationBilletVoyageRepository extends JpaRepository<ReservationBilletVoyage,Long> {

    @Query("select rb from  ReservationBilletVoyage rb where rb.id = :id and rb.isDeleted= :isDeleted")
    ReservationBilletVoyage findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select rb from ReservationBilletVoyage rb where rb.designation = :designation and rb.isDeleted= :isDeleted")
    ReservationBilletVoyage findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

}
