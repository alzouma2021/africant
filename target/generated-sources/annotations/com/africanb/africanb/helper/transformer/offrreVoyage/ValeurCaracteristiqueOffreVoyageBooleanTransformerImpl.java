package com.africanb.africanb.helper.transformer.offrreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageBoolean;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageBooleanDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-07T12:59:20+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ValeurCaracteristiqueOffreVoyageBooleanTransformerImpl implements ValeurCaracteristiqueOffreVoyageBooleanTransformer {

    @Override
    public ValeurCaracteristiqueOffreVoyageBooleanDTO toDto(ValeurCaracteristiqueOffreVoyageBoolean entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ValeurCaracteristiqueOffreVoyageBooleanDTO valeurCaracteristiqueOffreVoyageBooleanDTO = new ValeurCaracteristiqueOffreVoyageBooleanDTO();

        valeurCaracteristiqueOffreVoyageBooleanDTO.setId( entity.getId() );
        valeurCaracteristiqueOffreVoyageBooleanDTO.setDesignation( entity.getDesignation() );
        valeurCaracteristiqueOffreVoyageBooleanDTO.setDescription( entity.getDescription() );
        valeurCaracteristiqueOffreVoyageBooleanDTO.setValeur( entity.getValeur() );
        valeurCaracteristiqueOffreVoyageBooleanDTO.setOffreVoyageDesignation( entityOffreVoyageDesignation( entity ) );
        valeurCaracteristiqueOffreVoyageBooleanDTO.setProprieteOffreVoyageDesignation( entityProprieteOffreVoyageDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            valeurCaracteristiqueOffreVoyageBooleanDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            valeurCaracteristiqueOffreVoyageBooleanDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            valeurCaracteristiqueOffreVoyageBooleanDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        valeurCaracteristiqueOffreVoyageBooleanDTO.setUpdatedBy( entity.getUpdatedBy() );
        valeurCaracteristiqueOffreVoyageBooleanDTO.setCreatedBy( entity.getCreatedBy() );
        valeurCaracteristiqueOffreVoyageBooleanDTO.setDeletedBy( entity.getDeletedBy() );
        valeurCaracteristiqueOffreVoyageBooleanDTO.setIsDeleted( entity.getIsDeleted() );
        valeurCaracteristiqueOffreVoyageBooleanDTO.setValeurTexte( entity.getValeurTexte() );

        return valeurCaracteristiqueOffreVoyageBooleanDTO;
    }

    @Override
    public List<ValeurCaracteristiqueOffreVoyageBooleanDTO> toDtos(List<ValeurCaracteristiqueOffreVoyageBoolean> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ValeurCaracteristiqueOffreVoyageBooleanDTO> list = new ArrayList<ValeurCaracteristiqueOffreVoyageBooleanDTO>( entities.size() );
        for ( ValeurCaracteristiqueOffreVoyageBoolean valeurCaracteristiqueOffreVoyageBoolean : entities ) {
            list.add( toDto( valeurCaracteristiqueOffreVoyageBoolean ) );
        }

        return list;
    }

    @Override
    public ValeurCaracteristiqueOffreVoyageBoolean toEntity(ValeurCaracteristiqueOffreVoyageBooleanDTO dto, OffreVoyage offreVoyage, ProprieteOffreVoyage proprieteOffreVoyage) throws ParseException {
        if ( dto == null && offreVoyage == null && proprieteOffreVoyage == null ) {
            return null;
        }

        ValeurCaracteristiqueOffreVoyageBoolean valeurCaracteristiqueOffreVoyageBoolean = new ValeurCaracteristiqueOffreVoyageBoolean();

        if ( dto != null ) {
            valeurCaracteristiqueOffreVoyageBoolean.setId( dto.getId() );
            valeurCaracteristiqueOffreVoyageBoolean.setDesignation( dto.getDesignation() );
            valeurCaracteristiqueOffreVoyageBoolean.setDescription( dto.getDescription() );
            valeurCaracteristiqueOffreVoyageBoolean.setValeur( dto.getValeur() );
            if ( dto.getUpdatedAt() != null ) {
                valeurCaracteristiqueOffreVoyageBoolean.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                valeurCaracteristiqueOffreVoyageBoolean.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                valeurCaracteristiqueOffreVoyageBoolean.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            valeurCaracteristiqueOffreVoyageBoolean.setUpdatedBy( dto.getUpdatedBy() );
            valeurCaracteristiqueOffreVoyageBoolean.setCreatedBy( dto.getCreatedBy() );
            valeurCaracteristiqueOffreVoyageBoolean.setDeletedBy( dto.getDeletedBy() );
            valeurCaracteristiqueOffreVoyageBoolean.setIsDeleted( dto.getIsDeleted() );
            valeurCaracteristiqueOffreVoyageBoolean.setValeurTexte( dto.getValeurTexte() );
        }
        if ( offreVoyage != null ) {
            valeurCaracteristiqueOffreVoyageBoolean.setOffreVoyage( offreVoyage );
        }
        if ( proprieteOffreVoyage != null ) {
            valeurCaracteristiqueOffreVoyageBoolean.setProprieteOffreVoyage( proprieteOffreVoyage );
        }

        return valeurCaracteristiqueOffreVoyageBoolean;
    }

    private String entityOffreVoyageDesignation(ValeurCaracteristiqueOffreVoyageBoolean valeurCaracteristiqueOffreVoyageBoolean) {
        if ( valeurCaracteristiqueOffreVoyageBoolean == null ) {
            return null;
        }
        OffreVoyage offreVoyage = valeurCaracteristiqueOffreVoyageBoolean.getOffreVoyage();
        if ( offreVoyage == null ) {
            return null;
        }
        String designation = offreVoyage.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityProprieteOffreVoyageDesignation(ValeurCaracteristiqueOffreVoyageBoolean valeurCaracteristiqueOffreVoyageBoolean) {
        if ( valeurCaracteristiqueOffreVoyageBoolean == null ) {
            return null;
        }
        ProprieteOffreVoyage proprieteOffreVoyage = valeurCaracteristiqueOffreVoyageBoolean.getProprieteOffreVoyage();
        if ( proprieteOffreVoyage == null ) {
            return null;
        }
        String designation = proprieteOffreVoyage.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
