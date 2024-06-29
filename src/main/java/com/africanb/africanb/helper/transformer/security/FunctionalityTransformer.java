package com.africanb.africanb.helper.transformer.security;


import com.africanb.africanb.dao.entity.security.Functionality;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.security.FunctionalityDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper
public interface FunctionalityTransformer {

    FunctionalityTransformer INSTANCE = Mappers.getMapper(FunctionalityTransformer.class);

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
    FunctionalityDTO toDto(Functionality entity);

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<FunctionalityDTO> toDtos(List<Functionality> entities) throws ParseException;

    default FunctionalityDTO toLiteDto(Functionality entity) {
        if (entity == null) {
            return null;
        }
        FunctionalityDTO dto = new FunctionalityDTO();
        dto.setCode( entity.getCode() );
        dto.setLibelle(entity.getLibelle());
        dto.setId( entity.getId() );
        return dto;
    }

    default List<FunctionalityDTO> toLiteDtos(List<Functionality> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<FunctionalityDTO> dtos = new ArrayList<>();
        for (Functionality entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.code", target = "code"),
            @Mapping(source = "dto.libelle", target = "libelle"),
            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy HH:mm:ss",target="deletedAt"),
            @Mapping(source="dto.updatedBy",target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),
    })
    Functionality toEntity(FunctionalityDTO dto);
}
