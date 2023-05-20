package com.africanb.africanb.helper.transformer.document;


import com.africanb.africanb.dao.entity.compagnie.Bagage;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.document.Document;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.compagnie.BagageDTO;
import com.africanb.africanb.helper.dto.document.DocumentDTO;
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
public interface DocumentTransformer {

    DocumentTransformer INSTANCE = Mappers.getMapper(DocumentTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.path", target = "path"),
            @Mapping(source = "entity.typeMime", target = "typeMime"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    DocumentDTO toDto(Document entity) throws ParseException;;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<DocumentDTO> toDtos(List<Document> entities) throws ParseException;

    default DocumentDTO toLiteDto(Document entity) {
        if (entity == null) {
            return null;
        }
        DocumentDTO dto = new DocumentDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setPath(entity.getPath());
        dto.setTypeMime(entity.getTypeMime());
        dto.setExtension(entity.getExtension());
        return dto;
    }

    default List<DocumentDTO> toLiteDtos(List<Document> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<DocumentDTO> dtos = new ArrayList<DocumentDTO>();
        for (Document entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(source = "dto.path", target = "path"),
            @Mapping(source = "dto.typeMime", target = "typeMime"),

            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy", target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),

    })
    Document toEntity(DocumentDTO dto) throws ParseException;
}
