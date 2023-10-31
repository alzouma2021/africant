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
public interface ReferenceTransformer {

    ReferenceTransformer INSTANCE = Mappers.getMapper(ReferenceTransformer.class);

    @FullTransformerQualifier
    @Mappings({

            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),

            @Mapping(source = "entity.referenceFamille.id", target = "referenceFamilleId"),
            @Mapping(source = "entity.referenceFamille.designation", target = "referenceFamilleDesignation"),

            @Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),

            @Mapping(source="entity.updatedBy", target="updatedBy"),
            @Mapping(source="entity.createdBy", target="createdBy"),
            @Mapping(source="entity.deletedBy", target="deletedBy"),
            @Mapping(source="entity.isDeleted", target="isDeleted"),

    })
    ReferenceDTO toDto(Reference entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ReferenceDTO> toDtos(List<Reference> entities) throws ParseException;

    public default ReferenceDTO toLiteDto(Reference entity) {
        if (entity == null) {
            return null;
        }
        ReferenceDTO dto = new ReferenceDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public default List<ReferenceDTO> toLiteDtos(List<Reference> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<ReferenceDTO> dtos = new ArrayList<ReferenceDTO>();
        for (Reference entity : entities) {
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
            @Mapping(source="dto.isDeleted", target="isDeleted"),

            @Mapping(source = "referenceFamille", target = "referenceFamille"),
    })
    Reference toEntity(ReferenceDTO dto, ReferenceFamille referenceFamille) throws ParseException;
}
