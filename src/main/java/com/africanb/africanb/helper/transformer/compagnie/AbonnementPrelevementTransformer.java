package com.africanb.africanb.helper.transformer.compagnie;



import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPrelevement;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPrelevementDTO;
import com.africanb.africanb.utils.Reference.Reference;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface AbonnementPrelevementTransformer {


    AbonnementPrelevementTransformer INSTANCE = Mappers.getMapper(AbonnementPrelevementTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.dateDebutAbonnement", dateFormat="dd/MM/yyyy",target = "dateDebutAbonnement"),
            @Mapping(source = "entity.dateFinAbonnement", dateFormat="dd/MM/yyyy",target = "dateFinAbonnement"),
            @Mapping(source = "entity.taux", target = "taux"),

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
    AbonnementPrelevementDTO toDto(AbonnementPrelevement entity) throws ParseException;;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<AbonnementPrelevementDTO> toDtos(List<AbonnementPrelevement> entities) throws ParseException;

    default AbonnementPrelevementDTO toLiteDto(AbonnementPrelevement entity) {
        if (entity == null) {
            return null;
        }
        AbonnementPrelevementDTO dto = new AbonnementPrelevementDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setTaux(entity.getTaux());

        return dto;
    }

    default List<AbonnementPrelevementDTO> toLiteDtos(List<AbonnementPrelevement> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<AbonnementPrelevementDTO> dtos = new ArrayList<AbonnementPrelevementDTO>();
        for (AbonnementPrelevement entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),

            @Mapping(source = "dto.dateDebutAbonnement", dateFormat="dd/MM/yyyy", target = "dateDebutAbonnement"),
            @Mapping(source = "dto.dateFinAbonnement", dateFormat="dd/MM/yyyy", target = "dateFinAbonnement"),
            @Mapping(source = "dto.taux", target = "taux"),

            @Mapping(source = "dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "dto.updatedBy", target="updatedBy"),
            @Mapping(source = "dto.createdBy", target="createdBy"),
            @Mapping(source = "dto.deletedBy", target="deletedBy"),
            @Mapping(source = "dto.isDeleted", target="isDeleted"),

            @Mapping(source = "compagnieTransport", target="compagnieTransport"),
            @Mapping(source = "periodiciteAbonnement", target="periodiciteAbonnement")
    })
    AbonnementPrelevement toEntity(AbonnementPrelevementDTO dto, CompagnieTransport compagnieTransport, Reference periodiciteAbonnement) throws ParseException;
}
