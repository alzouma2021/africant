package com.africanb.africanb.helper.transformer.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiement;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.HistoriquePaiement;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.ReservationBilletVoyage;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.HistoriquePaiementDTO;
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
public interface HistoriquePaiementTransformer {

    HistoriquePaiementTransformer INSTANCE = Mappers.getMapper(HistoriquePaiementTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.identifiantUnique", target = "identifiantUnique"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.dateTimePayment", target = "dateTimePayment"),
            @Mapping(source = "entity.modePaiement.designation", target = "modePaiementDesignation"),
            @Mapping(source = "entity.reservationBilletVoyage.designation", target = "reservationBilletVoyageDesignation")
    })
    HistoriquePaiementDTO toDto(HistoriquePaiement entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<HistoriquePaiementDTO> toDtos(List<HistoriquePaiement> entities) throws ParseException;

    default HistoriquePaiementDTO toLiteDto(HistoriquePaiement entity) {
        if (entity == null) {
            return null;
        }
        HistoriquePaiementDTO dto = new HistoriquePaiementDTO();
        dto.setId( entity.getId() );
       dto.setIdentifiantUnique(entity.getIdentifiantUnique());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    default List<HistoriquePaiementDTO> toLiteDtos(List<HistoriquePaiement> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<HistoriquePaiementDTO> dtos = new ArrayList<>();
        for (HistoriquePaiement entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.identifiantUnique", target = "identifiantUnique"),
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(source = "dto.dateTimePayment", target = "dateTimePayment"),
            @Mapping(source="modePaiement", target="modePaiement"),
            @Mapping(source="reservationBilletVoyage", target="reservationBilletVoyage")
    })
    HistoriquePaiement toEntity(HistoriquePaiementDTO dto, ModePaiement modePaiement, ReservationBilletVoyage reservationBilletVoyage) throws ParseException;

}
