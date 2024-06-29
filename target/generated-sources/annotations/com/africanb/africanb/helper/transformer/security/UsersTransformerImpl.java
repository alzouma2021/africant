package com.africanb.africanb.helper.transformer.security;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.security.Role;
import com.africanb.africanb.dao.entity.security.Users;
import com.africanb.africanb.helper.dto.security.UsersDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-29T16:17:02+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
public class UsersTransformerImpl implements UsersTransformer {

    @Override
    public UsersDTO toDto(Users entity) {
        if ( entity == null ) {
            return null;
        }

        UsersDTO usersDTO = new UsersDTO();

        usersDTO.setId( entity.getId() );
        usersDTO.setNom( entity.getNom() );
        usersDTO.setPrenoms( entity.getPrenoms() );
        usersDTO.setEmail( entity.getEmail() );
        usersDTO.setTelephone( entity.getTelephone() );
        usersDTO.setNumberOfConnections( entity.getNumberOfConnections() );
        usersDTO.setLogin( entity.getLogin() );
        usersDTO.setIsActif( entity.getIsActif() );
        usersDTO.setIsFirst( entity.getIsFirst() );
        usersDTO.setRoleCode( entityRoleCode( entity ) );
        usersDTO.setRoleLibelle( entityRoleLibelle( entity ) );
        usersDTO.setCompagnieTransportId( entityCompagnieTransportId( entity ) );
        usersDTO.setCompagnieTransportDesignation( entityCompagnieTransportDesignation( entity ) );
        usersDTO.setCompagnieTransportRaisonSociale( entityCompagnieTransportRaisonSociale( entity ) );
        usersDTO.setGareDesignation( entity.getGareDesignation() );
        if ( entity.getUpdatedAt() != null ) {
            usersDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            usersDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            usersDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( entity.getDeletedAt() ) );
        }
        if ( entity.getLastConnectionDate() != null ) {
            usersDTO.setLastConnectionDate( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).format( entity.getLastConnectionDate() ) );
        }
        usersDTO.setUpdatedBy( entity.getUpdatedBy() );
        usersDTO.setCreatedBy( entity.getCreatedBy() );
        usersDTO.setDeletedBy( entity.getDeletedBy() );
        usersDTO.setIsDeleted( entity.getIsDeleted() );

        return usersDTO;
    }

    @Override
    public List<UsersDTO> toDtos(List<Users> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<UsersDTO> list = new ArrayList<UsersDTO>( entities.size() );
        for ( Users users : entities ) {
            list.add( toDto( users ) );
        }

        return list;
    }

    @Override
    public Users toEntity(UsersDTO dto, Role role, CompagnieTransport compagnieTransport) {
        if ( dto == null && role == null && compagnieTransport == null ) {
            return null;
        }

        Users users = new Users();

        if ( dto != null ) {
            users.setId( dto.getId() );
            users.setNom( dto.getNom() );
            users.setPrenoms( dto.getPrenoms() );
            users.setEmail( dto.getEmail() );
            users.setTelephone( dto.getTelephone() );
            users.setLogin( dto.getLogin() );
            users.setNumberOfConnections( dto.getNumberOfConnections() );
            users.setIsActif( dto.getIsActif() );
            users.setIsFirst( dto.getIsFirst() );
            try {
                if ( dto.getUpdatedAt() != null ) {
                    users.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).parse( dto.getUpdatedAt() ) );
                }
            }
            catch ( ParseException e ) {
                throw new RuntimeException( e );
            }
            try {
                if ( dto.getCreatedAt() != null ) {
                    users.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).parse( dto.getCreatedAt() ) );
                }
            }
            catch ( ParseException e ) {
                throw new RuntimeException( e );
            }
            try {
                if ( dto.getDeletedAt() != null ) {
                    users.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).parse( dto.getDeletedAt() ) );
                }
            }
            catch ( ParseException e ) {
                throw new RuntimeException( e );
            }
            try {
                if ( dto.getLastConnectionDate() != null ) {
                    users.setLastConnectionDate( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" ).parse( dto.getLastConnectionDate() ) );
                }
            }
            catch ( ParseException e ) {
                throw new RuntimeException( e );
            }
            users.setUpdatedBy( dto.getUpdatedBy() );
            users.setCreatedBy( dto.getCreatedBy() );
            users.setDeletedBy( dto.getDeletedBy() );
            users.setIsDeleted( dto.getIsDeleted() );
            users.setGareDesignation( dto.getGareDesignation() );
        }
        if ( role != null ) {
            users.setRole( role );
        }
        if ( compagnieTransport != null ) {
            users.setCompagnieTransport( compagnieTransport );
        }

        return users;
    }

    private String entityRoleCode(Users users) {
        if ( users == null ) {
            return null;
        }
        Role role = users.getRole();
        if ( role == null ) {
            return null;
        }
        String code = role.getCode();
        if ( code == null ) {
            return null;
        }
        return code;
    }

    private String entityRoleLibelle(Users users) {
        if ( users == null ) {
            return null;
        }
        Role role = users.getRole();
        if ( role == null ) {
            return null;
        }
        String libelle = role.getLibelle();
        if ( libelle == null ) {
            return null;
        }
        return libelle;
    }

    private Long entityCompagnieTransportId(Users users) {
        if ( users == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = users.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        Long id = compagnieTransport.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityCompagnieTransportDesignation(Users users) {
        if ( users == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = users.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        String designation = compagnieTransport.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }

    private String entityCompagnieTransportRaisonSociale(Users users) {
        if ( users == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = users.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        String raisonSociale = compagnieTransport.getRaisonSociale();
        if ( raisonSociale == null ) {
            return null;
        }
        return raisonSociale;
    }
}
