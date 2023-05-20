package com.africanb.africanb.dao.repository.compagnie;


import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPrelevement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonnementPrelevementRepository extends JpaRepository<AbonnementPrelevement,Long> {

    @Query("select ap from  AbonnementPrelevement ap where ap.id = :id and ap.isDeleted= :isDeleted")
    AbonnementPrelevement findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select ap from AbonnementPrelevement ap where ap.designation = :designation and ap.isDeleted= :isDeleted")
    AbonnementPrelevement findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);

    @Query("select ap from AbonnementPrelevement ap where  ap.compagnieTransport.raisonSociale = :compagnieTransportRaisonSociale  and ap.isDeleted= :isDeleted")
    AbonnementPrelevement findByCompagnieTransportRaisonSociale(@Param("compagnieTransportRaisonSociale") String compagnieTransportRaisonSociale, @Param("isDeleted") Boolean isDeleted);

}
