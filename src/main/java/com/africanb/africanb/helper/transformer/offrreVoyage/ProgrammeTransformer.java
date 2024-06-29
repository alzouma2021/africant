package com.africanb.africanb.helper.transformer.offrreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.JourSemaine;
import com.africanb.africanb.dao.entity.offreVoyage.Programme;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.offreVoyage.ProgrammeDTO;
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
public interface ProgrammeTransformer {

    ProgrammeTransformer INSTANCE = Mappers.getMapper(ProgrammeTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.nombrePlaceDisponible", target = "nombrePlaceDisponible"),
            @Mapping(source = "entity.dateDepart", dateFormat="dd/MM/yyyy",target = "dateDepart"),
            @Mapping(source = "entity.dateArrivee", dateFormat="dd/MM/yyyy",target = "dateArrivee"),
            @Mapping(source = "entity.heureDepart", target = "heureDepart"),
            @Mapping(source = "entity.heureArrivee",target = "heureArrivee"),

            @Mapping(source = "entity.jourSemaine.designation", target = "jourSemaineDesignation"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    ProgrammeDTO toDto(Programme entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ProgrammeDTO> toDtos(List<Programme> entities) throws ParseException;

    default ProgrammeDTO toLiteDto(Programme entity) {
        if (entity == null) {
            return null;
        }
        ProgrammeDTO dto = new ProgrammeDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setJourSemaineDesignation(entity.getJourSemaine().getDesignation());
        return dto;
    }

    default List<ProgrammeDTO> toLiteDtos(List<Programme> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<ProgrammeDTO> dtos = new ArrayList<>();
        for (Programme entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),

            @Mapping(source = "dto.nombrePlaceDisponible", target = "nombrePlaceDisponible"),
            @Mapping(source = "dto.dateDepart", dateFormat="dd/MM/yyyy",target = "dateDepart"),
            @Mapping(source = "dto.dateArrivee", dateFormat="dd/MM/yyyy",target = "dateArrivee"),
            @Mapping(source = "dto.heureDepart", target = "heureDepart"),
            @Mapping(source = "dto.heureArrivee",target = "heureArrivee"),

            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy", target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),

            @Mapping(source="jourSemaine", target="jourSemaine"),
    })
    Programme toEntity(ProgrammeDTO dto, JourSemaine jourSemaine) throws ParseException;
}
