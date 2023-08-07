package com.africanb.africanb.helper.transformer.offrreVoyage;

import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.VilleEscale;
import com.africanb.africanb.helper.dto.offreVoyage.VilleEscaleDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-07T09:59:28+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class VilleEscaleTransformerImpl implements VilleEscaleTransformer {

    @Override
    public VilleEscaleDTO toDto(VilleEscale entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        VilleEscaleDTO villeEscaleDTO = new VilleEscaleDTO();

        villeEscaleDTO.setId( entity.getId() );
        villeEscaleDTO.setOffreVoyageDesignation( entityOffreVoyageDesignation( entity ) );
        villeEscaleDTO.setVilleDesignation( entityVilleDesignation( entity ) );
        villeEscaleDTO.setPosition( entity.getPosition() );
        if ( entity.getUpdatedAt() != null ) {
            villeEscaleDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            villeEscaleDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            villeEscaleDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        villeEscaleDTO.setUpdatedBy( entity.getUpdatedBy() );
        villeEscaleDTO.setCreatedBy( entity.getCreatedBy() );
        villeEscaleDTO.setDeletedBy( entity.getDeletedBy() );
        villeEscaleDTO.setIsDeleted( entity.getIsDeleted() );

        return villeEscaleDTO;
    }

    @Override
    public List<VilleEscaleDTO> toDtos(List<VilleEscale> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<VilleEscaleDTO> list = new ArrayList<VilleEscaleDTO>( entities.size() );
        for ( VilleEscale villeEscale : entities ) {
            list.add( toDto( villeEscale ) );
        }

        return list;
    }

    @Override
    public VilleEscale toEntity(VilleEscaleDTO dto, OffreVoyage offreVoyage, Ville ville) throws ParseException {
        if ( dto == null && offreVoyage == null && ville == null ) {
            return null;
        }

        VilleEscale villeEscale = new VilleEscale();

        if ( dto != null ) {
            villeEscale.setId( dto.getId() );
            villeEscale.setPosition( dto.getPosition() );
            if ( dto.getUpdatedAt() != null ) {
                villeEscale.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                villeEscale.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                villeEscale.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            villeEscale.setUpdatedBy( dto.getUpdatedBy() );
            villeEscale.setCreatedBy( dto.getCreatedBy() );
            villeEscale.setDeletedBy( dto.getDeletedBy() );
            villeEscale.setIsDeleted( dto.getIsDeleted() );
        }
        if ( offreVoyage != null ) {
            villeEscale.setOffreVoyage( offreVoyage );
        }
        if ( ville != null ) {
            villeEscale.setVille( ville );
        }

        return villeEscale;
    }

    private String entityOffreVoyageDesignation(VilleEscale villeEscale) {
        if ( villeEscale == null ) {
            return null;
        }
        OffreVoyage offreVoyage = villeEscale.getOffreVoyage();
        if ( offreVoyage == null ) {
            return null;
        }
        String designation = offreVoyage.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityVilleDesignation(VilleEscale villeEscale) {
        if ( villeEscale == null ) {
            return null;
        }
        Ville ville = villeEscale.getVille();
        if ( ville == null ) {
            return null;
        }
        String designation = ville.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
