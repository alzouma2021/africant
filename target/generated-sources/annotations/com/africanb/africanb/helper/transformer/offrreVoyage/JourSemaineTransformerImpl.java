package com.africanb.africanb.helper.transformer.offrreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.JourSemaine;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.helper.dto.offreVoyage.JourSemaineDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-10T10:49:37+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class JourSemaineTransformerImpl implements JourSemaineTransformer {

    @Override
    public JourSemaineDTO toDto(JourSemaine entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        JourSemaineDTO jourSemaineDTO = new JourSemaineDTO();

        jourSemaineDTO.setId( entity.getId() );
        jourSemaineDTO.setDesignation( entity.getDesignation() );
        jourSemaineDTO.setDescription( entity.getDescription() );
        jourSemaineDTO.setJourSemaineDesignation( entityJourSemaineDesignation( entity ) );
        jourSemaineDTO.setOffreVoyageDesignation( entityOffreVoyageDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            jourSemaineDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            jourSemaineDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            jourSemaineDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        jourSemaineDTO.setUpdatedBy( entity.getUpdatedBy() );
        jourSemaineDTO.setCreatedBy( entity.getCreatedBy() );
        jourSemaineDTO.setDeletedBy( entity.getDeletedBy() );
        jourSemaineDTO.setIsDeleted( entity.getIsDeleted() );

        return jourSemaineDTO;
    }

    @Override
    public List<JourSemaineDTO> toDtos(List<JourSemaine> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<JourSemaineDTO> list = new ArrayList<JourSemaineDTO>( entities.size() );
        for ( JourSemaine jourSemaine : entities ) {
            list.add( toDto( jourSemaine ) );
        }

        return list;
    }

    @Override
    public JourSemaine toEntity(JourSemaineDTO dto, Reference jourSemaine, OffreVoyage offreVoyage) throws ParseException {
        if ( dto == null && jourSemaine == null && offreVoyage == null ) {
            return null;
        }

        JourSemaine jourSemaine1 = new JourSemaine();

        if ( dto != null ) {
            jourSemaine1.setId( dto.getId() );
            jourSemaine1.setDesignation( dto.getDesignation() );
            jourSemaine1.setDescription( dto.getDescription() );
            if ( dto.getUpdatedAt() != null ) {
                jourSemaine1.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                jourSemaine1.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                jourSemaine1.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            jourSemaine1.setUpdatedBy( dto.getUpdatedBy() );
            jourSemaine1.setCreatedBy( dto.getCreatedBy() );
            jourSemaine1.setDeletedBy( dto.getDeletedBy() );
            jourSemaine1.setIsDeleted( dto.getIsDeleted() );
        }
        if ( jourSemaine != null ) {
            jourSemaine1.setJourSemaine( jourSemaine );
        }
        if ( offreVoyage != null ) {
            jourSemaine1.setOffreVoyage( offreVoyage );
        }

        return jourSemaine1;
    }

    private String entityJourSemaineDesignation(JourSemaine jourSemaine) {
        if ( jourSemaine == null ) {
            return null;
        }
        Reference jourSemaine1 = jourSemaine.getJourSemaine();
        if ( jourSemaine1 == null ) {
            return null;
        }
        String designation = jourSemaine1.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityOffreVoyageDesignation(JourSemaine jourSemaine) {
        if ( jourSemaine == null ) {
            return null;
        }
        OffreVoyage offreVoyage = jourSemaine.getOffreVoyage();
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
