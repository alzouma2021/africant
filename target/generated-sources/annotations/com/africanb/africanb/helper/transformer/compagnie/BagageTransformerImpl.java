package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.Bagage;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.helper.dto.compagnie.BagageDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-14T15:57:51+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class BagageTransformerImpl implements BagageTransformer {

    @Override
    public BagageDTO toDto(Bagage entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        BagageDTO bagageDTO = new BagageDTO();

        bagageDTO.setId( entity.getId() );
        bagageDTO.setDesignation( entity.getDesignation() );
        bagageDTO.setDescription( entity.getDescription() );
        bagageDTO.setCoutBagageParTypeBagage( entity.getCoutBagageParTypeBagage() );
        bagageDTO.setNombreBagageGratuitParTypeBagage( entity.getNombreBagageGratuitParTypeBagage() );
        bagageDTO.setTypeBagageDesignation( entityTypeBagageDesignation( entity ) );
        bagageDTO.setCompagnieTransportRaisonSociale( entityCompagnieTransportRaisonSociale( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            bagageDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            bagageDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            bagageDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        bagageDTO.setUpdatedBy( entity.getUpdatedBy() );
        bagageDTO.setCreatedBy( entity.getCreatedBy() );
        bagageDTO.setDeletedBy( entity.getDeletedBy() );
        bagageDTO.setIsDeleted( entity.getIsDeleted() );

        return bagageDTO;
    }

    @Override
    public List<BagageDTO> toDtos(List<Bagage> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<BagageDTO> list = new ArrayList<BagageDTO>( entities.size() );
        for ( Bagage bagage : entities ) {
            list.add( toDto( bagage ) );
        }

        return list;
    }

    @Override
    public Bagage toEntity(BagageDTO dto, Reference typeBagage, CompagnieTransport compagnieTransport) throws ParseException {
        if ( dto == null && typeBagage == null && compagnieTransport == null ) {
            return null;
        }

        Bagage bagage = new Bagage();

        if ( dto != null ) {
            bagage.setId( dto.getId() );
            bagage.setDesignation( dto.getDesignation() );
            bagage.setDescription( dto.getDescription() );
            bagage.setCoutBagageParTypeBagage( dto.getCoutBagageParTypeBagage() );
            bagage.setNombreBagageGratuitParTypeBagage( dto.getNombreBagageGratuitParTypeBagage() );
            if ( dto.getUpdatedAt() != null ) {
                bagage.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                bagage.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                bagage.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            bagage.setUpdatedBy( dto.getUpdatedBy() );
            bagage.setCreatedBy( dto.getCreatedBy() );
            bagage.setDeletedBy( dto.getDeletedBy() );
            bagage.setIsDeleted( dto.getIsDeleted() );
        }
        if ( typeBagage != null ) {
            bagage.setTypeBagage( typeBagage );
        }
        if ( compagnieTransport != null ) {
            bagage.setCompagnieTransport( compagnieTransport );
        }

        return bagage;
    }

    private String entityTypeBagageDesignation(Bagage bagage) {
        if ( bagage == null ) {
            return null;
        }
        Reference typeBagage = bagage.getTypeBagage();
        if ( typeBagage == null ) {
            return null;
        }
        String designation = typeBagage.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityCompagnieTransportRaisonSociale(Bagage bagage) {
        if ( bagage == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = bagage.getCompagnieTransport();
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
