package com.africanb.africanb.helper.transformer.offrreVoyage;


import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.VilleEscale;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.offreVoyage.VilleEscaleDTO;
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
public interface VilleEscaleTransformer {

    VilleEscaleTransformer INSTANCE = Mappers.getMapper(VilleEscaleTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.offreVoyage.designation", target = "offreVoyageDesignation"),
            @Mapping(source = "entity.ville.designation", target = "villeDesignation"),
            @Mapping(source = "entity.position", target = "position"),

            @Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="entity.updatedBy", target="updatedBy"),
            @Mapping(source="entity.createdBy", target="createdBy"),
            @Mapping(source="entity.deletedBy", target="deletedBy"),
            @Mapping(source="entity.isDeleted", target="isDeleted"),
    })
    VilleEscaleDTO toDto(VilleEscale entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<VilleEscaleDTO> toDtos(List<VilleEscale> entities) throws ParseException;

    default VilleEscaleDTO toLiteDto(VilleEscale entity) {
        if (entity == null) {
            return null;
        }
        VilleEscaleDTO dto = new VilleEscaleDTO();
        dto.setId(entity.getId() );
        dto.setVilleDesignation(entity.getVille().getDesignation());
        dto.setOffreVoyageDesignation(entity.getOffreVoyage().getDesignation());
        return dto;
    }

    default List<VilleEscaleDTO> toLiteDtos(List<VilleEscale> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<VilleEscaleDTO> dtos = new ArrayList<>();
        for (VilleEscale entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.position", target = "position"),

            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy", target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),

            @Mapping(source = "offreVoyage", target = "offreVoyage"),
            @Mapping(source = "ville", target = "ville"),
    })
    VilleEscale toEntity(VilleEscaleDTO dto, OffreVoyage offreVoyage, Ville ville) throws ParseException;
}
