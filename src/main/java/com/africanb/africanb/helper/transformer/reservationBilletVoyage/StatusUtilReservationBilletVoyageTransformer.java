package com.africanb.africanb.helper.transformer.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.compagnie.StatusUtilCompagnieTransport;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.ReservationBilletVoyage;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.StatusUtilReservationBilletVoyage;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.StatusUtilReservationBilletVoyageDTO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface StatusUtilReservationBilletVoyageTransformer {

    StatusUtilReservationBilletVoyageTransformer INSTANCE = Mappers.getMapper(StatusUtilReservationBilletVoyageTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.statusUtil.designation", target = "statusUtilDesignation"),
            @Mapping(source = "entity.reservationBilletVoyage.designation", target = "reservationBilletVoyageDesignation")
    })
    StatusUtilReservationBilletVoyageDTO toDto(StatusUtilReservationBilletVoyage entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<StatusUtilReservationBilletVoyageDTO> toDtos(List<StatusUtilReservationBilletVoyage> entities) throws ParseException;

    public default StatusUtilReservationBilletVoyageDTO toLiteDto(StatusUtilReservationBilletVoyage entity) {
        if (entity == null) {
            return null;
        }
        StatusUtilReservationBilletVoyageDTO dto = new StatusUtilReservationBilletVoyageDTO();
        dto.setId(entity.getId() );
        dto.setStatusUtilDesignation(entity.getStatusUtil().getDescription());
        dto.setReservationBilletVoyageDesignation(entity.getReservationBilletVoyage().getDesignation());
        return dto;
    }

    public default List<StatusUtilReservationBilletVoyageDTO> toLiteDtos(List<StatusUtilReservationBilletVoyage> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<StatusUtilReservationBilletVoyageDTO> dtos = new ArrayList<StatusUtilReservationBilletVoyageDTO>();
        for (StatusUtilReservationBilletVoyage entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "reservationBilletVoyage", target = "reservationBilletVoyage"),
            @Mapping(source = "statusUtil", target = "statusUtil"),
    })
    StatusUtilReservationBilletVoyage toEntity(StatusUtilReservationBilletVoyageDTO dto, ReservationBilletVoyage reservationBilletVoyage, StatusUtil statusUtil) throws ParseException;
}
