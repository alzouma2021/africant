package com.africanb.africanb.helper.transformer.security;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.security.Role;
import com.africanb.africanb.dao.entity.security.Users;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.security.UsersDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UsersTransformer {

    UsersTransformer INSTANCE = Mappers.getMapper(UsersTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.nom", target = "nom"),
            @Mapping(source = "entity.prenoms", target = "prenoms"),
            @Mapping(source = "entity.email", target = "email"),
            @Mapping(source = "entity.telephone", target = "telephone"),
            @Mapping(source = "entity.numberOfConnections", target = "numberOfConnections"),
            @Mapping(source = "entity.password", ignore = true, target = "password"),
            @Mapping(source = "entity.login", target = "login"),
            @Mapping(source = "entity.isActif", target = "isActif"),
            @Mapping(source = "entity.isFirst", target = "isFirst"),
            @Mapping(source = "entity.role.code", target = "roleCode"),
            @Mapping(source = "entity.role.libelle", target = "roleLibelle"),
            @Mapping(source = "entity.compagnieTransport.id", target = "compagnieTransportId"),
            @Mapping(source = "entity.compagnieTransport.designation", target = "compagnieTransportDesignation"),
            @Mapping(source = "entity.compagnieTransport.raisonSociale", target = "compagnieTransportRaisonSociale"),
            @Mapping(source = "entity.gareDesignation", target = "gareDesignation"),
            @Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="updatedAt"),
            @Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="createdAt"),
            @Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="deletedAt"),
            @Mapping(source="entity.lastConnectionDate", dateFormat="dd/MM/yyyy HH:mm:ss",target="lastConnectionDate"),
            @Mapping(source="entity.updatedBy",target="updatedBy"),
            @Mapping(source="entity.createdBy", target="createdBy"),
            @Mapping(source="entity.deletedBy", target="deletedBy"),
            @Mapping(source="entity.isDeleted", target="isDeleted"),
    })
    UsersDTO toDto(Users entity);
    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<UsersDTO> toDtos(List<Users> entities) throws ParseException;

    public default UsersDTO toLiteDto(Users entity) {
        if (entity == null) {
            return null;
        }
        UsersDTO dto = new UsersDTO();
        dto.setId( entity.getId() );
        dto.setNom( entity.getNom() );
        dto.setPrenoms(entity.getPrenoms());
        dto.setEmail(entity.getEmail());
        dto.setTelephone(entity.getTelephone());
        dto.setLogin(entity.getLogin());
        dto.setRoleLibelle(entity.getRole().getLibelle());
        dto.setRoleCode(entity.getRole().getCode());
        return dto;
    }

    public default List<UsersDTO> toLiteDtos(List<Users> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<UsersDTO> dtos = new ArrayList<UsersDTO>();
        for (Users entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.nom", target = "nom"),
            @Mapping(source = "dto.prenoms", target = "prenoms"),
            @Mapping(source = "dto.email", target = "email"),
            @Mapping(source = "dto.telephone", target = "telephone"),
            @Mapping(source = "dto.password", ignore = true, target = "password"),
            @Mapping(source = "dto.login", target = "login"),
            @Mapping(source = "dto.numberOfConnections", target = "numberOfConnections"),
            @Mapping(source = "dto.isActif", target = "isActif"),
            @Mapping(source = "dto.isFirst", target = "isFirst"),
            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="deletedAt"),
            @Mapping(source="dto.lastConnectionDate", dateFormat="dd/MM/yyyy HH:mm:ss",target="lastConnectionDate"),
            @Mapping(source="dto.updatedBy",target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),
            @Mapping(source = "dto.gareDesignation", target = "gareDesignation"),
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "compagnieTransport", target = "compagnieTransport"),
    })
    Users toEntity(UsersDTO dto, Role role, CompagnieTransport compagnieTransport);
}
