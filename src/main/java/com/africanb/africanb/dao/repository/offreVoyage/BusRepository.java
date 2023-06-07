package com.africanb.africanb.dao.repository.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus,Long> {

    @Query("select bus from  Bus bus where bus.id = :id and bus.isDeleted= :isDeleted")
    Bus findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select bus from Bus bus where bus.designation = :designation and bus.isDeleted= :isDeleted")
    Bus findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select bus from Bus bus where bus.numeroBus = :numeroBus and bus.isDeleted= :isDeleted")
    Bus findByNumeroBus(@Param("numeroBus") String numeroBus, @Param("isDeleted") Boolean isDeleted);

    @Query("select bus from Bus bus where bus.offreVoyage.designation = :offreVoyageDesignation and bus.isDeleted= :isDeleted")
    List<Bus> findByOffreVoyageDesignation(@Param("offreVoyageDesignation") String offreVoyageDesignation, @Param("isDeleted") Boolean isDeleted);

}
