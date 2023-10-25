package com.africanb.africanb.helper.transformer.reservationBilletVoyage;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.Gare;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.Programme;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.ReservationBilletVoyage;
import com.africanb.africanb.dao.entity.security.Users;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.ReservationBilletVoyageDTO;
import com.africanb.africanb.helper.status.Status;
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
public interface ReservationBilletVoyageTransformer {

    ReservationBilletVoyageTransformer INSTANCE = Mappers.getMapper(ReservationBilletVoyageTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.dateReservation", dateFormat="dd/MM/yyyy",target = "dateReservation"),
            @Mapping(source = "entity.dateEffectiveDepart", dateFormat="dd/MM/yyyy",target = "dateEffectiveDepart"),
            @Mapping(source = "entity.montantTotalReservation", target = "montantTotalReservation"),
            @Mapping(source = "entity.nombrePlace", target = "nombrePlace"),
            @Mapping(source = "entity.isCanceled", target = "isCanceled"),
            @Mapping(source = "entity.raisonAnnulation", target = "raisonAnnulation"),

            @Mapping(source = "entity.gare.designation", target = "gareDesignation"),
            @Mapping(source = "entity.offreVoyage.designation", target = "offreVoyageDesignation"),
            @Mapping(source = "entity.programme.designation", target = "programmeDesignation"),
            @Mapping(source = "entity.statusReservation.designation", target = "statusReservationCode"),
            @Mapping(source = "entity.users.email", target = "userEmail"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    ReservationBilletVoyageDTO toDto(ReservationBilletVoyage entity) throws ParseException;;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ReservationBilletVoyageDTO> toDtos(List<ReservationBilletVoyage> entities) throws ParseException;

    default ReservationBilletVoyageDTO toLiteDto(ReservationBilletVoyage entity) {
        if (entity == null) {
            return null;
        }
        ReservationBilletVoyageDTO dto = new ReservationBilletVoyageDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        return dto;
    }

    default List<ReservationBilletVoyageDTO> toLiteDtos(List<ReservationBilletVoyage> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<ReservationBilletVoyageDTO> dtos = new ArrayList<ReservationBilletVoyageDTO>();
        for (ReservationBilletVoyage entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(source = "dto.dateReservation", dateFormat="dd/MM/yyyy",target = "dateReservation"),
            @Mapping(source = "dto.dateEffectiveDepart", dateFormat="dd/MM/yyyy",target = "dateEffectiveDepart"),
            @Mapping(source = "dto.montantTotalReservation", target = "montantTotalReservation"),
            @Mapping(source = "dto.nombrePlace", target = "nombrePlace"),
            @Mapping(source = "dto.isCanceled", target = "isCanceled"),
            @Mapping(source = "dto.raisonAnnulation", target = "raisonAnnulation"),

            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy", target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),

            @Mapping(source="offreVoyage", target="offreVoyage"),
            @Mapping(source="programme", target="programme"),
            @Mapping(source="gare", target="gare"),
            @Mapping(source="statusReservation", target="statusReservation"),
            @Mapping(source="users", target="users")
    })
    ReservationBilletVoyage toEntity(ReservationBilletVoyageDTO dto, Gare gare, OffreVoyage offreVoyage, Programme programme, StatusUtil statusReservation, Users users) throws ParseException;
}
