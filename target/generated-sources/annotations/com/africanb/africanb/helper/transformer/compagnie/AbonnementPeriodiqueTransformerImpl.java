package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPeriodique;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPeriodiqueDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-01T10:52:47+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
public class AbonnementPeriodiqueTransformerImpl implements AbonnementPeriodiqueTransformer {

    @Override
    public AbonnementPeriodiqueDTO toDto(AbonnementPeriodique entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        AbonnementPeriodiqueDTO abonnementPeriodiqueDTO = new AbonnementPeriodiqueDTO();

        abonnementPeriodiqueDTO.setId( entity.getId() );
        abonnementPeriodiqueDTO.setDesignation( entity.getDesignation() );
        abonnementPeriodiqueDTO.setDescription( entity.getDescription() );
        if ( entity.getDateDebutAbonnement() != null ) {
            abonnementPeriodiqueDTO.setDateDebutAbonnement( new SimpleDateFormat().format( entity.getDateDebutAbonnement() ) );
        }
        if ( entity.getDateFinAbonnement() != null ) {
            abonnementPeriodiqueDTO.setDateFinAbonnement( new SimpleDateFormat().format( entity.getDateFinAbonnement() ) );
        }
        abonnementPeriodiqueDTO.setRedevance( entity.getRedevance() );
        abonnementPeriodiqueDTO.setRedevancePublicite( entity.getRedevancePublicite() );
        abonnementPeriodiqueDTO.setCompagnieTransportRaisonSociale( entityCompagnieTransportRaisonSociale( entity ) );
        abonnementPeriodiqueDTO.setPeriodiciteAbonnementDesignation( entityPeriodiciteAbonnementDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            abonnementPeriodiqueDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            abonnementPeriodiqueDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            abonnementPeriodiqueDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        abonnementPeriodiqueDTO.setUpdatedBy( entity.getUpdatedBy() );
        abonnementPeriodiqueDTO.setCreatedBy( entity.getCreatedBy() );
        abonnementPeriodiqueDTO.setDeletedBy( entity.getDeletedBy() );
        abonnementPeriodiqueDTO.setIsDeleted( entity.getIsDeleted() );

        return abonnementPeriodiqueDTO;
    }

    @Override
    public List<AbonnementPeriodiqueDTO> toDtos(List<AbonnementPeriodique> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<AbonnementPeriodiqueDTO> list = new ArrayList<AbonnementPeriodiqueDTO>( entities.size() );
        for ( AbonnementPeriodique abonnementPeriodique : entities ) {
            list.add( toDto( abonnementPeriodique ) );
        }

        return list;
    }

    @Override
    public AbonnementPeriodique toEntity(AbonnementPeriodiqueDTO dto, CompagnieTransport compagnieTransport, Reference periodiciteAbonnement) throws ParseException {
        if ( dto == null && compagnieTransport == null && periodiciteAbonnement == null ) {
            return null;
        }

        AbonnementPeriodique abonnementPeriodique = new AbonnementPeriodique();

        if ( dto != null ) {
            abonnementPeriodique.setId( dto.getId() );
            abonnementPeriodique.setDesignation( dto.getDesignation() );
            abonnementPeriodique.setDescription( dto.getDescription() );
            if ( dto.getDateDebutAbonnement() != null ) {
                abonnementPeriodique.setDateDebutAbonnement( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDateDebutAbonnement() ) );
            }
            if ( dto.getDateFinAbonnement() != null ) {
                abonnementPeriodique.setDateFinAbonnement( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDateFinAbonnement() ) );
            }
            abonnementPeriodique.setRedevance( dto.getRedevance() );
            abonnementPeriodique.setRedevancePublicite( dto.getRedevancePublicite() );
            if ( dto.getUpdatedAt() != null ) {
                abonnementPeriodique.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                abonnementPeriodique.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                abonnementPeriodique.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            abonnementPeriodique.setUpdatedBy( dto.getUpdatedBy() );
            abonnementPeriodique.setCreatedBy( dto.getCreatedBy() );
            abonnementPeriodique.setDeletedBy( dto.getDeletedBy() );
            abonnementPeriodique.setIsDeleted( dto.getIsDeleted() );
        }
        if ( compagnieTransport != null ) {
            abonnementPeriodique.setCompagnieTransport( compagnieTransport );
        }
        if ( periodiciteAbonnement != null ) {
            abonnementPeriodique.setPeriodiciteAbonnement( periodiciteAbonnement );
        }

        return abonnementPeriodique;
    }

    private String entityCompagnieTransportRaisonSociale(AbonnementPeriodique abonnementPeriodique) {
        if ( abonnementPeriodique == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = abonnementPeriodique.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        String raisonSociale = compagnieTransport.getRaisonSociale();
        if ( raisonSociale == null ) {
            return null;
        }
        return raisonSociale;
    }

    private String entityPeriodiciteAbonnementDesignation(AbonnementPeriodique abonnementPeriodique) {
        if ( abonnementPeriodique == null ) {
            return null;
        }
        Reference periodiciteAbonnement = abonnementPeriodique.getPeriodiciteAbonnement();
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
