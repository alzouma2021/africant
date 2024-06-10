package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.FamilleStatusUtil;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.helper.dto.compagnie.StatusUtilDTO;
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
public class StatusUtilTransformerImpl implements StatusUtilTransformer {

    @Override
    public StatusUtilDTO toDto(StatusUtil entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        StatusUtilDTO statusUtilDTO = new StatusUtilDTO();

        statusUtilDTO.setId( entity.getId() );
        statusUtilDTO.setDesignation( entity.getDesignation() );
        statusUtilDTO.setDescription( entity.getDescription() );
        statusUtilDTO.setFamilleStatusUtilId( entityFamilleStatusUtilId( entity ) );
        statusUtilDTO.setFamilleStatusUtilDesignation( entityFamilleStatusUtilDesignation( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            statusUtilDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            statusUtilDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            statusUtilDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        statusUtilDTO.setUpdatedBy( entity.getUpdatedBy() );
        statusUtilDTO.setCreatedBy( entity.getCreatedBy() );
        statusUtilDTO.setDeletedBy( entity.getDeletedBy() );
        statusUtilDTO.setIsDeleted( entity.getIsDeleted() );

        return statusUtilDTO;
    }

    @Override
    public List<StatusUtilDTO> toDtos(List<StatusUtil> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<StatusUtilDTO> list = new ArrayList<StatusUtilDTO>( entities.size() );
        for ( StatusUtil statusUtil : entities ) {
            list.add( toDto( statusUtil ) );
        }

        return list;
    }

    @Override
    public StatusUtil toEntity(StatusUtilDTO dto, FamilleStatusUtil familleStatusUtil) throws ParseException {
        if ( dto == null && familleStatusUtil == null ) {
            return null;
        }

        StatusUtil statusUtil = new StatusUtil();

        if ( dto != null ) {
            statusUtil.setId( dto.getId() );
            statusUtil.setDesignation( dto.getDesignation() );
            statusUtil.setDescription( dto.getDescription() );
            if ( dto.getUpdatedAt() != null ) {
                statusUtil.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                statusUtil.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                statusUtil.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            statusUtil.setUpdatedBy( dto.getUpdatedBy() );
            statusUtil.setCreatedBy( dto.getCreatedBy() );
            statusUtil.setDeletedBy( dto.getDeletedBy() );
            statusUtil.setIsDeleted( dto.getIsDeleted() );
        }
        if ( familleStatusUtil != null ) {
            statusUtil.setFamilleStatusUtil( familleStatusUtil );
        }

        return statusUtil;
    }

    private Long entityFamilleStatusUtilId(StatusUtil statusUtil) {
        if ( statusUtil == null ) {
            return null;
        }
        FamilleStatusUtil familleStatusUtil = statusUtil.getFamilleStatusUtil();
        if ( familleStatusUtil == null ) {
            return null;
        }
        Long id = familleStatusUtil.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityFamilleStatusUtilDesignation(StatusUtil statusUtil) {
        if ( statusUtil == null ) {
            return null;
        }
        FamilleStatusUtil familleStatusUtil = statusUtil.getFamilleStatusUtil();
        if ( familleStatusUtil == null ) {
            return null;
        }
        String designation = familleStatusUtil.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
