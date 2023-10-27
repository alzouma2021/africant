package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.Gare;
import com.africanb.africanb.helper.dto.compagnie.GareDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-27T12:05:18+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class GareTransformerImpl implements GareTransformer {

    @Override
    public GareDTO toDto(Gare entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        GareDTO gareDTO = new GareDTO();

        gareDTO.setId( entity.getId() );
        gareDTO.setDesignation( entity.getDesignation() );
        gareDTO.setDescription( entity.getDescription() );
        gareDTO.setTelephone1( entity.getTelephone1() );
        gareDTO.setTelephone2( entity.getTelephone2() );
        gareDTO.setEmail( entity.getEmail() );
        gareDTO.setAdresseLocalisation( entity.getAdresseLocalisation() );
        gareDTO.setCompagnieTransportRaisonSociale( entityCompagnieTransportRaisonSociale( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            gareDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            gareDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            gareDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        gareDTO.setUpdatedBy( entity.getUpdatedBy() );
        gareDTO.setCreatedBy( entity.getCreatedBy() );
        gareDTO.setDeletedBy( entity.getDeletedBy() );
        gareDTO.setIsDeleted( entity.getIsDeleted() );

        return gareDTO;
    }

    @Override
    public List<GareDTO> toDtos(List<Gare> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<GareDTO> list = new ArrayList<GareDTO>( entities.size() );
        for ( Gare gare : entities ) {
            list.add( toDto( gare ) );
        }

        return list;
    }

    @Override
    public Gare toEntity(GareDTO dto, CompagnieTransport compagnieTransport) throws ParseException {
        if ( dto == null && compagnieTransport == null ) {
            return null;
        }

        Gare gare = new Gare();

        if ( dto != null ) {
            gare.setId( dto.getId() );
            gare.setDesignation( dto.getDesignation() );
            gare.setDescription( dto.getDescription() );
            gare.setTelephone1( dto.getTelephone1() );
            gare.setTelephone2( dto.getTelephone2() );
            gare.setEmail( dto.getEmail() );
            gare.setAdresseLocalisation( dto.getAdresseLocalisation() );
            if ( dto.getUpdatedAt() != null ) {
                gare.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                gare.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                gare.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            gare.setUpdatedBy( dto.getUpdatedBy() );
            gare.setCreatedBy( dto.getCreatedBy() );
            gare.setDeletedBy( dto.getDeletedBy() );
            gare.setIsDeleted( dto.getIsDeleted() );
        }
        if ( compagnieTransport != null ) {
            gare.setCompagnieTransport( compagnieTransport );
        }

        return gare;
    }

    private String entityCompagnieTransportRaisonSociale(Gare gare) {
        if ( gare == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = gare.getCompagnieTransport();
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
