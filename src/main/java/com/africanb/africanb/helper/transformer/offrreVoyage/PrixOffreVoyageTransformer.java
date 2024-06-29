package com.africanb.africanb.helper.transformer.offrreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.offreVoyage.PrixOffreVoyageDTO;
import com.africanb.africanb.utils.Reference.Reference;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper
public interface PrixOffreVoyageTransformer {

    PrixOffreVoyageTransformer INSTANCE = Mappers.getMapper(PrixOffreVoyageTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.prix", target = "prix"),

            @Mapping(source = "entity.mode.designation", target = "modeDesignation"),
            @Mapping(source = "entity.categorieVoyageur.designation", target = "categorieVoyageurDesignation"),
            @Mapping(source = "entity.offreVoyage.designation", target = "offreVoyageDesignation"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    PrixOffreVoyageDTO toDto(PrixOffreVoyage entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<PrixOffreVoyageDTO> toDtos(List<PrixOffreVoyage> entities) throws ParseException;

    default PrixOffreVoyageDTO toLiteDto(PrixOffreVoyage entity) {
        if (entity == null) {
            return null;
        }
        PrixOffreVoyageDTO dto = new PrixOffreVoyageDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setPrix(entity.getPrix());
        return dto;
    }

    default List<PrixOffreVoyageDTO> toLiteDtos(List<PrixOffreVoyage> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<PrixOffreVoyageDTO> dtos = new ArrayList<>();
        for (PrixOffreVoyage entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(source = "dto.prix", target = "prix"),

            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy", target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),

            @Mapping(source="mode", target="mode"),
            @Mapping(source="offreVoyage", target="offreVoyage"),
            @Mapping(source="categorieVoyageur", target="categorieVoyageur"),
    })
    PrixOffreVoyage toEntity(PrixOffreVoyageDTO dto, Reference mode, OffreVoyage offreVoyage, Reference categorieVoyageur) throws ParseException;
}
