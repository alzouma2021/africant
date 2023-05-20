package com.africanb.africanb.dao.repository.Reference;

import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import com.africanb.africanb.utils.Reference.Reference;
import com.africanb.africanb.utils.Reference.ReferenceFamille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceFamilleRepository extends JpaRepository<ReferenceFamille,Long> {
    @Query("select rf from  ReferenceFamille rf where rf.id = :id and rf.isDeleted= :isDeleted")
    ReferenceFamille findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select rf from ReferenceFamille rf where rf.designation = :designation and rf.isDeleted= :isDeleted")
    ReferenceFamille findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);
}
