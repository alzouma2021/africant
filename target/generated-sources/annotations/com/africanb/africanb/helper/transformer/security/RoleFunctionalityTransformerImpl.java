package com.africanb.africanb.helper.transformer.security;

import com.africanb.africanb.dao.entity.security.Functionality;
import com.africanb.africanb.dao.entity.security.Role;
import com.africanb.africanb.dao.entity.security.RoleFunctionality;
import com.africanb.africanb.helper.dto.security.RoleFunctionalityDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-29T21:41:08+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
public class RoleFunctionalityTransformerImpl implements RoleFunctionalityTransformer {

    @Override
    public RoleFunctionalityDTO toDto(RoleFunctionality entity) {
        if ( entity == null ) {
            return null;
        }

        RoleFunctionalityDTO roleFunctionalityDTO = new RoleFunctionalityDTO();

        roleFunctionalityDTO.setId( entity.getId() );
        roleFunctionalityDTO.setRoleId( entityRoleId( entity ) );
        roleFunctionalityDTO.setRoleLibelle( entityRoleLibelle( entity ) );
        roleFunctionalityDTO.setFunctionalityId( entityFunctionalityId( entity ) );
        roleFunctionalityDTO.setFunctionalityLibelle( entityFunctionalityLibelle( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            roleFunctionalityDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            roleFunctionalityDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            roleFunctionalityDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        roleFunctionalityDTO.setUpdatedBy( entity.getUpdatedBy() );
        roleFunctionalityDTO.setCreatedBy( entity.getCreatedBy() );
        roleFunctionalityDTO.setDeletedBy( entity.getDeletedBy() );
        roleFunctionalityDTO.setIsDeleted( entity.getIsDeleted() );

        return roleFunctionalityDTO;
    }

    @Override
    public List<RoleFunctionalityDTO> toDtos(List<RoleFunctionality> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<RoleFunctionalityDTO> list = new ArrayList<RoleFunctionalityDTO>( entities.size() );
        for ( RoleFunctionality roleFunctionality : entities ) {
            list.add( toDto( roleFunctionality ) );
        }

        return list;
    }

    @Override
    public RoleFunctionality toEntity(RoleFunctionalityDTO dto, Functionality functionality, Role role) {
        if ( dto == null && functionality == null && role == null ) {
            return null;
        }

        RoleFunctionality roleFunctionality = new RoleFunctionality();

        if ( dto != null ) {
            roleFunctionality.setId( dto.getId() );
            try {
                if ( dto.getUpdatedAt() != null ) {
                    roleFunctionality.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
                }
            }
            catch ( ParseException e ) {
                throw new RuntimeException( e );
            }
            try {
                if ( dto.getCreatedAt() != null ) {
                    roleFunctionality.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
                }
            }
            catch ( ParseException e ) {
                throw new RuntimeException( e );
            }
            try {
                if ( dto.getDeletedAt() != null ) {
                    roleFunctionality.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
                }
            }
            catch ( ParseException e ) {
                throw new RuntimeException( e );
            }
            roleFunctionality.setUpdatedBy( dto.getUpdatedBy() );
            roleFunctionality.setCreatedBy( dto.getCreatedBy() );
            roleFunctionality.setDeletedBy( dto.getDeletedBy() );
            roleFunctionality.setIsDeleted( dto.getIsDeleted() );
        }
        if ( functionality != null ) {
            roleFunctionality.setFunctionality( functionality );
        }
        if ( role != null ) {
            roleFunctionality.setRole( role );
        }

        return roleFunctionality;
    }

    private Long entityRoleId(RoleFunctionality roleFunctionality) {
        if ( roleFunctionality == null ) {
            return null;
        }
        Role role = roleFunctionality.getRole();
        if ( role == null ) {
            return null;
        }
        Long id = role.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityRoleLibelle(RoleFunctionality roleFunctionality) {
        if ( roleFunctionality == null ) {
            return null;
        }
        Role role = roleFunctionality.getRole();
        if ( role == null ) {
            return null;
        }
        String libelle = role.getLibelle();
        if ( libelle == null ) {
            return null;
        }
        return libelle;
    }

    private Long entityFunctionalityId(RoleFunctionality roleFunctionality) {
        if ( roleFunctionality == null ) {
            return null;
        }
        Functionality functionality = roleFunctionality.getFunctionality();
        if ( functionality == null ) {
            return null;
        }
        Long id = functionality.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityFunctionalityLibelle(RoleFunctionality roleFunctionality) {
        if ( roleFunctionality == null ) {
            return null;
        }
        Functionality functionality = roleFunctionality.getFunctionality();
        if ( functionality == null ) {
            return null;
        }
        String libelle = functionality.getLibelle();
        if ( libelle == null ) {
            return null;
        }
        return libelle;
    }
}
