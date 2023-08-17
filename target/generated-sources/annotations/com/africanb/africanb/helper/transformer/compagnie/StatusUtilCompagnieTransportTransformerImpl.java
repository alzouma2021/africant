package com.africanb.africanb.helper.transformer.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.compagnie.StatusUtilCompagnieTransport;
import com.africanb.africanb.helper.dto.compagnie.StatusUtilCompagnieTransportDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-17T09:25:19+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class StatusUtilCompagnieTransportTransformerImpl implements StatusUtilCompagnieTransportTransformer {

    @Override
    public StatusUtilCompagnieTransportDTO toDto(StatusUtilCompagnieTransport entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        StatusUtilCompagnieTransportDTO statusUtilCompagnieTransportDTO = new StatusUtilCompagnieTransportDTO();

        statusUtilCompagnieTransportDTO.setId( entity.getId() );
        statusUtilCompagnieTransportDTO.setStatusUtilId( entityStatusUtilId( entity ) );
        statusUtilCompagnieTransportDTO.setCompagnieTransportId( entityCompagnieTransportId( entity ) );
        if ( entity.getUpdatedAt() != null ) {
            statusUtilCompagnieTransportDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            statusUtilCompagnieTransportDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            statusUtilCompagnieTransportDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        statusUtilCompagnieTransportDTO.setUpdatedBy( entity.getUpdatedBy() );
        statusUtilCompagnieTransportDTO.setCreatedBy( entity.getCreatedBy() );
        statusUtilCompagnieTransportDTO.setDeletedBy( entity.getDeletedBy() );
        statusUtilCompagnieTransportDTO.setIsDeleted( entity.getIsDeleted() );

        return statusUtilCompagnieTransportDTO;
    }

    @Override
    public List<StatusUtilCompagnieTransportDTO> toDtos(List<StatusUtilCompagnieTransport> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<StatusUtilCompagnieTransportDTO> list = new ArrayList<StatusUtilCompagnieTransportDTO>( entities.size() );
        for ( StatusUtilCompagnieTransport statusUtilCompagnieTransport : entities ) {
            list.add( toDto( statusUtilCompagnieTransport ) );
        }

        return list;
    }

    @Override
    public StatusUtilCompagnieTransport toEntity(StatusUtilCompagnieTransportDTO dto, CompagnieTransport compagnieTransport, StatusUtil statusUtil) throws ParseException {
        if ( dto == null && compagnieTransport == null && statusUtil == null ) {
            return null;
        }

        StatusUtilCompagnieTransport statusUtilCompagnieTransport = new StatusUtilCompagnieTransport();

        if ( dto != null ) {
            statusUtilCompagnieTransport.setId( dto.getId() );
            if ( dto.getUpdatedAt() != null ) {
                statusUtilCompagnieTransport.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
            }
            if ( dto.getCreatedAt() != null ) {
                statusUtilCompagnieTransport.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
            }
            if ( dto.getDeletedAt() != null ) {
                statusUtilCompagnieTransport.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
            }
            statusUtilCompagnieTransport.setUpdatedBy( dto.getUpdatedBy() );
            statusUtilCompagnieTransport.setCreatedBy( dto.getCreatedBy() );
            statusUtilCompagnieTransport.setDeletedBy( dto.getDeletedBy() );
            statusUtilCompagnieTransport.setIsDeleted( dto.getIsDeleted() );
        }
        if ( compagnieTransport != null ) {
            statusUtilCompagnieTransport.setCompagnieTransport( compagnieTransport );
        }
        if ( statusUtil != null ) {
            statusUtilCompagnieTransport.setStatusUtil( statusUtil );
        }

        return statusUtilCompagnieTransport;
    }

    private Long entityStatusUtilId(StatusUtilCompagnieTransport statusUtilCompagnieTransport) {
        if ( statusUtilCompagnieTransport == null ) {
            return null;
        }
        StatusUtil statusUtil = statusUtilCompagnieTransport.getStatusUtil();
        if ( statusUtil == null ) {
            return null;
        }
        Long id = statusUtil.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityCompagnieTransportId(StatusUtilCompagnieTransport statusUtilCompagnieTransport) {
        if ( statusUtilCompagnieTransport == null ) {
            return null;
        }
        CompagnieTransport compagnieTransport = statusUtilCompagnieTransport.getCompagnieTransport();
        if ( compagnieTransport == null ) {
            return null;
        }
        Long id = compagnieTransport.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
