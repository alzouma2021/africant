package com.africanb.africanb.helper.transformer.offrreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageLong;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageLongDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-12T21:18:26+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ValeurCaracteristiqueOffreVoyageLongTransformerImpl implements ValeurCaracteristiqueOffreVoyageLongTransformer {

    @Override
    public ValeurCaracteristiqueOffreVoyageLongDTO toDto(ValeurCaracteristiqueOffreVoyageLong entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ValeurCaracteristiqueOffreVoyageLongDTO valeurCaracteristiqueOffreVoyageLongDTO = new ValeurCaracteristiqueOffreVoyageLongDTO();

        valeurCaracteristiqueOffreVoyageLongDTO.setId( entity.getId() );
        valeurCaracteristiqueOffreVoyageLongDTO.setDesignation( entity.getDesignation() );
        valeurCaracteristiqueOffreVoyageLongDTO.setDescription( entity.getDescription() );
        valeurCaracteristiqueOffreVoyageLongDTO.setValeur( entity.getValeur() );
        valeurCaracteristiqueOffreVoyageLongDTO.setOffreVoyageDesignation( entityOffreVoyageDesignation( entity ) );
        valeurCaracteristiqueOffreVoyageLongDTO.setProprieteOffreVoyageDesignation( entityProprieteOffreVoyageDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            valeurCaracteristiqueOffreVoyageLongDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            valeurCaracteristiqueOffreVoyageLongDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            valeurCaracteristiqueOffreVoyageLongDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        valeurCaracteristiqueOffreVoyageLongDTO.setUpdatedBy( entity.getUpdatedBy() );
        valeurCaracteristiqueOffreVoyageLongDTO.setCreatedBy( entity.getCreatedBy() );
        valeurCaracteristiqueOffreVoyageLongDTO.setDeletedBy( entity.getDeletedBy() );
        valeurCaracteristiqueOffreVoyageLongDTO.setIsDeleted( entity.getIsDeleted() );
        valeurCaracteristiqueOffreVoyageLongDTO.setValeurTexte( entity.getValeurTexte() );

        return valeurCaracteristiqueOffreVoyageLongDTO;
    }

    @Override
    public List<ValeurCaracteristiqueOffreVoyageLongDTO> toDtos(List<ValeurCaracteristiqueOffreVoyageLong> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ValeurCaracteristiqueOffreVoyageLongDTO> list = new ArrayList<ValeurCaracteristiqueOffreVoyageLongDTO>( entities.size() );
        for ( ValeurCaracteristiqueOffreVoyageLong valeurCaracteristiqueOffreVoyageLong : entities ) {
            list.add( toDto( valeurCaracteristiqueOffreVoyageLong ) );
        }

        return list;
    }

    @Override
    public ValeurCaracteristiqueOffreVoyageLong toEntity(ValeurCaracteristiqueOffreVoyageLongDTO dto, OffreVoyage offreVoyage, ProprieteOffreVoyage proprieteOffreVoyage) throws ParseException {
        if ( dto == null && offreVoyage == null && proprieteOffreVoyage == null ) {
            return null;
        }

        ValeurCaracteristiqueOffreVoyageLong valeurCaracteristiqueOffreVoyageLong = new ValeurCaracteristiqueOffreVoyageLong();

        if ( dto != null ) {
            valeurCaracteristiqueOffreVoyageLong.setId( dto.getId() );
            valeurCaracteristiqueOffreVoyageLong.setDesignation( dto.getDesignation() );
            valeurCaracteristiqueOffreVoyageLong.setDescription( dto.getDescription() );
            valeurCaracteristiqueOffreVoyageLong.setValeur( dto.getValeur() );
            if ( dto.getUpdatedAt() != null ) {
                valeurCaracteristiqueOffreVoyageLong.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                valeurCaracteristiqueOffreVoyageLong.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                valeurCaracteristiqueOffreVoyageLong.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            valeurCaracteristiqueOffreVoyageLong.setUpdatedBy( dto.getUpdatedBy() );
            valeurCaracteristiqueOffreVoyageLong.setCreatedBy( dto.getCreatedBy() );
            valeurCaracteristiqueOffreVoyageLong.setDeletedBy( dto.getDeletedBy() );
            valeurCaracteristiqueOffreVoyageLong.setIsDeleted( dto.getIsDeleted() );
            valeurCaracteristiqueOffreVoyageLong.setValeurTexte( dto.getValeurTexte() );
        }
        if ( offreVoyage != null ) {
            valeurCaracteristiqueOffreVoyageLong.setOffreVoyage( offreVoyage );
        }
        if ( proprieteOffreVoyage != null ) {
            valeurCaracteristiqueOffreVoyageLong.setProprieteOffreVoyage( proprieteOffreVoyage );
        }

        return valeurCaracteristiqueOffreVoyageLong;
    }

    private String entityOffreVoyageDesignation(ValeurCaracteristiqueOffreVoyageLong valeurCaracteristiqueOffreVoyageLong) {
        if ( valeurCaracteristiqueOffreVoyageLong == null ) {
            return null;
        }
        OffreVoyage offreVoyage = valeurCaracteristiqueOffreVoyageLong.getOffreVoyage();
        if ( offreVoyage == null ) {
            return null;
        }
        String designation = offreVoyage.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityProprieteOffreVoyageDesignation(ValeurCaracteristiqueOffreVoyageLong valeurCaracteristiqueOffreVoyageLong) {
        if ( valeurCaracteristiqueOffreVoyageLong == null ) {
            return null;
        }
        ProprieteOffreVoyage proprieteOffreVoyage = valeurCaracteristiqueOffreVoyageLong.getProprieteOffreVoyage();
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
