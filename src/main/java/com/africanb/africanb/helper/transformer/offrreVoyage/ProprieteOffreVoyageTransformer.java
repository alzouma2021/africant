package com.africanb.africanb.helper.transformer.offrreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.offreVoyage.ProprieteOffreVoyageDTO;
import com.africanb.africanb.utils.Reference.Reference;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ProprieteOffreVoyageTransformer {

    ProprieteOffreVoyageTransformer INSTANCE = Mappers.getMapper(ProprieteOffreVoyageTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.estObligatoire", target = "estObligatoire"),

            @Mapping(source = "entity.typeProprieteOffreVoyage.designation", target = "typeProprieteOffreVoyageDesignation"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    ProprieteOffreVoyageDTO toDto(ProprieteOffreVoyage entity) throws ParseException;;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ProprieteOffreVoyageDTO> toDtos(List<ProprieteOffreVoyage> entities) throws ParseException;

    default ProprieteOffreVoyageDTO toLiteDto(ProprieteOffreVoyage entity) {
        if (entity == null) {
            return null;
        }
        ProprieteOffreVoyageDTO dto = new ProprieteOffreVoyageDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setTypeProprieteOffreVoyageDesignation(entity.getTypeProprieteOffreVoyage().getDesignation());
        return dto;
    }

    default List<ProprieteOffreVoyageDTO> toLiteDtos(List<ProprieteOffreVoyage> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<ProprieteOffreVoyageDTO> dtos = new ArrayList<ProprieteOffreVoyageDTO>();
        for (ProprieteOffreVoyage entity : entities) {
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

            @Mapping(source="typeProprieteOffreVoyage", target="typeProprieteOffreVoyage")
    })
    ProprieteOffreVoyage toEntity(ProprieteOffreVoyageDTO dto, Reference typeProprieteOffreVoyage) throws ParseException;
}
