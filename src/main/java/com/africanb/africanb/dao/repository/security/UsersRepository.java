package com.africanb.africanb.dao.repository.security;


import com.africanb.africanb.dao.entity.security.Users;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
