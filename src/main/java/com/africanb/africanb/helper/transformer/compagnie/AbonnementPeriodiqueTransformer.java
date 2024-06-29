package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPeriodique;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPeriodiqueDTO;
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
public interface AbonnementPeriodiqueTransformer {


    AbonnementPeriodiqueTransformer INSTANCE = Mappers.getMapper(AbonnementPeriodiqueTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.dateDebutAbonnement", target = "dateDebutAbonnement"),
            @Mapping(source = "entity.dateFinAbonnement", target = "dateFinAbonnement"),
            @Mapping(source = "entity.redevance", target = "redevance"),
            @Mapping(source = "entity.redevancePublicite", target = "redevancePublicite"),

            @Mapping(source = "entity.compagnieTransport.raisonSociale", target = "compagnieTransportRaisonSociale"),
            @Mapping(source = "entity.periodiciteAbonnement.designation", target = "periodiciteAbonnementDesignation"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    AbonnementPeriodiqueDTO toDto(AbonnementPeriodique entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<AbonnementPeriodiqueDTO> toDtos(List<AbonnementPeriodique> entities) throws ParseException;

    default AbonnementPeriodiqueDTO toLiteDto(AbonnementPeriodique entity) {
        if (entity == null) {
            return null;
        }
        AbonnementPeriodiqueDTO dto = new AbonnementPeriodiqueDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setRedevance(entity.getRedevance());
        dto.setRedevancePublicite(entity.getRedevancePublicite());
        return dto;
    }

    default List<AbonnementPeriodiqueDTO> toLiteDtos(List<AbonnementPeriodique> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<AbonnementPeriodiqueDTO> dtos = new ArrayList<>();
        for (AbonnementPeriodique entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),

            @Mapping(source = "dto.dateDebutAbonnement", dateFormat="dd/MM/yyyy",target = "dateDebutAbonnement"),
            @Mapping(source = "dto.dateFinAbonnement",   dateFormat="dd/MM/yyyy",target = "dateFinAbonnement"),
            @Mapping(source = "dto.redevance", target = "redevance"),
            @Mapping(source = "dto.redevancePublicite", target = "redevancePublicite"),

            @Mapping(source = "dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "dto.updatedBy", target="updatedBy"),
            @Mapping(source = "dto.createdBy", target="createdBy"),
            @Mapping(source = "dto.deletedBy", target="deletedBy"),
            @Mapping(source = "dto.isDeleted", target="isDeleted"),

            @Mapping(source = "compagnieTransport", target="compagnieTransport"),
            @Mapping(source = "periodiciteAbonnement", target="periodiciteAbonnement"),
    })
    AbonnementPeriodique toEntity(AbonnementPeriodiqueDTO dto, CompagnieTransport compagnieTransport, Reference periodiciteAbonnement) throws ParseException;
}
