package com.africanb.africanb.helper.transformer.offrreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageString;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageStringDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T16:51:03+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
public class ValeurCaracteristiqueOffreVoyageStringTransformerImpl implements ValeurCaracteristiqueOffreVoyageStringTransformer {

    @Override
    public ValeurCaracteristiqueOffreVoyageStringDTO toDto(ValeurCaracteristiqueOffreVoyageString entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ValeurCaracteristiqueOffreVoyageStringDTO valeurCaracteristiqueOffreVoyageStringDTO = new ValeurCaracteristiqueOffreVoyageStringDTO();

        valeurCaracteristiqueOffreVoyageStringDTO.setId( entity.getId() );
        valeurCaracteristiqueOffreVoyageStringDTO.setDesignation( entity.getDesignation() );
        valeurCaracteristiqueOffreVoyageStringDTO.setDescription( entity.getDescription() );
        valeurCaracteristiqueOffreVoyageStringDTO.setValeur( entity.getValeur() );
        valeurCaracteristiqueOffreVoyageStringDTO.setOffreVoyageDesignation( entityOffreVoyageDesignation( entity ) );
        valeurCaracteristiqueOffreVoyageStringDTO.setProprieteOffreVoyageDesignation( entityProprieteOffreVoyageDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            valeurCaracteristiqueOffreVoyageStringDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            valeurCaracteristiqueOffreVoyageStringDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            valeurCaracteristiqueOffreVoyageStringDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        valeurCaracteristiqueOffreVoyageStringDTO.setUpdatedBy( entity.getUpdatedBy() );
        valeurCaracteristiqueOffreVoyageStringDTO.setCreatedBy( entity.getCreatedBy() );
        valeurCaracteristiqueOffreVoyageStringDTO.setDeletedBy( entity.getDeletedBy() );
        valeurCaracteristiqueOffreVoyageStringDTO.setIsDeleted( entity.getIsDeleted() );
        valeurCaracteristiqueOffreVoyageStringDTO.setValeurTexte( entity.getValeurTexte() );

        return valeurCaracteristiqueOffreVoyageStringDTO;
    }

    @Override
    public List<ValeurCaracteristiqueOffreVoyageStringDTO> toDtos(List<ValeurCaracteristiqueOffreVoyageString> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ValeurCaracteristiqueOffreVoyageStringDTO> list = new ArrayList<ValeurCaracteristiqueOffreVoyageStringDTO>( entities.size() );
        for ( ValeurCaracteristiqueOffreVoyageString valeurCaracteristiqueOffreVoyageString : entities ) {
            list.add( toDto( valeurCaracteristiqueOffreVoyageString ) );
        }

        return list;
    }

    @Override
    public ValeurCaracteristiqueOffreVoyageString toEntity(ValeurCaracteristiqueOffreVoyageStringDTO dto, OffreVoyage offreVoyage, ProprieteOffreVoyage proprieteOffreVoyage) throws ParseException {
        if ( dto == null && offreVoyage == null && proprieteOffreVoyage == null ) {
            return null;
        }

        ValeurCaracteristiqueOffreVoyageString valeurCaracteristiqueOffreVoyageString = new ValeurCaracteristiqueOffreVoyageString();

        if ( dto != null ) {
            valeurCaracteristiqueOffreVoyageString.setId( dto.getId() );
            valeurCaracteristiqueOffreVoyageString.setDesignation( dto.getDesignation() );
            valeurCaracteristiqueOffreVoyageString.setDescription( dto.getDescription() );
            valeurCaracteristiqueOffreVoyageString.setValeur( dto.getValeur() );
            if ( dto.getUpdatedAt() != null ) {
                valeurCaracteristiqueOffreVoyageString.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                valeurCaracteristiqueOffreVoyageString.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                valeurCaracteristiqueOffreVoyageString.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            valeurCaracteristiqueOffreVoyageString.setUpdatedBy( dto.getUpdatedBy() );
            valeurCaracteristiqueOffreVoyageString.setCreatedBy( dto.getCreatedBy() );
            valeurCaracteristiqueOffreVoyageString.setDeletedBy( dto.getDeletedBy() );
            valeurCaracteristiqueOffreVoyageString.setIsDeleted( dto.getIsDeleted() );
            valeurCaracteristiqueOffreVoyageString.setValeurTexte( dto.getValeurTexte() );
        }
        if ( offreVoyage != null ) {
            valeurCaracteristiqueOffreVoyageString.setOffreVoyage( offreVoyage );
        }
        if ( proprieteOffreVoyage != null ) {
            valeurCaracteristiqueOffreVoyageString.setProprieteOffreVoyage( proprieteOffreVoyage );
        }

        return valeurCaracteristiqueOffreVoyageString;
    }

    private String entityOffreVoyageDesignation(ValeurCaracteristiqueOffreVoyageString valeurCaracteristiqueOffreVoyageString) {
        if ( valeurCaracteristiqueOffreVoyageString == null ) {
            return null;
        }
        OffreVoyage offreVoyage = valeurCaracteristiqueOffreVoyageString.getOffreVoyage();
        if ( offreVoyage == null ) {
            return null;
        }
        String designation = offreVoyage.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityProprieteOffreVoyageDesignation(ValeurCaracteristiqueOffreVoyageString valeurCaracteristiqueOffreVoyageString) {
        if ( valeurCaracteristiqueOffreVoyageString == null ) {
            return null;
        }
        ProprieteOffreVoyage proprieteOffreVoyage = valeurCaracteristiqueOffreVoyageString.getProprieteOffreVoyage();
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
