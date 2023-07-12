package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieAttestionTransport;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.document.Document;
import com.africanb.africanb.helper.dto.compagnie.CompagnieAttestionTransportDTO;
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
public class CompagnieAttestionTransportTransformerImpl implements CompagnieAttestionTransportTransformer {

    @Override
    public CompagnieAttestionTransportDTO toDto(CompagnieAttestionTransport entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        CompagnieAttestionTransportDTO compagnieAttestionTransportDTO = new CompagnieAttestionTransportDTO();

        compagnieAttestionTransportDTO.setId( entity.getId() );
        compagnieAttestionTransportDTO.setCompagnieRaisonSociale( entityCompagnieRaisonSociale( entity ) );
        compagnieAttestionTransportDTO.setDocumentDesignation( entityAttestionTransportDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            compagnieAttestionTransportDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            compagnieAttestionTransportDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            compagnieAttestionTransportDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        compagnieAttestionTransportDTO.setUpdatedBy( entity.getUpdatedBy() );
        compagnieAttestionTransportDTO.setCreatedBy( entity.getCreatedBy() );
        compagnieAttestionTransportDTO.setDeletedBy( entity.getDeletedBy() );
        compagnieAttestionTransportDTO.setIsDeleted( entity.getIsDeleted() );

        return compagnieAttestionTransportDTO;
    }

    @Override
    public List<CompagnieAttestionTransportDTO> toDtos(List<CompagnieAttestionTransport> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<CompagnieAttestionTransportDTO> list = new ArrayList<CompagnieAttestionTransportDTO>( entities.size() );
        for ( CompagnieAttestionTransport compagnieAttestionTransport : entities ) {
            list.add( toDto( compagnieAttestionTransport ) );
        }

        return list;
    }

    @Override
    public CompagnieAttestionTransport toEntity(CompagnieAttestionTransportDTO dto, CompagnieTransport compagnie, Document attestionTransport) throws ParseException {
        if ( dto == null && compagnie == null && attestionTransport == null ) {
            return null;
        }

        CompagnieAttestionTransport compagnieAttestionTransport = new CompagnieAttestionTransport();

        if ( dto != null ) {
            compagnieAttestionTransport.setId( dto.getId() );
            if ( dto.getUpdatedAt() != null ) {
                compagnieAttestionTransport.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                compagnieAttestionTransport.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                compagnieAttestionTransport.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            compagnieAttestionTransport.setUpdatedBy( dto.getUpdatedBy() );
            compagnieAttestionTransport.setCreatedBy( dto.getCreatedBy() );
            compagnieAttestionTransport.setDeletedBy( dto.getDeletedBy() );
            compagnieAttestionTransport.setIsDeleted( dto.getIsDeleted() );
        }
        if ( compagnie != null ) {
            compagnieAttestionTransport.setCompagnie( compagnie );
        }
        if ( attestionTransport != null ) {
            compagnieAttestionTransport.setAttestionTransport( attestionTransport );
        }

        return compagnieAttestionTransport;
    }

    private String entityCompagnieRaisonSociale(CompagnieAttestionTransport compagnieAttestionTransport) {
        if ( compagnieAttestionTransport == null ) {
            return null;
        }
        CompagnieTransport compagnie = compagnieAttestionTransport.getCompagnie();
        if ( compagnie == null ) {
            return null;
        }
        String raisonSociale = compagnie.getRaisonSociale();
        if ( raisonSociale == null ) {
            return null;
        }
        return raisonSociale;
    }

    private String entityAttestionTransportDesignation(CompagnieAttestionTransport compagnieAttestionTransport) {
        if ( compagnieAttestionTransport == null ) {
            return null;
        }
        Document attestionTransport = compagnieAttestionTransport.getAttestionTransport();
        if ( attestionTransport == null ) {
            return null;
        }
        String designation = attestionTransport.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
