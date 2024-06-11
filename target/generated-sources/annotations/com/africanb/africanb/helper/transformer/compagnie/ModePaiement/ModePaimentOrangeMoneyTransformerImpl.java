package com.africanb.africanb.helper.transformer.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementOrangeMoney;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementOrangeMoneyDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-11T18:20:24+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ModePaimentOrangeMoneyTransformerImpl implements ModePaimentOrangeMoneyTransformer {

    @Override
    public ModePaiementOrangeMoneyDTO toDto(ModePaiementOrangeMoney entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ModePaiementOrangeMoneyDTO modePaiementOrangeMoneyDTO = new ModePaiementOrangeMoneyDTO();

        modePaiementOrangeMoneyDTO.setId( entity.getId() );
        modePaiementOrangeMoneyDTO.setDesignation( entity.getDesignation() );
        modePaiementOrangeMoneyDTO.setDescription( entity.getDescription() );
        modePaiementOrangeMoneyDTO.setTelephoneOrangeMoney( entity.getTelephoneOrangeMoney() );
        modePaiementOrangeMoneyDTO.setCompagnieTransportRaisonSociale( entityCompagnieTransportRaisonSociale( entity ) );
        modePaiementOrangeMoneyDTO.setTypeModePaiementDesignation( entityTypeModePaiementDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            modePaiementOrangeMoneyDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            modePaiementOrangeMoneyDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            modePaiementOrangeMoneyDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        modePaiementOrangeMoneyDTO.setUpdatedBy( entity.getUpdatedBy() );
        modePaiementOrangeMoneyDTO.setCreatedBy( entity.getCreatedBy() );
        modePaiementOrangeMoneyDTO.setDeletedBy( entity.getDeletedBy() );
        modePaiementOrangeMoneyDTO.setIsDeleted( entity.getIsDeleted() );

        return modePaiementOrangeMoneyDTO;
    }

    @Override
    public List<ModePaiementOrangeMoneyDTO> toDtos(List<ModePaiementOrangeMoney> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ModePaiementOrangeMoneyDTO> list = new ArrayList<ModePaiementOrangeMoneyDTO>( entities.size() );
        for ( ModePaiementOrangeMoney modePaiementOrangeMoney : entities ) {
            list.add( toDto( modePaiementOrangeMoney ) );
        }

        return list;
    }

    @Override
    public ModePaiementOrangeMoney toEntity(ModePaiementOrangeMoneyDTO dto, CompagnieTransport compagnieTransport, Reference typeModePaiement) throws ParseException {
        if ( dto == null && compagnieTransport == null && typeModePaiement == null ) {
            return null;
        }

        ModePaiementOrangeMoney modePaiementOrangeMoney = new ModePaiementOrangeMoney();

        if ( dto != null ) {
            modePaiementOrangeMoney.setId( dto.getId() );
            modePaiementOrangeMoney.setDesignation( dto.getDesignation() );
            modePaiementOrangeMoney.setDescription( dto.getDescription() );
            modePaiementOrangeMoney.setTelephoneOrangeMoney( dto.getTelephoneOrangeMoney() );
            if ( dto.getUpdatedAt() != null ) {
                modePaiementOrangeMoney.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                modePaiementOrangeMoney.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                modePaiementOrangeMoney.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            modePaiementOrangeMoney.setUpdatedBy( dto.getUpdatedBy() );
            modePaiementOrangeMoney.setCreatedBy( dto.getCreatedBy() );
            modePaiementOrangeMoney.setDeletedBy( dto.getDeletedBy() );
            modePaiementOrangeMoney.setIsDeleted( dto.getIsDeleted() );
        }
        if ( compagnieTransport != null ) {
            modePaiementOrangeMoney.setCompagnieTransport( compagnieTransport );
        }
        if ( typeModePaiement != null ) {
            modePaiementOrangeMoney.setTypeModePaiement( typeModePaiement );
        }

        return modePaiementOrangeMoney;
    }

    private String entityCompagnieTransportRaisonSociale(ModePaiementOrangeMoney modePaiementOrangeMoney) {
        if ( modePaiementOrangeMoney == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = modePaiementOrangeMoney.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        String raisonSociale = compagnieTransport.getRaisonSociale();
        if ( raisonSociale == null ) {
            return null;
        }
        return raisonSociale;
    }

    private String entityTypeModePaiementDesignation(ModePaiementOrangeMoney modePaiementOrangeMoney) {
        if ( modePaiementOrangeMoney == null ) {
            return null;
        }
        Reference typeModePaiement = modePaiementOrangeMoney.getTypeModePaiement();
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
