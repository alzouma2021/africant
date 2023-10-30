package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.FamilleStatusUtil;
import com.africanb.africanb.helper.dto.compagnie.FamilleStatusUtilDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-30T11:35:25+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class FamilleStatusUtilTransformerImpl implements FamilleStatusUtilTransformer {

    @Override
    public FamilleStatusUtilDTO toDto(FamilleStatusUtil entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        FamilleStatusUtilDTO familleStatusUtilDTO = new FamilleStatusUtilDTO();

        familleStatusUtilDTO.setId( entity.getId() );
        familleStatusUtilDTO.setDesignation( entity.getDesignation() );
        familleStatusUtilDTO.setDescription( entity.getDescription() );
        if ( entity.getUpdatedAt() != null ) {
            familleStatusUtilDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            familleStatusUtilDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            familleStatusUtilDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        familleStatusUtilDTO.setUpdatedBy( entity.getUpdatedBy() );
        familleStatusUtilDTO.setCreatedBy( entity.getCreatedBy() );
        familleStatusUtilDTO.setDeletedBy( entity.getDeletedBy() );
        familleStatusUtilDTO.setIsDeleted( entity.getIsDeleted() );

        return familleStatusUtilDTO;
    }

    @Override
    public List<FamilleStatusUtilDTO> toDtos(List<FamilleStatusUtil> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<FamilleStatusUtilDTO> list = new ArrayList<FamilleStatusUtilDTO>( entities.size() );
        for ( FamilleStatusUtil familleStatusUtil : entities ) {
            list.add( toDto( familleStatusUtil ) );
        }

        return list;
    }

    @Override
    public FamilleStatusUtil toEntity(FamilleStatusUtilDTO dto) throws ParseException {
        if ( dto == null ) {
            return null;
        }

        FamilleStatusUtil familleStatusUtil = new FamilleStatusUtil();

        familleStatusUtil.setId( dto.getId() );
        familleStatusUtil.setDesignation( dto.getDesignation() );
        familleStatusUtil.setDescription( dto.getDescription() );
        if ( dto.getUpdatedAt() != null ) {
            familleStatusUtil.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
        }
        if ( dto.getCreatedAt() != null ) {
            familleStatusUtil.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
        }
        if ( dto.getDeletedAt() != null ) {
            familleStatusUtil.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
        }
        familleStatusUtil.setUpdatedBy( dto.getUpdatedBy() );
        familleStatusUtil.setCreatedBy( dto.getCreatedBy() );
        familleStatusUtil.setDeletedBy( dto.getDeletedBy() );
        familleStatusUtil.setIsDeleted( dto.getIsDeleted() );

        return familleStatusUtil;
    }
}
