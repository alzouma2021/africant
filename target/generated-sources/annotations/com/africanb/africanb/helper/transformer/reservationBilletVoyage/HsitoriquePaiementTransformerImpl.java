package com.africanb.africanb.helper.transformer.reservationBilletVoyage;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiement;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.HistoriquePaiement;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.HistoriquePaiementDTO;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-30T11:35:24+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.0.1 (Oracle Corporation)"
)
public class HsitoriquePaiementTransformerImpl implements HsitoriquePaiementTransformer {

    @Override
    public HistoriquePaiementDTO toDto(HistoriquePaiement entity) throws ParseException {
        if ( entity == null ) {
            return null;
        }

        HistoriquePaiementDTO historiquePaiementDTO = new HistoriquePaiementDTO();

        historiquePaiementDTO.setId( entity.getId() );
        historiquePaiementDTO.setIdentifiantUnique( entity.getIdentifiantUnique() );
        historiquePaiementDTO.setDescription( entity.getDescription() );
        historiquePaiementDTO.setDateTimePayment( entity.getDateTimePayment() );
        historiquePaiementDTO.setModePaiementDesignation( entityModePaiementDesignation( entity ) );

        return historiquePaiementDTO;
    }

    @Override
    public List<HistoriquePaiementDTO> toDtos(List<HistoriquePaiement> entities) throws ParseException {
        if ( entities == null ) {
            return null;
        }

        List<HistoriquePaiementDTO> list = new ArrayList<HistoriquePaiementDTO>( entities.size() );
        for ( HistoriquePaiement historiquePaiement : entities ) {
            list.add( toDto( historiquePaiement ) );
        }

        return list;
    }

    @Override
    public HistoriquePaiement toEntity(HistoriquePaiementDTO dto, ModePaiement modePaiement) throws ParseException {
        if ( dto == null && modePaiement == null ) {
            return null;
        }

        HistoriquePaiement historiquePaiement = new HistoriquePaiement();

        if ( dto != null ) {
            historiquePaiement.setId( dto.getId() );
            historiquePaiement.setIdentifiantUnique( dto.getIdentifiantUnique() );
            historiquePaiement.setDescription( dto.getDescription() );
            historiquePaiement.setDateTimePayment( dto.getDateTimePayment() );
        }
        if ( modePaiement != null ) {
            historiquePaiement.setModePaiement( modePaiement );
        }

        return historiquePaiement;
    }

    private String entityModePaiementDesignation(HistoriquePaiement historiquePaiement) {
        if ( historiquePaiement == null ) {
            return null;
        }
        ModePaiement modePaiement = historiquePaiement.getModePaiement();
        if ( modePaiement == null ) {
            return null;
        }
        String designation = modePaiement.getDesignation();
        if ( designation == null ) {
            return null;
        }
        return designation;
    }
}
