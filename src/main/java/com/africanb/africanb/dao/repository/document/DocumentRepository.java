package com.africanb.africanb.dao.repository.document;

import com.africanb.africanb.dao.entity.document.Document;
import com.africanb.africanb.utils.Reference.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    @Query("select d from  Document d where d.designation = :designation and d.isDeleted= :isDeleted")
    Document findByDesignation(@Param("designation") String designation, @Param("isDeleted") Boolean isDeleted);
}
