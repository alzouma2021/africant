package com.africanb.africanb.helper.transformer.offrreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.helper.dto.offreVoyage.ProprieteOffreVoyageDTO;
import com.africanb.africanb.utils.Reference.Reference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-24T12:00:28+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class ProprieteOffreVoyageTransformerImpl implements ProprieteOffreVoyageTransformer {

    @Override
    public ProprieteOffreVoyageDTO toDto(ProprieteOffreVoyage entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        ProprieteOffreVoyageDTO proprieteOffreVoyageDTO = new ProprieteOffreVoyageDTO();

        proprieteOffreVoyageDTO.setId( entity.getId() );
        proprieteOffreVoyageDTO.setDesignation( entity.getDesignation() );
        proprieteOffreVoyageDTO.setDescription( entity.getDescription() );
        proprieteOffreVoyageDTO.setEstObligatoire( entity.getEstObligatoire() );
        proprieteOffreVoyageDTO.setTypeProprieteOffreVoyageDesignation( entityTypeProprieteOffreVoyageDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            proprieteOffreVoyageDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            proprieteOffreVoyageDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            proprieteOffreVoyageDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        proprieteOffreVoyageDTO.setUpdatedBy( entity.getUpdatedBy() );
        proprieteOffreVoyageDTO.setCreatedBy( entity.getCreatedBy() );
        proprieteOffreVoyageDTO.setDeletedBy( entity.getDeletedBy() );
        proprieteOffreVoyageDTO.setIsDeleted( entity.getIsDeleted() );

        return proprieteOffreVoyageDTO;
    }

    @Override
    public List<ProprieteOffreVoyageDTO> toDtos(List<ProprieteOffreVoyage> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<ProprieteOffreVoyageDTO> list = new ArrayList<ProprieteOffreVoyageDTO>( entities.size() );
        for ( ProprieteOffreVoyage proprieteOffreVoyage : entities ) {
            list.add( toDto( proprieteOffreVoyage ) );
        }

        return list;
    }

    @Override
    public ProprieteOffreVoyage toEntity(ProprieteOffreVoyageDTO dto, Reference typeProprieteOffreVoyage) throws ParseException {
        if ( dto == null && typeProprieteOffreVoyage == null ) {
            return null;
        }

        ProprieteOffreVoyage proprieteOffreVoyage = new ProprieteOffreVoyage();

        if ( dto != null ) {
            proprieteOffreVoyage.setId( dto.getId() );
            proprieteOffreVoyage.setDesignation( dto.getDesignation() );
            proprieteOffreVoyage.setDescription( dto.getDescription() );
            if ( dto.getUpdatedAt() != null ) {
                proprieteOffreVoyage.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                proprieteOffreVoyage.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                proprieteOffreVoyage.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            proprieteOffreVoyage.setUpdatedBy( dto.getUpdatedBy() );
            proprieteOffreVoyage.setCreatedBy( dto.getCreatedBy() );
            proprieteOffreVoyage.setDeletedBy( dto.getDeletedBy() );
            proprieteOffreVoyage.setIsDeleted( dto.getIsDeleted() );
            proprieteOffreVoyage.setEstObligatoire( dto.getEstObligatoire() );
        }
        if ( typeProprieteOffreVoyage != null ) {
            proprieteOffreVoyage.setTypeProprieteOffreVoyage( typeProprieteOffreVoyage );
        }

        return proprieteOffreVoyage;
    }

    private String entityTypeProprieteOffreVoyageDesignation(ProprieteOffreVoyage proprieteOffreVoyage) {
        if ( proprieteOffreVoyage == null ) {
            return null;
        }
        Reference typeProprieteOffreVoyage = proprieteOffreVoyage.getTypeProprieteOffreVoyage();
        if ( typeProprieteOffreVoyage == null ) {
            return null;
        }
        String designation = typeProprieteOffreVoyage.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
