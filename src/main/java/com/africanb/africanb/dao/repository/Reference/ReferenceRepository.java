package com.africanb.africanb.dao.repository.Reference;

import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import com.africanb.africanb.utils.Reference.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference,Long> {
    @Query("select r from  Reference r where r.id = :id and r.isDeleted= :isDeleted")
    Reference findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select r from Reference r where r.designation = :designation and r.isDeleted= :isDeleted")
    Reference findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select r from Reference r where r.referenceFamille.designation = :referenceFamilleDesignation and r.isDeleted= :isDeleted")
    List<Reference> findByReferenceFamilleDesignation(@Param("referenceFamilleDesignation") String referenceFamilleDesignation, @Param("isDeleted") Boolean isDeleted);

}
