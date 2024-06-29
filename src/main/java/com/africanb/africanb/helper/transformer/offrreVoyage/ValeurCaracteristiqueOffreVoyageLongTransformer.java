package com.africanb.africanb.helper.transformer.offrreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageLong;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageLongDTO;
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
public interface ValeurCaracteristiqueOffreVoyageLongTransformer {


    ValeurCaracteristiqueOffreVoyageLongTransformer INSTANCE = Mappers.getMapper(ValeurCaracteristiqueOffreVoyageLongTransformer.class);

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
    ValeurCaracteristiqueOffreVoyageLongDTO toDto(ValeurCaracteristiqueOffreVoyageLong entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ValeurCaracteristiqueOffreVoyageLongDTO> toDtos(List<ValeurCaracteristiqueOffreVoyageLong> entities) throws ParseException;

    default ValeurCaracteristiqueOffreVoyageLongDTO toLiteDto(ValeurCaracteristiqueOffreVoyageLong entity) {
        if (entity == null) {
            return null;
        }
        ValeurCaracteristiqueOffreVoyageLongDTO dto = new ValeurCaracteristiqueOffreVoyageLongDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setValeur(entity.getValeur());
        return dto;
    }

    default List<ValeurCaracteristiqueOffreVoyageLongDTO> toLiteDtos(List<ValeurCaracteristiqueOffreVoyageLong> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<ValeurCaracteristiqueOffreVoyageLongDTO> dtos = new ArrayList<>();
        for (ValeurCaracteristiqueOffreVoyageLong entity : entities) {
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
    ValeurCaracteristiqueOffreVoyageLong toEntity(ValeurCaracteristiqueOffreVoyageLongDTO dto, OffreVoyage offreVoyage, ProprieteOffreVoyage proprieteOffreVoyage) throws ParseException;
}
