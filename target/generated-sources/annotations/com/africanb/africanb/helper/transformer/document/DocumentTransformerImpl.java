package com.africanb.africanb.helper.transformer.document;

import com.africanb.africanb.dao.entity.document.Document;
import com.africanb.africanb.helper.dto.document.DocumentDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-07T12:59:20+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class DocumentTransformerImpl implements DocumentTransformer {

    @Override
    public DocumentDTO toDto(Document entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        DocumentDTO documentDTO = new DocumentDTO();

        documentDTO.setId( entity.getId() );
        documentDTO.setDesignation( entity.getDesignation() );
        documentDTO.setDescription( entity.getDescription() );
        documentDTO.setPath( entity.getPath() );
        documentDTO.setTypeMime( entity.getTypeMime() );
        if ( entity.getUpdatedAt() != null ) {
            documentDTO.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getUpdatedAt() ) );
        }
        if ( entity.getCreatedAt() != null ) {
            documentDTO.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getCreatedAt() ) );
        }
        if ( entity.getDeletedAt() != null ) {
            documentDTO.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).format( entity.getDeletedAt() ) );
        }
        documentDTO.setUpdatedBy( entity.getUpdatedBy() );
        documentDTO.setCreatedBy( entity.getCreatedBy() );
        documentDTO.setDeletedBy( entity.getDeletedBy() );
        documentDTO.setIsDeleted( entity.getIsDeleted() );
        documentDTO.setExtension( entity.getExtension() );

        return documentDTO;
    }

    @Override
    public List<DocumentDTO> toDtos(List<Document> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<DocumentDTO> list = new ArrayList<DocumentDTO>( entities.size() );
        for ( Document document : entities ) {
            list.add( toDto( document ) );
        }

        return list;
    }

    @Override
    public Document toEntity(DocumentDTO dto) throws ParseException {
        if ( dto == null ) {
            return null;
        }

        Document document = new Document();

        document.setId( dto.getId() );
        document.setDesignation( dto.getDesignation() );
        document.setDescription( dto.getDescription() );
        document.setPath( dto.getPath() );
        document.setTypeMime( dto.getTypeMime() );
        if ( dto.getUpdatedAt() != null ) {
            document.setUpdatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getUpdatedAt() ) );
        }
        if ( dto.getCreatedAt() != null ) {
            document.setCreatedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getCreatedAt() ) );
        }
        if ( dto.getDeletedAt() != null ) {
            document.setDeletedAt( new SimpleDateFormat( "dd/MM/yyyy" ).parse( dto.getDeletedAt() ) );
        }
        document.setUpdatedBy( dto.getUpdatedBy() );
        document.setCreatedBy( dto.getCreatedBy() );
        document.setDeletedBy( dto.getDeletedBy() );
        document.setIsDeleted( dto.getIsDeleted() );
        document.setExtension( dto.getExtension() );

        return document;
    }
}
