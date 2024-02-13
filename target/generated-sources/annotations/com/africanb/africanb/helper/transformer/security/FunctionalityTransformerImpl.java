package com.africanb.africanb.helper.transformer.security;

import com.africanb.africanb.dao.entity.security.Functionality;
import com.africanb.africanb.helper.dto.security.FunctionalityDTO;
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
public class FunctionalityTransformerImpl implements FunctionalityTransformer {

    @Override
    public FunctionalityDTO toDto(Functionality entity) {
        if ( entity == null ) {
            return null;
        }

        FunctionalityDTO functionalityDTO = new FunctionalityDTO();

        functionalityDTO.setId( entity.getId() );
        functionalityDTO.setCode( entity.getCode() );
        functionalityDTO.setLibelle( entity.getLibelle() );
        if ( entity.getUpdatedAt() != null ) {
            functionalityDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            functionalityDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            functionalityDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( entity.getDeletedAt() ) );
        }
        functionalityDTO.setUpdatedBy( entity.getUpdatedBy() );
        functionalityDTO.setCreatedBy( entity.getCreatedBy() );
        functionalityDTO.setDeletedBy( entity.getDeletedBy() );
        functionalityDTO.setIsDeleted( entity.getIsDeleted() );

        return functionalityDTO;
    }

    @Override
    public List<FunctionalityDTO> toDtos(List<Functionality> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<FunctionalityDTO> list = new ArrayList<FunctionalityDTO>( entities.size() );
        for ( Functionality functionality : entities ) {
            list.add( toDto( functionality ) );
        }

        return list;
    }

    @Override
    public Functionality toEntity(FunctionalityDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Functionality functionality = new Functionality();

        functionality.setId( dto.getId() );
        functionality.setCode( dto.getCode() );
        functionality.setLibelle( dto.getLibelle() );
        try {
            if ( dto.getUpdatedAt() != null ) {
                functionality.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).parse( dto.getUpdatedAt() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        try {
            if ( dto.getCreatedAt() != null ) {
                functionality.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).parse( dto.getCreatedAt() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        try {
            if ( dto.getDeletedAt() != null ) {
                functionality.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).parse( dto.getDeletedAt() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        functionality.setUpdatedBy( dto.getUpdatedBy() );
        functionality.setCreatedBy( dto.getCreatedBy() );
        functionality.setDeletedBy( dto.getDeletedBy() );
        functionality.setIsDeleted( dto.getIsDeleted() );

        return functionality;
    }
}
