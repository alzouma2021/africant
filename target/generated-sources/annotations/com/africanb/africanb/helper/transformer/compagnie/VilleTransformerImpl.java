package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.Pays;
import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.helper.dto.compagnie.VilleDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-25T17:27:05+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class VilleTransformerImpl implements VilleTransformer {

    @Override
    public VilleDTO toDto(Ville entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        VilleDTO villeDTO = new VilleDTO();

        villeDTO.setId( entity.getId() );
        villeDTO.setDesignation( entity.getDesignation() );
        villeDTO.setDescription( entity.getDescription() );
        villeDTO.setPaysId( entityPaysId( entity ) );
        villeDTO.setPaysDesignation( entityPaysDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            villeDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            villeDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            villeDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        villeDTO.setUpdatedBy( entity.getUpdatedBy() );
        villeDTO.setCreatedBy( entity.getCreatedBy() );
        villeDTO.setDeletedBy( entity.getDeletedBy() );
        villeDTO.setIsDeleted( entity.getIsDeleted() );

        return villeDTO;
    }

    @Override
    public List<VilleDTO> toDtos(List<Ville> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<VilleDTO> list = new ArrayList<VilleDTO>( entities.size() );
        for ( Ville ville : entities ) {
            list.add( toDto( ville ) );
        }

        return list;
    }

    @Override
    public Ville toEntity(VilleDTO dto, Pays pays) throws ParseException {
        if ( dto == null && pays == null ) {
            return null;
        }

        Ville ville = new Ville();

        if ( dto != null ) {
            ville.setId( dto.getId() );
            ville.setDesignation( dto.getDesignation() );
            ville.setDescription( dto.getDescription() );
            if ( dto.getUpdatedAt() != null ) {
                ville.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                ville.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                ville.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            ville.setUpdatedBy( dto.getUpdatedBy() );
            ville.setCreatedBy( dto.getCreatedBy() );
            ville.setDeletedBy( dto.getDeletedBy() );
            ville.setIsDeleted( dto.getIsDeleted() );
        }
        if ( pays != null ) {
            ville.setPays( pays );
        }

        return ville;
    }

    private Long entityPaysId(Ville ville) {
        if ( ville == null ) {
            return null;
        }
        Pays pays = ville.getPays();
        if ( pays == null ) {
            return null;
        }
        Long id = pays.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityPaysDesignation(Ville ville) {
        if ( ville == null ) {
            return null;
        }
        Pays pays = ville.getPays();
        if ( pays == null ) {
            return null;
        }
        String designation = pays.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
