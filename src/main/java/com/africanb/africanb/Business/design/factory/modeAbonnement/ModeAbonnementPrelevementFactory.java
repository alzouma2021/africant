package com.africanb.africanb.Business.design.factory.modeAbonnement;

import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPrelevement;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPrelevementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.ModeAbonnementDTO;

public class ModeAbonnementPrelevementFactory implements ModeAbonnementDTOFactory{
    @Override
    public ModeAbonnementDTO createProduct(Object object) {
        if(object instanceof AbonnementPrelevement abonnementPrelevement){
            ModeAbonnementDTO dto = new ModeAbonnementDTO();
            dto.setId(abonnementPrelevement.getId());
            dto.setDesignation(abonnementPrelevement.getDesignation());
            dto.setDescription( abonnementPrelevement.getDescription());
            dto.setTaux(abonnementPrelevement.getTaux());
            dto.setDateDebutAbonnement(abonnementPrelevement.getDateDebutAbonnement().toString());
            dto.setDateFinAbonnement(abonnementPrelevement.getDateFinAbonnement().toString());
            dto.setPeriodiciteAbonnementDesignation(abonnementPrelevement.getPeriodiciteAbonnement().getDesignation());
            dto.setCompagnieTransportRaisonSociale(abonnementPrelevement.getCompagnieTransport().getRaisonSociale());
            dto.setDeletedAt( abonnementPrelevement.getDeletedAt()!=null?abonnementPrelevement.getDeletedAt().toString():null);
            dto.setUpdatedAt( abonnementPrelevement.getUpdatedAt()!=null?abonnementPrelevement.getUpdatedAt().toString():null);
            dto.setCreatedAt( abonnementPrelevement.getCreatedAt()!=null?abonnementPrelevement.getCreatedAt().toString():null);
            dto.setCreatedBy( abonnementPrelevement.getCreatedBy());
            dto.setIsDeleted( abonnementPrelevement.getIsDeleted());
            dto.setDeletedBy( abonnementPrelevement.getDeletedBy());
            dto.setUpdatedBy( abonnementPrelevement.getUpdatedBy());
            return dto;
        }
        return new ModeAbonnementDTO();
    }
}
