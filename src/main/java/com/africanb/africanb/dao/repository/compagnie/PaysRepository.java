package com.africanb.africanb.dao.repository.compagnie;

import com.africanb.africanb.dao.entity.compagnie.Pays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaysRepository extends JpaRepository<Pays,Long> {

    @Query("select p from  Pays p where p.id = :id and p.isDeleted= :isDeleted")
    Pays findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select p from Pays p where p.designation = :designation and p.isDeleted= :isDeleted")
    Pays findByDesignation(@Param("designation") String code, @Param("isDeleted") Boolean isDeleted);

    @Query("select p from Pays p where p.isDeleted= :isDeleted")
    List<Pays> getAllPays(@Param("isDeleted") Boolean isDeleted);

}
