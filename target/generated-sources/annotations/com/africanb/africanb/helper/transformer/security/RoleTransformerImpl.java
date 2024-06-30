package com.africanb.africanb.helper.transformer.security;

import com.africanb.africanb.dao.entity.security.Role;
import com.africanb.africanb.helper.dto.security.RoleDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T16:51:03+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
public class RoleTransformerImpl implements RoleTransformer {

    @Override
    public RoleDTO toDto(Role entity) {
        if ( entity == null ) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setId( entity.getId() );
        roleDTO.setCode( entity.getCode() );
        roleDTO.setLibelle( entity.getLibelle() );
        if ( entity.getUpdatedAt() != null ) {
            roleDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            roleDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            roleDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( entity.getDeletedAt() ) );
        }
        roleDTO.setUpdatedBy( entity.getUpdatedBy() );
        roleDTO.setCreatedBy( entity.getCreatedBy() );
        roleDTO.setDeletedBy( entity.getDeletedBy() );
        roleDTO.setIsDeleted( entity.getIsDeleted() );

        return roleDTO;
    }

    @Override
    public List<RoleDTO> toDtos(List<Role> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<RoleDTO> list = new ArrayList<RoleDTO>( entities.size() );
        for ( Role role : entities ) {
            list.add( toDto( role ) );
        }

        return list;
    }

    @Override
    public Role toEntity(RoleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( dto.getId() );
        role.setCode( dto.getCode() );
        role.setLibelle( dto.getLibelle() );
        try {
            if ( dto.getUpdatedAt() != null ) {
                role.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).parse( dto.getUpdatedAt() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        try {
            if ( dto.getCreatedAt() != null ) {
                role.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).parse( dto.getCreatedAt() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        try {
            if ( dto.getDeletedAt() != null ) {
                role.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).parse( dto.getDeletedAt() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        role.setUpdatedBy( dto.getUpdatedBy() );
        role.setCreatedBy( dto.getCreatedBy() );
        role.setDeletedBy( dto.getDeletedBy() );
        role.setIsDeleted( dto.getIsDeleted() );

        return role;
    }
}
