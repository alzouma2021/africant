package com.africanb.africanb.helper.transformer.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementMoovMoney;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementMoovMoneyDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-10T16:54:58+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ModePaimentMoovMoneyTransformerImpl implements ModePaimentMoovMoneyTransformer {

    @Override
    public ModePaiementMoovMoneyDTO toDto(ModePaiementMoovMoney entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ModePaiementMoovMoneyDTO modePaiementMoovMoneyDTO = new ModePaiementMoovMoneyDTO();

        modePaiementMoovMoneyDTO.setId( entity.getId() );
        modePaiementMoovMoneyDTO.setDesignation( entity.getDesignation() );
        modePaiementMoovMoneyDTO.setDescription( entity.getDescription() );
        modePaiementMoovMoneyDTO.setTelephoneMoovMoney( entity.getTelephoneMoovMoney() );
        modePaiementMoovMoneyDTO.setCompagnieTransportRaisonSociale( entityCompagnieTransportRaisonSociale( entity ) );
        modePaiementMoovMoneyDTO.setTypeModePaiementDesignation( entityTypeModePaiementDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            modePaiementMoovMoneyDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            modePaiementMoovMoneyDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            modePaiementMoovMoneyDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        modePaiementMoovMoneyDTO.setUpdatedBy( entity.getUpdatedBy() );
        modePaiementMoovMoneyDTO.setCreatedBy( entity.getCreatedBy() );
        modePaiementMoovMoneyDTO.setDeletedBy( entity.getDeletedBy() );
        modePaiementMoovMoneyDTO.setIsDeleted( entity.getIsDeleted() );

        return modePaiementMoovMoneyDTO;
    }

    @Override
    public List<ModePaiementMoovMoneyDTO> toDtos(List<ModePaiementMoovMoney> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ModePaiementMoovMoneyDTO> list = new ArrayList<ModePaiementMoovMoneyDTO>( entities.size() );
        for ( ModePaiementMoovMoney modePaiementMoovMoney : entities ) {
            list.add( toDto( modePaiementMoovMoney ) );
        }

        return list;
    }

    @Override
    public ModePaiementMoovMoney toEntity(ModePaiementMoovMoneyDTO dto, CompagnieTransport compagnieTransport, Reference typeModePaiement) throws ParseException {
        if ( dto == null && compagnieTransport == null && typeModePaiement == null ) {
            return null;
        }

        ModePaiementMoovMoney modePaiementMoovMoney = new ModePaiementMoovMoney();

        if ( dto != null ) {
            modePaiementMoovMoney.setId( dto.getId() );
            modePaiementMoovMoney.setDesignation( dto.getDesignation() );
            modePaiementMoovMoney.setDescription( dto.getDescription() );
            modePaiementMoovMoney.setTelephoneMoovMoney( dto.getTelephoneMoovMoney() );
            if ( dto.getUpdatedAt() != null ) {
                modePaiementMoovMoney.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                modePaiementMoovMoney.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                modePaiementMoovMoney.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            modePaiementMoovMoney.setUpdatedBy( dto.getUpdatedBy() );
            modePaiementMoovMoney.setCreatedBy( dto.getCreatedBy() );
            modePaiementMoovMoney.setDeletedBy( dto.getDeletedBy() );
            modePaiementMoovMoney.setIsDeleted( dto.getIsDeleted() );
        }
        if ( compagnieTransport != null ) {
            modePaiementMoovMoney.setCompagnieTransport( compagnieTransport );
        }
        if ( typeModePaiement != null ) {
            modePaiementMoovMoney.setTypeModePaiement( typeModePaiement );
        }

        return modePaiementMoovMoney;
    }

    private String entityCompagnieTransportRaisonSociale(ModePaiementMoovMoney modePaiementMoovMoney) {
        if ( modePaiementMoovMoney == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = modePaiementMoovMoney.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        String raisonSociale = compagnieTransport.getRaisonSociale();
        if ( raisonSociale == null ) {
            return null;
        }
        return raisonSociale;
    }

    private String entityTypeModePaiementDesignation(ModePaiementMoovMoney modePaiementMoovMoney) {
        if ( modePaiementMoovMoney == null ) {
            return null;
        }
        Reference typeModePaiement = modePaiementMoovMoney.getTypeModePaiement();
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
