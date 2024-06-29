package com.africanb.africanb.helper.transformer.offrreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageString;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageStringDTO;
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
public interface ValeurCaracteristiqueOffreVoyageStringTransformer {


    ValeurCaracteristiqueOffreVoyageStringTransformer INSTANCE = Mappers.getMapper(ValeurCaracteristiqueOffreVoyageStringTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.valeur", target = "valeur"),

            @Mapping(source = "entity.offreVoyage.designation", target = "offreVoyageDesignation"),
            @Mapping(source = "entity.proprieteOffreVoyage.designation", target = "proprieteOffreVoyageDesignation"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    ValeurCaracteristiqueOffreVoyageStringDTO toDto(ValeurCaracteristiqueOffreVoyageString entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ValeurCaracteristiqueOffreVoyageStringDTO> toDtos(List<ValeurCaracteristiqueOffreVoyageString> entities) throws ParseException;

    default ValeurCaracteristiqueOffreVoyageStringDTO toLiteDto(ValeurCaracteristiqueOffreVoyageString entity) {
        if (entity == null) {
            return null;
        }
        ValeurCaracteristiqueOffreVoyageStringDTO dto = new ValeurCaracteristiqueOffreVoyageStringDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setValeur(entity.getValeur());
        return dto;
    }

    default List<ValeurCaracteristiqueOffreVoyageStringDTO> toLiteDtos(List<ValeurCaracteristiqueOffreVoyageString> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<ValeurCaracteristiqueOffreVoyageStringDTO> dtos = new ArrayList<>();
        for (ValeurCaracteristiqueOffreVoyageString entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(source = "dto.valeur", target = "valeur"),

            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy", target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),

            @Mapping(source="offreVoyage", target="offreVoyage"),
            @Mapping(source="proprieteOffreVoyage", target="proprieteOffreVoyage")
    })
    ValeurCaracteristiqueOffreVoyageString toEntity(ValeurCaracteristiqueOffreVoyageStringDTO dto, OffreVoyage offreVoyage, ProprieteOffreVoyage proprieteOffreVoyage) throws ParseException;
}
