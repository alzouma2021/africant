package com.africanb.africanb.helper.transformer.security;

import com.africanb.africanb.dao.entity.security.Functionality;
import com.africanb.africanb.dao.entity.security.Role;
import com.africanb.africanb.dao.entity.security.RoleFunctionality;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.security.RoleFunctionalityDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper
public interface RoleFunctionalityTransformer {

    RoleFunctionalityTransformer INSTANCE = Mappers.getMapper(RoleFunctionalityTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.role.id", target = "roleId"),
            @Mapping(source = "entity.role.libelle", target = "roleLibelle"),
            @Mapping(source = "entity.functionality.id", target = "functionalityId"),
            @Mapping(source = "entity.functionality.libelle", target = "functionalityLibelle"),
            @Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="entity.updatedBy",target="updatedBy"),
            @Mapping(source="entity.createdBy", target="createdBy"),
            @Mapping(source="entity.deletedBy", target="deletedBy"),
    })
    RoleFunctionalityDTO toDto(RoleFunctionality entity);

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<RoleFunctionalityDTO> toDtos(List<RoleFunctionality> entities) throws ParseException;

    default RoleFunctionalityDTO toLiteDto(RoleFunctionality entity) {
        if (entity == null) {
            return null;
        }
        RoleFunctionalityDTO dto = new RoleFunctionalityDTO();
        dto.setId( entity.getId() );
        dto.setRoleId( entity.getRole().getId() );
        dto.setRoleLibelle(entity.getRole().getLibelle());
        dto.setFunctionalityId(entity.getFunctionality().getId());
        dto.setFunctionalityLibelle(entity.getFunctionality().getLibelle());
        return dto;
    }

    default List<RoleFunctionalityDTO> toLiteDtos(List<RoleFunctionality> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<RoleFunctionalityDTO> dtos = new ArrayList<>();
        for (RoleFunctionality entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "functionality", target = "functionality"),
            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy",target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),
    })
    RoleFunctionality toEntity(RoleFunctionalityDTO dto, Functionality functionality, Role role);
}
