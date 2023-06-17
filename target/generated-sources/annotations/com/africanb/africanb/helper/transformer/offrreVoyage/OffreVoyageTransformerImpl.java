package com.africanb.africanb.helper.transformer.offrreVoyage;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-17T18:03:40+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class OffreVoyageTransformerImpl implements OffreVoyageTransformer {

    @Override
    public OffreVoyageDTO toDto(OffreVoyage entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        OffreVoyageDTO offreVoyageDTO = new OffreVoyageDTO();

        offreVoyageDTO.setId( entity.getId() );
        offreVoyageDTO.setDesignation( entity.getDesignation() );
        offreVoyageDTO.setDescription( entity.getDescription() );
        offreVoyageDTO.setIsActif( entity.getIsActif() );
        offreVoyageDTO.setVilleDepartDesignation( entityVilleDepartDesignation( entity ) );
        offreVoyageDTO.setVilleDestinationDesignation( entityVilleDestinationDesignation( entity ) );
        offreVoyageDTO.setTypeOffreVoyageDesignation( entityTypeOffreVoyageDesignation( entity ) );
        offreVoyageDTO.setCompagnieTransportRaisonSociale( entityCompagnieTransportRaisonSociale( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            offreVoyageDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            offreVoyageDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            offreVoyageDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        offreVoyageDTO.setUpdatedBy( entity.getUpdatedBy() );
        offreVoyageDTO.setCreatedBy( entity.getCreatedBy() );
        offreVoyageDTO.setDeletedBy( entity.getDeletedBy() );
        offreVoyageDTO.setIsDeleted( entity.getIsDeleted() );

        return offreVoyageDTO;
    }

    @Override
    public List<OffreVoyageDTO> toDtos(List<OffreVoyage> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<OffreVoyageDTO> list = new ArrayList<OffreVoyageDTO>( entities.size() );
        for ( OffreVoyage offreVoyage : entities ) {
            list.add( toDto( offreVoyage ) );
        }

        return list;
    }

    @Override
    public OffreVoyage toEntity(OffreVoyageDTO dto, Ville villeDepart, Ville villeDestination, Reference typeOffreVoyage, CompagnieTransport compagnieTransport) throws ParseException {
        if ( dto == null && villeDepart == null && villeDestination == null && typeOffreVoyage == null && compagnieTransport == null ) {
            return null;
        }

        OffreVoyage offreVoyage = new OffreVoyage();

        if ( dto != null ) {
            offreVoyage.setId( dto.getId() );
            offreVoyage.setDesignation( dto.getDesignation() );
            offreVoyage.setDescription( dto.getDescription() );
            offreVoyage.setIsActif( dto.getIsActif() );
            if ( dto.getUpdatedAt() != null ) {
                offreVoyage.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                offreVoyage.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                offreVoyage.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            offreVoyage.setUpdatedBy( dto.getUpdatedBy() );
            offreVoyage.setCreatedBy( dto.getCreatedBy() );
            offreVoyage.setDeletedBy( dto.getDeletedBy() );
            offreVoyage.setIsDeleted( dto.getIsDeleted() );
        }
        if ( villeDepart != null ) {
            offreVoyage.setVilleDepart( villeDepart );
        }
        if ( villeDestination != null ) {
            offreVoyage.setVilleDestination( villeDestination );
        }
        if ( typeOffreVoyage != null ) {
            offreVoyage.setTypeOffreVoyage( typeOffreVoyage );
        }
        if ( compagnieTransport != null ) {
            offreVoyage.setCompagnieTransport( compagnieTransport );
        }

        return offreVoyage;
    }

    private String entityVilleDepartDesignation(OffreVoyage offreVoyage) {
        if ( offreVoyage == null ) {
            return null;
        }
        Ville villeDepart = offreVoyage.getVilleDepart();
        if ( villeDepart == null ) {
            return null;
        }
        String designation = villeDepart.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityVilleDestinationDesignation(OffreVoyage offreVoyage) {
        if ( offreVoyage == null ) {
            return null;
        }
        Ville villeDestination = offreVoyage.getVilleDestination();
        if ( villeDestination == null ) {
            return null;
        }
        String designation = villeDestination.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityTypeOffreVoyageDesignation(OffreVoyage offreVoyage) {
        if ( offreVoyage == null ) {
            return null;
        }
        Reference typeOffreVoyage = offreVoyage.getTypeOffreVoyage();
        if ( typeOffreVoyage == null ) {
            return null;
        }
        String designation = typeOffreVoyage.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityCompagnieTransportRaisonSociale(OffreVoyage offreVoyage) {
        if ( offreVoyage == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = offreVoyage.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        String raisonSociale = compagnieTransport.getRaisonSociale();
        if ( raisonSociale == null ) {
            return null;
        }
        return raisonSociale;
    }
}
