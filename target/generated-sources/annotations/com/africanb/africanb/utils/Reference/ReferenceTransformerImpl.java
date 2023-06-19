package com.africanb.africanb.utils.Reference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-19T17:00:00+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ReferenceTransformerImpl implements ReferenceTransformer {

    @Override
    public ReferenceDTO toDto(Reference entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ReferenceDTO referenceDTO = new ReferenceDTO();

        referenceDTO.setId( entity.getId() );
        referenceDTO.setDesignation( entity.getDesignation() );
        referenceDTO.setDescription( entity.getDescription() );
        referenceDTO.setReferenceFamilleId( entityReferenceFamilleId( entity ) );
        referenceDTO.setReferenceFamilleDesignation( entityReferenceFamilleDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            referenceDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            referenceDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            referenceDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        referenceDTO.setUpdatedBy( entity.getUpdatedBy() );
        referenceDTO.setCreatedBy( entity.getCreatedBy() );
        referenceDTO.setDeletedBy( entity.getDeletedBy() );
        referenceDTO.setIsDeleted( entity.getIsDeleted() );

        return referenceDTO;
    }

    @Override
    public List<ReferenceDTO> toDtos(List<Reference> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ReferenceDTO> list = new ArrayList<ReferenceDTO>( entities.size() );
        for ( Reference reference : entities ) {
            list.add( toDto( reference ) );
        }

        return list;
    }

    @Override
    public Reference toEntity(ReferenceDTO dto, ReferenceFamille referenceFamille) throws ParseException {
        if ( dto == null && referenceFamille == null ) {
            return null;
        }

        Reference reference = new Reference();

        if ( dto != null ) {
            reference.setId( dto.getId() );
            reference.setDesignation( dto.getDesignation() );
            reference.setDescription( dto.getDescription() );
            if ( dto.getUpdatedAt() != null ) {
                reference.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                reference.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                reference.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            reference.setUpdatedBy( dto.getUpdatedBy() );
            reference.setCreatedBy( dto.getCreatedBy() );
            reference.setDeletedBy( dto.getDeletedBy() );
            reference.setIsDeleted( dto.getIsDeleted() );
        }
        if ( referenceFamille != null ) {
            reference.setReferenceFamille( referenceFamille );
        }

        return reference;
    }

    private Long entityReferenceFamilleId(Reference reference) {
        if ( reference == null ) {
            return null;
        }
        ReferenceFamille referenceFamille = reference.getReferenceFamille();
        if ( referenceFamille == null ) {
            return null;
        }
        Long id = referenceFamille.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityReferenceFamilleDesignation(Reference reference) {
        if ( reference == null ) {
            return null;
        }
        ReferenceFamille referenceFamille = reference.getReferenceFamille();
        if ( referenceFamille == null ) {
            return null;
        }
        String designation = referenceFamille.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
