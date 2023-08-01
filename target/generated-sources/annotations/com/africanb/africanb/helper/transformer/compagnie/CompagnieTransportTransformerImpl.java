package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.helper.dto.compagnie.CompagnieTransportDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-01T19:47:29+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class CompagnieTransportTransformerImpl implements CompagnieTransportTransformer {

    @Override
    public CompagnieTransportDTO toDto(CompagnieTransport entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        CompagnieTransportDTO compagnieTransportDTO = new CompagnieTransportDTO();

        compagnieTransportDTO.setId( entity.getId() );
        compagnieTransportDTO.setDesignation( entity.getDesignation() );
        compagnieTransportDTO.setDescription( entity.getDescription() );
        compagnieTransportDTO.setIsActif( entity.getIsActif() );
        compagnieTransportDTO.setIsValidate( entity.getIsValidate() );
        compagnieTransportDTO.setRaisonSociale( entity.getRaisonSociale() );
        compagnieTransportDTO.setTelephone( entity.getTelephone() );
        compagnieTransportDTO.setSigle( entity.getSigle() );
        compagnieTransportDTO.setEmail( entity.getEmail() );
        compagnieTransportDTO.setStatusUtilActualId( entityStatusUtilActualId( entity ) );
        compagnieTransportDTO.setStatusUtilActualDesignation( entityStatusUtilActualDesignation( entity ) );
        compagnieTransportDTO.setVilleId( entityVilleId( entity ) );
        compagnieTransportDTO.setVilleDesignation( entityVilleDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            compagnieTransportDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            compagnieTransportDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            compagnieTransportDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        compagnieTransportDTO.setUpdatedBy( entity.getUpdatedBy() );
        compagnieTransportDTO.setCreatedBy( entity.getCreatedBy() );
        compagnieTransportDTO.setDeletedBy( entity.getDeletedBy() );
        compagnieTransportDTO.setIsDeleted( entity.getIsDeleted() );

        return compagnieTransportDTO;
    }

    @Override
    public List<CompagnieTransportDTO> toDtos(List<CompagnieTransport> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<CompagnieTransportDTO> list = new ArrayList<CompagnieTransportDTO>( entities.size() );
        for ( CompagnieTransport compagnieTransport : entities ) {
            list.add( toDto( compagnieTransport ) );
        }

        return list;
    }

    @Override
    public CompagnieTransport toEntity(CompagnieTransportDTO dto, Ville ville, StatusUtil statusUtilActual) throws ParseException {
        if ( dto == null && ville == null && statusUtilActual == null ) {
            return null;
        }

        CompagnieTransport compagnieTransport = new CompagnieTransport();

        if ( dto != null ) {
            compagnieTransport.setId( dto.getId() );
            compagnieTransport.setDesignation( dto.getDesignation() );
            compagnieTransport.setDescription( dto.getDescription() );
            compagnieTransport.setIsActif( dto.getIsActif() );
            compagnieTransport.setIsValidate( dto.getIsValidate() );
            compagnieTransport.setRaisonSociale( dto.getRaisonSociale() );
            compagnieTransport.setTelephone( dto.getTelephone() );
            compagnieTransport.setSigle( dto.getSigle() );
            compagnieTransport.setEmail( dto.getEmail() );
            if ( dto.getUpdatedAt() != null ) {
                compagnieTransport.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                compagnieTransport.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                compagnieTransport.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            compagnieTransport.setUpdatedBy( dto.getUpdatedBy() );
            compagnieTransport.setCreatedBy( dto.getCreatedBy() );
            compagnieTransport.setDeletedBy( dto.getDeletedBy() );
            compagnieTransport.setIsDeleted( dto.getIsDeleted() );
        }
        if ( ville != null ) {
            compagnieTransport.setVille( ville );
        }
        if ( statusUtilActual != null ) {
            compagnieTransport.setStatusUtilActual( statusUtilActual );
        }

        return compagnieTransport;
    }

    private Long entityStatusUtilActualId(CompagnieTransport compagnieTransport) {
        if ( compagnieTransport == null ) {
            return null;
        }
        StatusUtil statusUtilActual = compagnieTransport.getStatusUtilActual();
        if ( statusUtilActual == null ) {
            return null;
        }
        Long id = statusUtilActual.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityStatusUtilActualDesignation(CompagnieTransport compagnieTransport) {
        if ( compagnieTransport == null ) {
            return null;
        }
        StatusUtil statusUtilActual = compagnieTransport.getStatusUtilActual();
        if ( statusUtilActual == null ) {
            return null;
        }
        String designation = statusUtilActual.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private Long entityVilleId(CompagnieTransport compagnieTransport) {
        if ( compagnieTransport == null ) {
            return null;
        }
        Ville ville = compagnieTransport.getVille();
        if ( ville == null ) {
            return null;
        }
        Long id = ville.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityVilleDesignation(CompagnieTransport compagnieTransport) {
        if ( compagnieTransport == null ) {
            return null;
        }
        Ville ville = compagnieTransport.getVille();
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
