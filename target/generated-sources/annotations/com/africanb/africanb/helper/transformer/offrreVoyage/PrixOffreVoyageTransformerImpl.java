package com.africanb.africanb.helper.transformer.offrreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import com.africanb.africanb.helper.dto.offreVoyage.PrixOffreVoyageDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-01T10:52:48+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
public class PrixOffreVoyageTransformerImpl implements PrixOffreVoyageTransformer {

    @Override
    public PrixOffreVoyageDTO toDto(PrixOffreVoyage entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        PrixOffreVoyageDTO prixOffreVoyageDTO = new PrixOffreVoyageDTO();

        prixOffreVoyageDTO.setId( entity.getId() );
        prixOffreVoyageDTO.setDesignation( entity.getDesignation() );
        prixOffreVoyageDTO.setDescription( entity.getDescription() );
        prixOffreVoyageDTO.setPrix( entity.getPrix() );
        prixOffreVoyageDTO.setModeDesignation( entityModeDesignation( entity ) );
        prixOffreVoyageDTO.setCategorieVoyageurDesignation( entityCategorieVoyageurDesignation( entity ) );
        prixOffreVoyageDTO.setOffreVoyageDesignation( entityOffreVoyageDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            prixOffreVoyageDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            prixOffreVoyageDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            prixOffreVoyageDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        prixOffreVoyageDTO.setUpdatedBy( entity.getUpdatedBy() );
        prixOffreVoyageDTO.setCreatedBy( entity.getCreatedBy() );
        prixOffreVoyageDTO.setDeletedBy( entity.getDeletedBy() );
        prixOffreVoyageDTO.setIsDeleted( entity.getIsDeleted() );

        return prixOffreVoyageDTO;
    }

    @Override
    public List<PrixOffreVoyageDTO> toDtos(List<PrixOffreVoyage> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<PrixOffreVoyageDTO> list = new ArrayList<PrixOffreVoyageDTO>( entities.size() );
        for ( PrixOffreVoyage prixOffreVoyage : entities ) {
            list.add( toDto( prixOffreVoyage ) );
        }

        return list;
    }

    @Override
    public PrixOffreVoyage toEntity(PrixOffreVoyageDTO dto, Reference mode, OffreVoyage offreVoyage, Reference categorieVoyageur) throws ParseException {
        if ( dto == null && mode == null && offreVoyage == null && categorieVoyageur == null ) {
            return null;
        }

        PrixOffreVoyage prixOffreVoyage = new PrixOffreVoyage();

        if ( dto != null ) {
            prixOffreVoyage.setId( dto.getId() );
            prixOffreVoyage.setDesignation( dto.getDesignation() );
            prixOffreVoyage.setDescription( dto.getDescription() );
            prixOffreVoyage.setPrix( dto.getPrix() );
            if ( dto.getUpdatedAt() != null ) {
                prixOffreVoyage.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                prixOffreVoyage.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                prixOffreVoyage.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            prixOffreVoyage.setUpdatedBy( dto.getUpdatedBy() );
            prixOffreVoyage.setCreatedBy( dto.getCreatedBy() );
            prixOffreVoyage.setDeletedBy( dto.getDeletedBy() );
            prixOffreVoyage.setIsDeleted( dto.getIsDeleted() );
        }
        if ( mode != null ) {
            prixOffreVoyage.setMode( mode );
        }
        if ( offreVoyage != null ) {
            prixOffreVoyage.setOffreVoyage( offreVoyage );
        }
        if ( categorieVoyageur != null ) {
            prixOffreVoyage.setCategorieVoyageur( categorieVoyageur );
        }

        return prixOffreVoyage;
    }

    private String entityModeDesignation(PrixOffreVoyage prixOffreVoyage) {
        if ( prixOffreVoyage == null ) {
            return null;
        }
        Reference mode = prixOffreVoyage.getMode();
        if ( mode == null ) {
            return null;
        }
        String designation = mode.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityCategorieVoyageurDesignation(PrixOffreVoyage prixOffreVoyage) {
        if ( prixOffreVoyage == null ) {
            return null;
        }
        Reference categorieVoyageur = prixOffreVoyage.getCategorieVoyageur();
        if ( categorieVoyageur == null ) {
            return null;
        }
        String designation = categorieVoyageur.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityOffreVoyageDesignation(PrixOffreVoyage prixOffreVoyage) {
        if ( prixOffreVoyage == null ) {
            return null;
        }
        OffreVoyage offreVoyage = prixOffreVoyage.getOffreVoyage();
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
