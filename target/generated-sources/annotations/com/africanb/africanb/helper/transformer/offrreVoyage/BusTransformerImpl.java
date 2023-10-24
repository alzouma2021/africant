package com.africanb.africanb.helper.transformer.offrreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.Bus;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.helper.dto.offreVoyage.BusDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-24T11:04:17+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class BusTransformerImpl implements BusTransformer {

    @Override
    public BusDTO toDto(Bus entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        BusDTO busDTO = new BusDTO();

        busDTO.setId( entity.getId() );
        busDTO.setDesignation( entity.getDesignation() );
        busDTO.setDescription( entity.getDescription() );
        busDTO.setNumeroBus( entity.getNumeroBus() );
        busDTO.setNombrePlace( entity.getNombrePlace() );
        busDTO.setOffreVoyageDesignation( entityOffreVoyageDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            busDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            busDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            busDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        busDTO.setUpdatedBy( entity.getUpdatedBy() );
        busDTO.setCreatedBy( entity.getCreatedBy() );
        busDTO.setDeletedBy( entity.getDeletedBy() );
        busDTO.setIsDeleted( entity.getIsDeleted() );

        return busDTO;
    }

    @Override
    public List<BusDTO> toDtos(List<Bus> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<BusDTO> list = new ArrayList<BusDTO>( entities.size() );
        for ( Bus bus : entities ) {
            list.add( toDto( bus ) );
        }

        return list;
    }

    @Override
    public Bus toEntity(BusDTO dto, OffreVoyage offreVoyage) throws ParseException {
        if ( dto == null && offreVoyage == null ) {
            return null;
        }

        Bus bus = new Bus();

        if ( dto != null ) {
            bus.setId( dto.getId() );
            bus.setDesignation( dto.getDesignation() );
            bus.setDescription( dto.getDescription() );
            bus.setNumeroBus( dto.getNumeroBus() );
            bus.setNombrePlace( dto.getNombrePlace() );
            if ( dto.getUpdatedAt() != null ) {
                bus.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                bus.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                bus.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            bus.setUpdatedBy( dto.getUpdatedBy() );
            bus.setCreatedBy( dto.getCreatedBy() );
            bus.setDeletedBy( dto.getDeletedBy() );
            bus.setIsDeleted( dto.getIsDeleted() );
        }
        if ( offreVoyage != null ) {
            bus.setOffreVoyage( offreVoyage );
        }

        return bus;
    }

    private String entityOffreVoyageDesignation(Bus bus) {
        if ( bus == null ) {
            return null;
        }
        OffreVoyage offreVoyage = bus.getOffreVoyage();
        if ( offreVoyage == null ) {
            return null;
        }
        String designation = offreVoyage.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
