package com.africanb.africanb.helper.transformer.security;


import com.africanb.africanb.dao.entity.security.Role;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.security.RoleDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper
public interface RoleTransformer {

    RoleTransformer INSTANCE = Mappers.getMapper(RoleTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.code", target = "code"),
            @Mapping(source = "entity.libelle", target = "libelle"),
            @Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="updatedAt"),
            @Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="createdAt"),
            @Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="deletedAt"),
            @Mapping(source="entity.updatedBy",target="updatedBy"),
            @Mapping(source="entity.createdBy", target="createdBy"),
            @Mapping(source="entity.deletedBy", target="deletedBy"),
    })
    RoleDTO toDto(Role entity);
    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<RoleDTO> toDtos(List<Role> entities) throws ParseException;

    default RoleDTO toLiteDto(Role entity) {
        if (entity == null) {
            return null;
        }
        RoleDTO dto = new RoleDTO();
        dto.setId( entity.getId() );
        dto.setCode( entity.getCode() );
        dto.setLibelle(entity.getLibelle());
        return dto;
    }

    default List<RoleDTO> toLiteDtos(List<Role> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<RoleDTO> dtos = new ArrayList<>();
        for (Role entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @InheritInverseConfiguration
    Role toEntity(RoleDTO dto);
}
