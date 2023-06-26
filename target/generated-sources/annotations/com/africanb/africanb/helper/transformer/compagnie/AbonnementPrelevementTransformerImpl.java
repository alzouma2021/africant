package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPrelevement;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPrelevementDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-26T16:33:49+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class AbonnementPrelevementTransformerImpl implements AbonnementPrelevementTransformer {

    @Override
    public AbonnementPrelevementDTO toDto(AbonnementPrelevement entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        AbonnementPrelevementDTO abonnementPrelevementDTO = new AbonnementPrelevementDTO();

        abonnementPrelevementDTO.setId( entity.getId() );
        abonnementPrelevementDTO.setDesignation( entity.getDesignation() );
        abonnementPrelevementDTO.setDescription( entity.getDescription() );
        if ( entity.getDateDebutAbonnement() != null ) {
            abonnementPrelevementDTO.setDateDebutAbonnement( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDateDebutAbonnement() ) );
        }
        if ( entity.getDateFinAbonnement() != null ) {
            abonnementPrelevementDTO.setDateFinAbonnement( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDateFinAbonnement() ) );
        }
        abonnementPrelevementDTO.setTaux( entity.getTaux() );
        abonnementPrelevementDTO.setCompagnieTransportRaisonSociale( entityCompagnieTransportRaisonSociale( entity ) );
        abonnementPrelevementDTO.setPeriodiciteAbonnementDesignation( entityPeriodiciteAbonnementDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            abonnementPrelevementDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            abonnementPrelevementDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            abonnementPrelevementDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        abonnementPrelevementDTO.setUpdatedBy( entity.getUpdatedBy() );
        abonnementPrelevementDTO.setCreatedBy( entity.getCreatedBy() );
        abonnementPrelevementDTO.setDeletedBy( entity.getDeletedBy() );
        abonnementPrelevementDTO.setIsDeleted( entity.getIsDeleted() );

        return abonnementPrelevementDTO;
    }

    @Override
    public List<AbonnementPrelevementDTO> toDtos(List<AbonnementPrelevement> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<AbonnementPrelevementDTO> list = new ArrayList<AbonnementPrelevementDTO>( entities.size() );
        for ( AbonnementPrelevement abonnementPrelevement : entities ) {
            list.add( toDto( abonnementPrelevement ) );
        }

        return list;
    }

    @Override
    public AbonnementPrelevement toEntity(AbonnementPrelevementDTO dto, CompagnieTransport compagnieTransport, Reference periodiciteAbonnement) throws ParseException {
        if ( dto == null && compagnieTransport == null && periodiciteAbonnement == null ) {
            return null;
        }

        AbonnementPrelevement abonnementPrelevement = new AbonnementPrelevement();

        if ( dto != null ) {
            abonnementPrelevement.setId( dto.getId() );
            abonnementPrelevement.setDesignation( dto.getDesignation() );
            abonnementPrelevement.setDescription( dto.getDescription() );
            if ( dto.getDateDebutAbonnement() != null ) {
                abonnementPrelevement.setDateDebutAbonnement( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDateDebutAbonnement() ) );
            }
            if ( dto.getDateFinAbonnement() != null ) {
                abonnementPrelevement.setDateFinAbonnement( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDateFinAbonnement() ) );
            }
            abonnementPrelevement.setTaux( dto.getTaux() );
            if ( dto.getUpdatedAt() != null ) {
                abonnementPrelevement.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                abonnementPrelevement.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                abonnementPrelevement.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            abonnementPrelevement.setUpdatedBy( dto.getUpdatedBy() );
            abonnementPrelevement.setCreatedBy( dto.getCreatedBy() );
            abonnementPrelevement.setDeletedBy( dto.getDeletedBy() );
            abonnementPrelevement.setIsDeleted( dto.getIsDeleted() );
        }
        if ( compagnieTransport != null ) {
            abonnementPrelevement.setCompagnieTransport( compagnieTransport );
        }
        if ( periodiciteAbonnement != null ) {
            abonnementPrelevement.setPeriodiciteAbonnement( periodiciteAbonnement );
        }

        return abonnementPrelevement;
    }

    private String entityCompagnieTransportRaisonSociale(AbonnementPrelevement abonnementPrelevement) {
        if ( abonnementPrelevement == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = abonnementPrelevement.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        String raisonSociale = compagnieTransport.getRaisonSociale();
        if ( raisonSociale == null ) {
            return null;
        }
        return raisonSociale;
    }

    private String entityPeriodiciteAbonnementDesignation(AbonnementPrelevement abonnementPrelevement) {
        if ( abonnementPrelevement == null ) {
            return null;
        }
        Reference periodiciteAbonnement = abonnementPrelevement.getPeriodiciteAbonnement();
        if ( periodiciteAbonnement == null ) {
            return null;
        }
        String designation = periodiciteAbonnement.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
