package com.africanb.africanb.helper.transformer.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementWave;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementWaveDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-13T11:29:41+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ModePaimentWaveTransformerImpl implements ModePaimentWaveTransformer {

    @Override
    public ModePaiementWaveDTO toDto(ModePaiementWave entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ModePaiementWaveDTO modePaiementWaveDTO = new ModePaiementWaveDTO();

        modePaiementWaveDTO.setId( entity.getId() );
        modePaiementWaveDTO.setDesignation( entity.getDesignation() );
        modePaiementWaveDTO.setDescription( entity.getDescription() );
        modePaiementWaveDTO.setTelephoneWave( entity.getTelephoneWave() );
        modePaiementWaveDTO.setCompagnieTransportRaisonSociale( entityCompagnieTransportRaisonSociale( entity ) );
        modePaiementWaveDTO.setTypeModePaiementDesignation( entityTypeModePaiementDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            modePaiementWaveDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            modePaiementWaveDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            modePaiementWaveDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        modePaiementWaveDTO.setUpdatedBy( entity.getUpdatedBy() );
        modePaiementWaveDTO.setCreatedBy( entity.getCreatedBy() );
        modePaiementWaveDTO.setDeletedBy( entity.getDeletedBy() );
        modePaiementWaveDTO.setIsDeleted( entity.getIsDeleted() );

        return modePaiementWaveDTO;
    }

    @Override
    public List<ModePaiementWaveDTO> toDtos(List<ModePaiementWave> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ModePaiementWaveDTO> list = new ArrayList<ModePaiementWaveDTO>( entities.size() );
        for ( ModePaiementWave modePaiementWave : entities ) {
            list.add( toDto( modePaiementWave ) );
        }

        return list;
    }

    @Override
    public ModePaiementWave toEntity(ModePaiementWaveDTO dto, CompagnieTransport compagnieTransport, Reference typeModePaiement) throws ParseException {
        if ( dto == null && compagnieTransport == null && typeModePaiement == null ) {
            return null;
        }

        ModePaiementWave modePaiementWave = new ModePaiementWave();

        if ( dto != null ) {
            modePaiementWave.setId( dto.getId() );
            modePaiementWave.setDesignation( dto.getDesignation() );
            modePaiementWave.setDescription( dto.getDescription() );
            modePaiementWave.setTelephoneWave( dto.getTelephoneWave() );
            if ( dto.getUpdatedAt() != null ) {
                modePaiementWave.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                modePaiementWave.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                modePaiementWave.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            modePaiementWave.setUpdatedBy( dto.getUpdatedBy() );
            modePaiementWave.setCreatedBy( dto.getCreatedBy() );
            modePaiementWave.setDeletedBy( dto.getDeletedBy() );
            modePaiementWave.setIsDeleted( dto.getIsDeleted() );
        }
        if ( compagnieTransport != null ) {
            modePaiementWave.setCompagnieTransport( compagnieTransport );
        }
        if ( typeModePaiement != null ) {
            modePaiementWave.setTypeModePaiement( typeModePaiement );
        }

        return modePaiementWave;
    }

    private String entityCompagnieTransportRaisonSociale(ModePaiementWave modePaiementWave) {
        if ( modePaiementWave == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = modePaiementWave.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        String raisonSociale = compagnieTransport.getRaisonSociale();
        if ( raisonSociale == null ) {
            return null;
        }
        return raisonSociale;
    }

    private String entityTypeModePaiementDesignation(ModePaiementWave modePaiementWave) {
        if ( modePaiementWave == null ) {
            return null;
        }
        Reference typeModePaiement = modePaiementWave.getTypeModePaiement();
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
