package com.africanb.africanb.helper.transformer.compagnie;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.compagnie.CompagnieTransportDTO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CompagnieTransportTransformer {

    CompagnieTransportTransformer INSTANCE = Mappers.getMapper(CompagnieTransportTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.isActif", target = "isActif"),
            @Mapping(source = "entity.isValidate", target = "isValidate"),
            @Mapping(source = "entity.raisonSociale", target = "raisonSociale"),
            @Mapping(source = "entity.telephone", target = "telephone"),
            @Mapping(source = "entity.sigle", target = "sigle"),
            @Mapping(source = "entity.email", target = "email"),

            @Mapping(source = "entity.statusUtilActual.id", target = "statusUtilActualId"),
            @Mapping(source = "entity.statusUtilActual.designation", target = "statusUtilActualDesignation"),
            @Mapping(source = "entity.ville.id", target = "villeId"),
            @Mapping(source = "entity.ville.designation", target = "villeDesignation"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    CompagnieTransportDTO toDto(CompagnieTransport entity) throws ParseException;;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<CompagnieTransportDTO> toDtos(List<CompagnieTransport> entities) throws ParseException;

    default CompagnieTransportDTO toLiteDto(CompagnieTransport entity) {
        if (entity == null) {
            return null;
        }
        CompagnieTransportDTO dto = new CompagnieTransportDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setRaisonSociale(entity.getRaisonSociale());
        return dto;
    }

    default List<CompagnieTransportDTO> toLiteDtos(List<CompagnieTransport> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<CompagnieTransportDTO> dtos = new ArrayList<CompagnieTransportDTO>();
        for (CompagnieTransport entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(source = "dto.isActif", target = "isActif"),
            @Mapping(source = "dto.isValidate", target = "isValidate"),
            @Mapping(source = "dto.raisonSociale", target = "raisonSociale"),
            @Mapping(source = "dto.telephone", target = "telephone"),
            @Mapping(source = "dto.sigle", target = "sigle"),
            @Mapping(source = "dto.email", target = "email"),

            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy", target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),

            @Mapping(source="ville", target="ville"),
            @Mapping(source="statusUtilActual", target="statusUtilActual"),
    })
    CompagnieTransport toEntity(CompagnieTransportDTO dto, Ville ville, StatusUtil statusUtilActual) throws ParseException;
}
