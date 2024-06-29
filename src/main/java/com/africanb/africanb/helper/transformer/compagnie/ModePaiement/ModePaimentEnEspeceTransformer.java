package com.africanb.africanb.helper.transformer.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementEnEspece;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementEnEspeceDTO;
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
public interface ModePaimentEnEspeceTransformer {


    ModePaimentEnEspeceTransformer INSTANCE = Mappers.getMapper(ModePaimentEnEspeceTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),

            @Mapping(source = "entity.compagnieTransport.raisonSociale", target = "compagnieTransportRaisonSociale"),
            @Mapping(source = "entity.typeModePaiement.designation", target = "typeModePaiementDesignation"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    ModePaiementEnEspeceDTO toDto(ModePaiementEnEspece entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ModePaiementEnEspeceDTO> toDtos(List<ModePaiementEnEspece> entities) throws ParseException;

    default ModePaiementEnEspeceDTO toLiteDto(ModePaiementEnEspece entity) {
        if (entity == null) {
            return null;
        }
        ModePaiementEnEspeceDTO dto = new ModePaiementEnEspeceDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        return dto;
    }

    default List<ModePaiementEnEspeceDTO> toLiteDtos(List<ModePaiementEnEspece> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<ModePaiementEnEspeceDTO> dtos = new ArrayList<>();
        for (ModePaiementEnEspece entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),

            @Mapping(source = "dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "dto.updatedBy", target="updatedBy"),
            @Mapping(source = "dto.createdBy", target="createdBy"),
            @Mapping(source = "dto.deletedBy", target="deletedBy"),
            @Mapping(source = "dto.isDeleted", target="isDeleted"),

            @Mapping(source = "compagnieTransport", target="compagnieTransport"),
            @Mapping(source = "typeModePaiement", target="typeModePaiement"),
    })
    ModePaiementEnEspece toEntity(ModePaiementEnEspeceDTO dto, CompagnieTransport compagnieTransport, Reference typeModePaiement) throws ParseException;
}
