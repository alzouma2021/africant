package com.africanb.africanb.dao.repository.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.reservationBilletVoyage.StatusUtilReservationBilletVoyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusUtilReservationBilletVoyageRepository extends JpaRepository<StatusUtilReservationBilletVoyage,Long> {

    @Query("select suct from  StatusUtilReservationBilletVoyage suct where suct.reservationBilletVoyage.designation = :reservationBilletVoyageDesignation and suct.statusUtil.designation= :statusUtilDesignation ")
    StatusUtilReservationBilletVoyage findByReservationBilletVoyageAndStatusUtil(@Param("reservationBilletVoyageDesignation") String reservationBilletVoyageDesignation ,@Param("statusUtilDesignation") String statusUtilDesignation);

    @Query("select suct from  StatusUtilReservationBilletVoyage suct where suct.id = :id ")
    StatusUtilReservationBilletVoyage findOne(@Param("id") Long id);

}
