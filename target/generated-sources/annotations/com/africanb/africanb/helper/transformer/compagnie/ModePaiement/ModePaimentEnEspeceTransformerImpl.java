package com.africanb.africanb.helper.transformer.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementEnEspece;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementEnEspeceDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-03T12:04:38+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ModePaimentEnEspeceTransformerImpl implements ModePaimentEnEspeceTransformer {

    @Override
    public ModePaiementEnEspeceDTO toDto(ModePaiementEnEspece entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ModePaiementEnEspeceDTO modePaiementEnEspeceDTO = new ModePaiementEnEspeceDTO();

        modePaiementEnEspeceDTO.setId( entity.getId() );
        modePaiementEnEspeceDTO.setDesignation( entity.getDesignation() );
        modePaiementEnEspeceDTO.setDescription( entity.getDescription() );
        modePaiementEnEspeceDTO.setCompagnieTransportRaisonSociale( entityCompagnieTransportRaisonSociale( entity ) );
        modePaiementEnEspeceDTO.setTypeModePaiementDesignation( entityTypeModePaiementDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            modePaiementEnEspeceDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            modePaiementEnEspeceDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            modePaiementEnEspeceDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        modePaiementEnEspeceDTO.setUpdatedBy( entity.getUpdatedBy() );
        modePaiementEnEspeceDTO.setCreatedBy( entity.getCreatedBy() );
        modePaiementEnEspeceDTO.setDeletedBy( entity.getDeletedBy() );
        modePaiementEnEspeceDTO.setIsDeleted( entity.getIsDeleted() );

        return modePaiementEnEspeceDTO;
    }

    @Override
    public List<ModePaiementEnEspeceDTO> toDtos(List<ModePaiementEnEspece> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ModePaiementEnEspeceDTO> list = new ArrayList<ModePaiementEnEspeceDTO>( entities.size() );
        for ( ModePaiementEnEspece modePaiementEnEspece : entities ) {
            list.add( toDto( modePaiementEnEspece ) );
        }

        return list;
    }

    @Override
    public ModePaiementEnEspece toEntity(ModePaiementEnEspeceDTO dto, CompagnieTransport compagnieTransport, Reference typeModePaiement) throws ParseException {
        if ( dto == null && compagnieTransport == null && typeModePaiement == null ) {
            return null;
        }

        ModePaiementEnEspece modePaiementEnEspece = new ModePaiementEnEspece();

        if ( dto != null ) {
            modePaiementEnEspece.setId( dto.getId() );
            modePaiementEnEspece.setDesignation( dto.getDesignation() );
            modePaiementEnEspece.setDescription( dto.getDescription() );
            if ( dto.getUpdatedAt() != null ) {
                modePaiementEnEspece.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                modePaiementEnEspece.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                modePaiementEnEspece.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            modePaiementEnEspece.setUpdatedBy( dto.getUpdatedBy() );
            modePaiementEnEspece.setCreatedBy( dto.getCreatedBy() );
            modePaiementEnEspece.setDeletedBy( dto.getDeletedBy() );
            modePaiementEnEspece.setIsDeleted( dto.getIsDeleted() );
        }
        if ( compagnieTransport != null ) {
            modePaiementEnEspece.setCompagnieTransport( compagnieTransport );
        }
        if ( typeModePaiement != null ) {
            modePaiementEnEspece.setTypeModePaiement( typeModePaiement );
        }

        return modePaiementEnEspece;
    }

    private String entityCompagnieTransportRaisonSociale(ModePaiementEnEspece modePaiementEnEspece) {
        if ( modePaiementEnEspece == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = modePaiementEnEspece.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        String raisonSociale = compagnieTransport.getRaisonSociale();
        if ( raisonSociale == null ) {
            return null;
        }
        return raisonSociale;
    }

    private String entityTypeModePaiementDesignation(ModePaiementEnEspece modePaiementEnEspece) {
        if ( modePaiementEnEspece == null ) {
            return null;
        }
        Reference typeModePaiement = modePaiementEnEspece.getTypeModePaiement();
        if ( typeModePaiement == null ) {
            return null;
        }
        String designation = typeModePaiement.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
