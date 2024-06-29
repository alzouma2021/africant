package com.africanb.africanb.helper.transformer.compagnie;


import com.africanb.africanb.dao.entity.compagnie.CompagnieAttestionTransport;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.document.Document;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.compagnie.CompagnieAttestionTransportDTO;
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
public interface CompagnieAttestionTransportTransformer {

    CompagnieAttestionTransportTransformer INSTANCE = Mappers.getMapper(CompagnieAttestionTransportTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),

            @Mapping(source = "entity.compagnie.raisonSociale", target = "compagnieRaisonSociale"),
            @Mapping(source = "entity.attestionTransport.designation", target = "documentDesignation"),

            @Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="entity.updatedBy", target="updatedBy"),
            @Mapping(source="entity.createdBy", target="createdBy"),
            @Mapping(source="entity.deletedBy", target="deletedBy"),
            @Mapping(source="entity.isDeleted", target="isDeleted"),
    })
    CompagnieAttestionTransportDTO toDto(CompagnieAttestionTransport entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<CompagnieAttestionTransportDTO> toDtos(List<CompagnieAttestionTransport> entities) throws ParseException;

    public default CompagnieAttestionTransportDTO toLiteDto(CompagnieAttestionTransport entity) {
        if (entity == null) {
            return null;
        }
        CompagnieAttestionTransportDTO dto = new CompagnieAttestionTransportDTO();
        dto.setId(entity.getId() );
        dto.setCompagnieRaisonSociale(entity.getCompagnie().getRaisonSociale());
        dto.setDocumentDesignation(entity.getAttestionTransport().getDesignation());
        return dto;
    }

    public default List<CompagnieAttestionTransportDTO> toLiteDtos(List<CompagnieAttestionTransport> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<CompagnieAttestionTransportDTO> dtos = new ArrayList<>();
        for (CompagnieAttestionTransport entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),

            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy", target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),

            @Mapping(source = "compagnie", target = "compagnie"),
            @Mapping(source = "attestionTransport", target = "attestionTransport"),
    })
    CompagnieAttestionTransport toEntity(CompagnieAttestionTransportDTO dto, CompagnieTransport compagnie, Document attestionTransport) throws ParseException;
}
