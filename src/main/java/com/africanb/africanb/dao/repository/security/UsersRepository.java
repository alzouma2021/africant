package com.africanb.africanb.dao.repository.security;


import com.africanb.africanb.dao.entity.security.Users;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Query("select e from Users e where e.id= :id and e.isDeleted= :isDeleted")
    Users findOne(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Users e where e.login= :login and e.isDeleted= :isDeleted")
    Users findByLogin(@Param("login")String login, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Users e where e.login= :login and e.password= :password and e.isDeleted= :isDeleted")
    Users findByLoginAndPassword(@Param("login")String login, @Param("password")String password, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Users e where e.matricule= :matricule and e.isDeleted= :isDeleted")
    Users findByMatricule(@Param("matricule") String matricule, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Users e where e.email= :email and e.isDeleted= :isDeleted")
    Users findByEmail(@Param("email") String email, @Param("isDeleted") Boolean isDeleted);

    @Query("select e from Users e where e.isDeleted= :isDeleted")
    List<Users> findByIsDeleted(@Param("isDeleted") Boolean isDeleted);

    @Query("select e from Users e where e.updatedAt = :updatedAt and e.isDeleted = :isDeleted")
    List<Users> findByUpdatedAt(@Param("updatedAt")Date updatedAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Users e where e.updatedBy = :updatedBy and e.isDeleted = :isDeleted")
    List<Users> findByUpdatedBy(@Param("updatedBy")Long updatedBy, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Users e where e.createdAt = :createdAt and e.isDeleted = :isDeleted")
    List<Users> findByCreatedAt(@Param("createdAt")Date createdAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Users e where e.createdBy = :createdBy and e.isDeleted = :isDeleted")
    List<Users> findByCreatedBy(@Param("createdBy")Long createdBy, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Users e where e.deletedAt = :deletedAt and e.isDeleted = :isDeleted")
    List<Users> findByDeletedAt(@Param("deletedAt")Date deletedAt, @Param("isDeleted")Boolean isDeleted);

    @Query("select e from Users e where e.deletedBy = :deletedBy and e.isDeleted = :isDeleted")
    List<Users> findByDeletedBy(@Param("deletedBy")Long deletedBy, @Param("isDeleted")Boolean isDeleted);

}
