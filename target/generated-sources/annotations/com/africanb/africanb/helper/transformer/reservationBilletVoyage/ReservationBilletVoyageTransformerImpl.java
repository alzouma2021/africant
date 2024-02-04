package com.africanb.africanb.helper.transformer.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.compagnie.Gare;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.Programme;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.ReservationBilletVoyage;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.ReservationBilletVoyageDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-03T12:04:38+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ReservationBilletVoyageTransformerImpl implements ReservationBilletVoyageTransformer {

    @Override
    public ReservationBilletVoyageDTO toDto(ReservationBilletVoyage entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ReservationBilletVoyageDTO reservationBilletVoyageDTO = new ReservationBilletVoyageDTO();

        reservationBilletVoyageDTO.setId( entity.getId() );
        reservationBilletVoyageDTO.setDesignation( entity.getDesignation() );
        reservationBilletVoyageDTO.setDescription( entity.getDescription() );
        reservationBilletVoyageDTO.setDateReservation( entity.getDateReservation() );
        reservationBilletVoyageDTO.setDateEffectiveDepart( entity.getDateEffectiveDepart() );
        reservationBilletVoyageDTO.setMontantTotalReservation( entity.getMontantTotalReservation() );
        reservationBilletVoyageDTO.setNombrePlace( entity.getNombrePlace() );
        reservationBilletVoyageDTO.setIsCanceled( entity.getIsCanceled() );
        reservationBilletVoyageDTO.setRaisonAnnulation( entity.getRaisonAnnulation() );
        reservationBilletVoyageDTO.setGareDesignation( entityGareDesignation( entity ) );
        reservationBilletVoyageDTO.setOffreVoyageDesignation( entityOffreVoyageDesignation( entity ) );
        reservationBilletVoyageDTO.setProgrammeDesignation( entityProgrammeDesignation( entity ) );
        reservationBilletVoyageDTO.setIsOtherPerson( entity.getIsOtherPerson() );
        reservationBilletVoyageDTO.setClientDetails( entity.getClientDetails() );
        reservationBilletVoyageDTO.setStatusActualDesignation( entityStatusUtilActualDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            reservationBilletVoyageDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            reservationBilletVoyageDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            reservationBilletVoyageDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        reservationBilletVoyageDTO.setUpdatedBy( entity.getUpdatedBy() );
        reservationBilletVoyageDTO.setCreatedBy( entity.getCreatedBy() );
        reservationBilletVoyageDTO.setDeletedBy( entity.getDeletedBy() );
        reservationBilletVoyageDTO.setIsDeleted( entity.getIsDeleted() );

        return reservationBilletVoyageDTO;
    }

    @Override
    public List<ReservationBilletVoyageDTO> toDtos(List<ReservationBilletVoyage> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ReservationBilletVoyageDTO> list = new ArrayList<ReservationBilletVoyageDTO>( entities.size() );
        for ( ReservationBilletVoyage reservationBilletVoyage : entities ) {
            list.add( toDto( reservationBilletVoyage ) );
        }

        return list;
    }

    @Override
    public ReservationBilletVoyage toEntity(ReservationBilletVoyageDTO dto, Gare gare, OffreVoyage offreVoyage, Programme programme, StatusUtil statusUtilActual) throws ParseException {
        if ( dto == null && gare == null && offreVoyage == null && programme == null && statusUtilActual == null ) {
            return null;
        }

        ReservationBilletVoyage reservationBilletVoyage = new ReservationBilletVoyage();

        if ( dto != null ) {
            reservationBilletVoyage.setId( dto.getId() );
            reservationBilletVoyage.setDesignation( dto.getDesignation() );
            reservationBilletVoyage.setDescription( dto.getDescription() );
            reservationBilletVoyage.setDateReservation( dto.getDateReservation() );
            reservationBilletVoyage.setDateEffectiveDepart( dto.getDateEffectiveDepart() );
            reservationBilletVoyage.setMontantTotalReservation( dto.getMontantTotalReservation() );
            reservationBilletVoyage.setNombrePlace( dto.getNombrePlace() );
            reservationBilletVoyage.setIsOtherPerson( dto.getIsOtherPerson() );
            reservationBilletVoyage.setClientDetails( dto.getClientDetails() );
            reservationBilletVoyage.setIsCanceled( dto.getIsCanceled() );
            reservationBilletVoyage.setRaisonAnnulation( dto.getRaisonAnnulation() );
            if ( dto.getUpdatedAt() != null ) {
                reservationBilletVoyage.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                reservationBilletVoyage.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                reservationBilletVoyage.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            reservationBilletVoyage.setUpdatedBy( dto.getUpdatedBy() );
            reservationBilletVoyage.setCreatedBy( dto.getCreatedBy() );
            reservationBilletVoyage.setDeletedBy( dto.getDeletedBy() );
            reservationBilletVoyage.setIsDeleted( dto.getIsDeleted() );
        }
        if ( gare != null ) {
            reservationBilletVoyage.setGare( gare );
        }
        if ( offreVoyage != null ) {
            reservationBilletVoyage.setOffreVoyage( offreVoyage );
        }
        if ( programme != null ) {
            reservationBilletVoyage.setProgramme( programme );
        }
        if ( statusUtilActual != null ) {
            reservationBilletVoyage.setStatusUtilActual( statusUtilActual );
        }

        return reservationBilletVoyage;
    }

    private String entityGareDesignation(ReservationBilletVoyage reservationBilletVoyage) {
        if ( reservationBilletVoyage == null ) {
            return null;
        }
        Gare gare = reservationBilletVoyage.getGare();
        if ( gare == null ) {
            return null;
        }
        String designation = gare.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityOffreVoyageDesignation(ReservationBilletVoyage reservationBilletVoyage) {
        if ( reservationBilletVoyage == null ) {
            return null;
        }
        OffreVoyage offreVoyage = reservationBilletVoyage.getOffreVoyage();
        if ( offreVoyage == null ) {
            return null;
        }
        String designation = offreVoyage.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityProgrammeDesignation(ReservationBilletVoyage reservationBilletVoyage) {
        if ( reservationBilletVoyage == null ) {
            return null;
        }
        Programme programme = reservationBilletVoyage.getProgramme();
        if ( programme == null ) {
            return null;
        }
        String designation = programme.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityStatusUtilActualDesignation(ReservationBilletVoyage reservationBilletVoyage) {
        if ( reservationBilletVoyage == null ) {
            return null;
        }
        StatusUtil statusUtilActual = reservationBilletVoyage.getStatusUtilActual();
        if ( statusUtilActual == null ) {
            return null;
        }
        String designation = statusUtilActual.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
