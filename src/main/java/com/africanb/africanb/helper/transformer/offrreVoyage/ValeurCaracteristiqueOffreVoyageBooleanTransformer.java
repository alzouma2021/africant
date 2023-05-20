package com.africanb.africanb.helper.transformer.offrreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageBoolean;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageBooleanDTO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ValeurCaracteristiqueOffreVoyageBooleanTransformer {


    ValeurCaracteristiqueOffreVoyageBooleanTransformer INSTANCE = Mappers.getMapper(ValeurCaracteristiqueOffreVoyageBooleanTransformer.class);

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
    ValeurCaracteristiqueOffreVoyageBooleanDTO toDto(ValeurCaracteristiqueOffreVoyageBoolean entity) throws ParseException;;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ValeurCaracteristiqueOffreVoyageBooleanDTO> toDtos(List<ValeurCaracteristiqueOffreVoyageBoolean> entities) throws ParseException;

    default ValeurCaracteristiqueOffreVoyageBooleanDTO toLiteDto(ValeurCaracteristiqueOffreVoyageBoolean entity) {
        if (entity == null) {
            return null;
        }
        ValeurCaracteristiqueOffreVoyageBooleanDTO dto = new ValeurCaracteristiqueOffreVoyageBooleanDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setValeur(entity.getValeur());
        //dto.setJourSemaineDesignation(entity.getJourSemaine().getDesignation());
        return dto;
    }

    default List<ValeurCaracteristiqueOffreVoyageBooleanDTO> toLiteDtos(List<ValeurCaracteristiqueOffreVoyageBoolean> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<ValeurCaracteristiqueOffreVoyageBooleanDTO> dtos = new ArrayList<ValeurCaracteristiqueOffreVoyageBooleanDTO>();
        for (ValeurCaracteristiqueOffreVoyageBoolean entity : entities) {
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
    ValeurCaracteristiqueOffreVoyageBoolean toEntity(ValeurCaracteristiqueOffreVoyageBooleanDTO dto, OffreVoyage offreVoyage, ProprieteOffreVoyage proprieteOffreVoyage) throws ParseException;
}
