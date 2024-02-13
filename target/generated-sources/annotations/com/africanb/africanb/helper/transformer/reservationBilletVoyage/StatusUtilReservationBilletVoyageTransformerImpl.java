package com.africanb.africanb.helper.transformer.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.ReservationBilletVoyage;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.StatusUtilReservationBilletVoyage;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.StatusUtilReservationBilletVoyageDTO;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-13T11:29:41+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class StatusUtilReservationBilletVoyageTransformerImpl implements StatusUtilReservationBilletVoyageTransformer {

    @Override
    public StatusUtilReservationBilletVoyageDTO toDto(StatusUtilReservationBilletVoyage entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        StatusUtilReservationBilletVoyageDTO statusUtilReservationBilletVoyageDTO = new StatusUtilReservationBilletVoyageDTO();

        statusUtilReservationBilletVoyageDTO.setId( entity.getId() );
        statusUtilReservationBilletVoyageDTO.setStatusUtilDesignation( entityStatusUtilDesignation( entity ) );
        statusUtilReservationBilletVoyageDTO.setReservationBilletVoyageDesignation( entityReservationBilletVoyageDesignation( entity ) );

        return statusUtilReservationBilletVoyageDTO;
    }

    @Override
    public List<StatusUtilReservationBilletVoyageDTO> toDtos(List<StatusUtilReservationBilletVoyage> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<StatusUtilReservationBilletVoyageDTO> list = new ArrayList<StatusUtilReservationBilletVoyageDTO>( entities.size() );
        for ( StatusUtilReservationBilletVoyage statusUtilReservationBilletVoyage : entities ) {
            list.add( toDto( statusUtilReservationBilletVoyage ) );
        }

        return list;
    }

    @Override
    public StatusUtilReservationBilletVoyage toEntity(StatusUtilReservationBilletVoyageDTO dto, ReservationBilletVoyage reservationBilletVoyage, StatusUtil statusUtil) throws ParseException {
        if ( dto == null && reservationBilletVoyage == null && statusUtil == null ) {
            return null;
        }

        StatusUtilReservationBilletVoyage statusUtilReservationBilletVoyage = new StatusUtilReservationBilletVoyage();

        if ( dto != null ) {
            statusUtilReservationBilletVoyage.setId( dto.getId() );
        }
        if ( reservationBilletVoyage != null ) {
            statusUtilReservationBilletVoyage.setReservationBilletVoyage( reservationBilletVoyage );
        }
        if ( statusUtil != null ) {
            statusUtilReservationBilletVoyage.setStatusUtil( statusUtil );
        }

        return statusUtilReservationBilletVoyage;
    }

    private String entityStatusUtilDesignation(StatusUtilReservationBilletVoyage statusUtilReservationBilletVoyage) {
        if ( statusUtilReservationBilletVoyage == null ) {
            return null;
        }
        StatusUtil statusUtil = statusUtilReservationBilletVoyage.getStatusUtil();
        if ( statusUtil == null ) {
            return null;
        }
        String designation = statusUtil.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityReservationBilletVoyageDesignation(StatusUtilReservationBilletVoyage statusUtilReservationBilletVoyage) {
        if ( statusUtilReservationBilletVoyage == null ) {
            return null;
        }
        ReservationBilletVoyage reservationBilletVoyage = statusUtilReservationBilletVoyage.getReservationBilletVoyage();
        if ( reservationBilletVoyage == null ) {
            return null;
        }
        String designation = reservationBilletVoyage.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
