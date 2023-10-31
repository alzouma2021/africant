package com.africanb.africanb.utils.Reference;


import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ReferenceFamilleTransformer {

    ReferenceFamilleTransformer INSTANCE = Mappers.getMapper(ReferenceFamilleTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),

            @Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="entity.updatedBy", target="updatedBy"),
            @Mapping(source="entity.createdBy", target="createdBy"),
            @Mapping(source="entity.deletedBy", target="deletedBy"),
    })
    ReferenceFamilleDTO toDto(ReferenceFamille entity) throws ParseException;
    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ReferenceFamilleDTO> toDtos(List<ReferenceFamille> entities) throws ParseException;

    public default ReferenceFamilleDTO toLiteDto(ReferenceFamille entity) {
        if (entity == null) {
            return null;
        }
        ReferenceFamilleDTO dto = new ReferenceFamilleDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public default List<ReferenceFamilleDTO> toLiteDtos(List<ReferenceFamille> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<ReferenceFamilleDTO> dtos = new ArrayList<ReferenceFamilleDTO>();
        for (ReferenceFamille entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),

            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy", target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
    })
    ReferenceFamille toEntity(ReferenceFamilleDTO dto) throws ParseException;
}
