package com.africanb.africanb.utils.Reference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-01T10:30:02+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ReferenceFamilleTransformerImpl implements ReferenceFamilleTransformer {

    @Override
    public ReferenceFamilleDTO toDto(ReferenceFamille entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ReferenceFamilleDTO referenceFamilleDTO = new ReferenceFamilleDTO();

        referenceFamilleDTO.setId( entity.getId() );
        referenceFamilleDTO.setDesignation( entity.getDesignation() );
        referenceFamilleDTO.setDescription( entity.getDescription() );
        if ( entity.getUpdatedAt() != null ) {
            referenceFamilleDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            referenceFamilleDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            referenceFamilleDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        referenceFamilleDTO.setUpdatedBy( entity.getUpdatedBy() );
        referenceFamilleDTO.setCreatedBy( entity.getCreatedBy() );
        referenceFamilleDTO.setDeletedBy( entity.getDeletedBy() );
        referenceFamilleDTO.setIsDeleted( entity.getIsDeleted() );

        return referenceFamilleDTO;
    }

    @Override
    public List<ReferenceFamilleDTO> toDtos(List<ReferenceFamille> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ReferenceFamilleDTO> list = new ArrayList<ReferenceFamilleDTO>( entities.size() );
        for ( ReferenceFamille referenceFamille : entities ) {
            list.add( toDto( referenceFamille ) );
        }

        return list;
    }

    @Override
    public ReferenceFamille toEntity(ReferenceFamilleDTO dto) throws ParseException {
        if ( dto == null ) {
            return null;
        }

        ReferenceFamille referenceFamille = new ReferenceFamille();

        referenceFamille.setId( dto.getId() );
        referenceFamille.setDesignation( dto.getDesignation() );
        referenceFamille.setDescription( dto.getDescription() );
        if ( dto.getUpdatedAt() != null ) {
            referenceFamille.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
        }
        if ( dto.getCreatedAt() != null ) {
            referenceFamille.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
        }
        if ( dto.getDeletedAt() != null ) {
            referenceFamille.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
        }
        referenceFamille.setUpdatedBy( dto.getUpdatedBy() );
        referenceFamille.setCreatedBy( dto.getCreatedBy() );
        referenceFamille.setDeletedBy( dto.getDeletedBy() );
        referenceFamille.setIsDeleted( dto.getIsDeleted() );

        return referenceFamille;
    }
}
