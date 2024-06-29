package com.africanb.africanb.helper.transformer.offrreVoyage;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
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
public interface OffreVoyageTransformer {

    OffreVoyageTransformer INSTANCE = Mappers.getMapper(OffreVoyageTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.isActif", target = "isActif"),

            @Mapping(source = "entity.villeDepart.designation", target = "villeDepartDesignation"),
            @Mapping(source = "entity.villeDestination.designation", target = "villeDestinationDesignation"),
            @Mapping(source = "entity.typeOffreVoyage.designation", target = "typeOffreVoyageDesignation"),
            @Mapping(source = "entity.compagnieTransport.raisonSociale", target = "compagnieTransportRaisonSociale"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    OffreVoyageDTO toDto(OffreVoyage entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<OffreVoyageDTO> toDtos(List<OffreVoyage> entities) throws ParseException;

    default OffreVoyageDTO toLiteDto(OffreVoyage entity) {
        if (entity == null) {
            return null;
        }
        OffreVoyageDTO dto = new OffreVoyageDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        return dto;
    }

    default List<OffreVoyageDTO> toLiteDtos(List<OffreVoyage> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<OffreVoyageDTO> dtos = new ArrayList<>();
        for (OffreVoyage entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(source = "dto.isActif", target = "isActif"),

            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy", target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),

            @Mapping(source="villeDepart", target="villeDepart"),
            @Mapping(source="villeDestination", target="villeDestination"),
            @Mapping(source="typeOffreVoyage", target="typeOffreVoyage"),
            @Mapping(source="compagnieTransport", target="compagnieTransport"),
    })
    OffreVoyage toEntity(OffreVoyageDTO dto, Ville villeDepart, Ville villeDestination, Reference typeOffreVoyage, CompagnieTransport compagnieTransport) throws ParseException;
}
