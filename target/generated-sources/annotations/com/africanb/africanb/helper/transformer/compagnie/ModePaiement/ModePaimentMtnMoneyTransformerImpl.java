package com.africanb.africanb.helper.transformer.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementMtnMoney;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementMtnMoneyDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-10T18:23:49+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ModePaimentMtnMoneyTransformerImpl implements ModePaimentMtnMoneyTransformer {

    @Override
    public ModePaiementMtnMoneyDTO toDto(ModePaiementMtnMoney entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ModePaiementMtnMoneyDTO modePaiementMtnMoneyDTO = new ModePaiementMtnMoneyDTO();

        modePaiementMtnMoneyDTO.setId( entity.getId() );
        modePaiementMtnMoneyDTO.setDesignation( entity.getDesignation() );
        modePaiementMtnMoneyDTO.setDescription( entity.getDescription() );
        modePaiementMtnMoneyDTO.setTelephoneMtnMoney( entity.getTelephoneMtnMoney() );
        modePaiementMtnMoneyDTO.setCompagnieTransportRaisonSociale( entityCompagnieTransportRaisonSociale( entity ) );
        modePaiementMtnMoneyDTO.setTypeModePaiementDesignation( entityTypeModePaiementDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            modePaiementMtnMoneyDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            modePaiementMtnMoneyDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            modePaiementMtnMoneyDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        modePaiementMtnMoneyDTO.setUpdatedBy( entity.getUpdatedBy() );
        modePaiementMtnMoneyDTO.setCreatedBy( entity.getCreatedBy() );
        modePaiementMtnMoneyDTO.setDeletedBy( entity.getDeletedBy() );
        modePaiementMtnMoneyDTO.setIsDeleted( entity.getIsDeleted() );

        return modePaiementMtnMoneyDTO;
    }

    @Override
    public List<ModePaiementMtnMoneyDTO> toDtos(List<ModePaiementMtnMoney> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ModePaiementMtnMoneyDTO> list = new ArrayList<ModePaiementMtnMoneyDTO>( entities.size() );
        for ( ModePaiementMtnMoney modePaiementMtnMoney : entities ) {
            list.add( toDto( modePaiementMtnMoney ) );
        }

        return list;
    }

    @Override
    public ModePaiementMtnMoney toEntity(ModePaiementMtnMoneyDTO dto, CompagnieTransport compagnieTransport, Reference typeModePaiement) throws ParseException {
        if ( dto == null && compagnieTransport == null && typeModePaiement == null ) {
            return null;
        }

        ModePaiementMtnMoney modePaiementMtnMoney = new ModePaiementMtnMoney();

        if ( dto != null ) {
            modePaiementMtnMoney.setId( dto.getId() );
            modePaiementMtnMoney.setDesignation( dto.getDesignation() );
            modePaiementMtnMoney.setDescription( dto.getDescription() );
            modePaiementMtnMoney.setTelephoneMtnMoney( dto.getTelephoneMtnMoney() );
            if ( dto.getUpdatedAt() != null ) {
                modePaiementMtnMoney.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                modePaiementMtnMoney.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                modePaiementMtnMoney.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            modePaiementMtnMoney.setUpdatedBy( dto.getUpdatedBy() );
            modePaiementMtnMoney.setCreatedBy( dto.getCreatedBy() );
            modePaiementMtnMoney.setDeletedBy( dto.getDeletedBy() );
            modePaiementMtnMoney.setIsDeleted( dto.getIsDeleted() );
        }
        if ( compagnieTransport != null ) {
            modePaiementMtnMoney.setCompagnieTransport( compagnieTransport );
        }
        if ( typeModePaiement != null ) {
            modePaiementMtnMoney.setTypeModePaiement( typeModePaiement );
        }

        return modePaiementMtnMoney;
    }

    private String entityCompagnieTransportRaisonSociale(ModePaiementMtnMoney modePaiementMtnMoney) {
        if ( modePaiementMtnMoney == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = modePaiementMtnMoney.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        String raisonSociale = compagnieTransport.getRaisonSociale();
        if ( raisonSociale == null ) {
            return null;
        }
        return raisonSociale;
    }

    private String entityTypeModePaiementDesignation(ModePaiementMtnMoney modePaiementMtnMoney) {
        if ( modePaiementMtnMoney == null ) {
            return null;
        }
        Reference typeModePaiement = modePaiementMtnMoney.getTypeModePaiement();
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
