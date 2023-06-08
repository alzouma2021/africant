package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.Pays;
import com.africanb.africanb.helper.dto.compagnie.PaysDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-08T09:27:15+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class PaysTransformerImpl implements PaysTransformer {

    @Override
    public PaysDTO toDto(Pays entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        PaysDTO paysDTO = new PaysDTO();

        paysDTO.setId( entity.getId() );
        paysDTO.setDesignation( entity.getDesignation() );
        paysDTO.setDescription( entity.getDescription() );
        if ( entity.getUpdatedAt() != null ) {
            paysDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            paysDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            paysDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        paysDTO.setUpdatedBy( entity.getUpdatedBy() );
        paysDTO.setCreatedBy( entity.getCreatedBy() );
        paysDTO.setDeletedBy( entity.getDeletedBy() );
        paysDTO.setIsDeleted( entity.getIsDeleted() );

        return paysDTO;
    }

    @Override
    public List<PaysDTO> toDtos(List<Pays> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<PaysDTO> list = new ArrayList<PaysDTO>( entities.size() );
        for ( Pays pays : entities ) {
            list.add( toDto( pays ) );
        }

        return list;
    }

    @Override
    public Pays toEntity(PaysDTO dto) throws ParseException {
        if ( dto == null ) {
            return null;
        }

        Pays pays = new Pays();

        pays.setId( dto.getId() );
        pays.setDesignation( dto.getDesignation() );
        pays.setDescription( dto.getDescription() );
        if ( dto.getUpdatedAt() != null ) {
            pays.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
        }
        if ( dto.getCreatedAt() != null ) {
            pays.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
        }
        if ( dto.getDeletedAt() != null ) {
            pays.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
        }
        pays.setUpdatedBy( dto.getUpdatedBy() );
        pays.setCreatedBy( dto.getCreatedBy() );
        pays.setDeletedBy( dto.getDeletedBy() );
        pays.setIsDeleted( dto.getIsDeleted() );

        return pays;
    }
}
