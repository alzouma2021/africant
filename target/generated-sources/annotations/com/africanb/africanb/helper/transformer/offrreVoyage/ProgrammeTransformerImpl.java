package com.africanb.africanb.helper.transformer.offrreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.JourSemaine;
import com.africanb.africanb.dao.entity.offreVoyage.Programme;
import com.africanb.africanb.helper.dto.offreVoyage.ProgrammeDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-14T15:57:50+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ProgrammeTransformerImpl implements ProgrammeTransformer {

    @Override
    public ProgrammeDTO toDto(Programme entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ProgrammeDTO programmeDTO = new ProgrammeDTO();

        programmeDTO.setId( entity.getId() );
        programmeDTO.setDesignation( entity.getDesignation() );
        programmeDTO.setDescription( entity.getDescription() );
        programmeDTO.setNombrePlaceDisponible( entity.getNombrePlaceDisponible() );
        if ( entity.getDateDepart() != null ) {
            programmeDTO.setDateDepart( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDateDepart() ) );
        }
        if ( entity.getDateArrivee() != null ) {
            programmeDTO.setDateArrivee( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDateArrivee() ) );
        }
        programmeDTO.setHeureDepart( entity.getHeureDepart() );
        programmeDTO.setHeureArrivee( entity.getHeureArrivee() );
        programmeDTO.setJourSemaineDesignation( entityJourSemaineDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            programmeDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            programmeDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            programmeDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        programmeDTO.setUpdatedBy( entity.getUpdatedBy() );
        programmeDTO.setCreatedBy( entity.getCreatedBy() );
        programmeDTO.setDeletedBy( entity.getDeletedBy() );
        programmeDTO.setIsDeleted( entity.getIsDeleted() );

        return programmeDTO;
    }

    @Override
    public List<ProgrammeDTO> toDtos(List<Programme> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ProgrammeDTO> list = new ArrayList<ProgrammeDTO>( entities.size() );
        for ( Programme programme : entities ) {
            list.add( toDto( programme ) );
        }

        return list;
    }

    @Override
    public Programme toEntity(ProgrammeDTO dto, JourSemaine jourSemaine) throws ParseException {
        if ( dto == null && jourSemaine == null ) {
            return null;
        }

        Programme programme = new Programme();

        if ( dto != null ) {
            programme.setId( dto.getId() );
            programme.setDesignation( dto.getDesignation() );
            programme.setDescription( dto.getDescription() );
            programme.setNombrePlaceDisponible( dto.getNombrePlaceDisponible() );
            if ( dto.getDateDepart() != null ) {
                programme.setDateDepart( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDateDepart() ) );
            }
            if ( dto.getDateArrivee() != null ) {
                programme.setDateArrivee( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDateArrivee() ) );
            }
            programme.setHeureDepart( dto.getHeureDepart() );
            programme.setHeureArrivee( dto.getHeureArrivee() );
            if ( dto.getUpdatedAt() != null ) {
                programme.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                programme.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                programme.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            programme.setUpdatedBy( dto.getUpdatedBy() );
            programme.setCreatedBy( dto.getCreatedBy() );
            programme.setDeletedBy( dto.getDeletedBy() );
            programme.setIsDeleted( dto.getIsDeleted() );
        }
        if ( jourSemaine != null ) {
            programme.setJourSemaine( jourSemaine );
        }

        return programme;
    }

    private String entityJourSemaineDesignation(Programme programme) {
        if ( programme == null ) {
            return null;
        }
        JourSemaine jourSemaine = programme.getJourSemaine();
        if ( jourSemaine == null ) {
            return null;
        }
        String designation = jourSemaine.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
