package com.africanb.africanb.helper.transformer.compagnie;


import com.africanb.africanb.dao.entity.compagnie.*;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.compagnie.StatusUtilCompagnieTransportDTO;
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
public interface StatusUtilCompagnieTransportTransformer {

    StatusUtilCompagnieTransportTransformer INSTANCE = Mappers.getMapper(StatusUtilCompagnieTransportTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),

            @Mapping(source = "entity.statusUtil.id", target = "statusUtilId"),
            @Mapping(source = "entity.compagnieTransport.id", target = "compagnieTransportId"),

            @Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="entity.updatedBy", target="updatedBy"),
            @Mapping(source="entity.createdBy", target="createdBy"),
            @Mapping(source="entity.deletedBy", target="deletedBy"),
            @Mapping(source="entity.isDeleted", target="isDeleted"),
    })
    StatusUtilCompagnieTransportDTO toDto(StatusUtilCompagnieTransport entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<StatusUtilCompagnieTransportDTO> toDtos(List<StatusUtilCompagnieTransport> entities) throws ParseException;

    default StatusUtilCompagnieTransportDTO toLiteDto(StatusUtilCompagnieTransport entity) {
        if (entity == null) {
            return null;
        }
        StatusUtilCompagnieTransportDTO dto = new StatusUtilCompagnieTransportDTO();
        dto.setId(entity.getId() );
        dto.setStatusUtilId(entity.getStatusUtil().getId());
        dto.setCompagnieTransportId(entity.getCompagnieTransport().getId());
        return dto;
    }

    default List<StatusUtilCompagnieTransportDTO> toLiteDtos(List<StatusUtilCompagnieTransport> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<StatusUtilCompagnieTransportDTO> dtos = new ArrayList<>();
        for (StatusUtilCompagnieTransport entity : entities) {
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

            @Mapping(source = "compagnieTransport", target = "compagnieTransport"),
            @Mapping(source = "statusUtil", target = "statusUtil"),
    })
    StatusUtilCompagnieTransport toEntity(StatusUtilCompagnieTransportDTO dto, CompagnieTransport compagnieTransport, StatusUtil statusUtil) throws ParseException;
}
