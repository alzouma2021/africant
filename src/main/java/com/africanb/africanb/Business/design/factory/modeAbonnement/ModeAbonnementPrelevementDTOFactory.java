package com.africanb.africanb.Business.design.factory.modeAbonnement;

import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPrelevementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.ModeAbonnementDTO;

public class ModeAbonnementPrelevementDTOFactory implements ModeAbonnementDTOFactory {
    @Override
    public ModeAbonnementDTO createProduct(Object object) {
        if(object instanceof AbonnementPrelevementDTO abonnementPrelevementDTO){
            ModeAbonnementDTO dto = new ModeAbonnementDTO();
            dto.setId(abonnementPrelevementDTO.getId());
            dto.setDesignation(abonnementPrelevementDTO.getDesignation());
            dto.setDescription(abonnementPrelevementDTO.getDescription());
            dto.setTaux(abonnementPrelevementDTO.getTaux());
            dto.setDateDebutAbonnement(abonnementPrelevementDTO.getDateDebutAbonnement());
            dto.setDateFinAbonnement(abonnementPrelevementDTO.getDateFinAbonnement());
            dto.setPeriodiciteAbonnementDesignation(abonnementPrelevementDTO.getPeriodiciteAbonnementDesignation());
            dto.setCompagnieTransportRaisonSociale(abonnementPrelevementDTO.getCompagnieTransportRaisonSociale());
            dto.setDeletedAt(abonnementPrelevementDTO.getDeletedAt());
            dto.setUpdatedAt(abonnementPrelevementDTO.getUpdatedAt());
            dto.setCreatedAt(abonnementPrelevementDTO.getCreatedAt());
            dto.setCreatedBy(abonnementPrelevementDTO.getCreatedBy());
            dto.setIsDeleted(abonnementPrelevementDTO.getIsDeleted());
            dto.setDeletedBy(abonnementPrelevementDTO.getDeletedBy());
            dto.setUpdatedBy(abonnementPrelevementDTO.getUpdatedBy());
            dto.setIsDeletedParam(abonnementPrelevementDTO.getIsDeletedParam());
            dto.setUpdatedAtParam(abonnementPrelevementDTO.getUpdatedAtParam());
            dto.setCreatedAtParam(abonnementPrelevementDTO.getCreatedAtParam());
            dto.setCreatedByParam(abonnementPrelevementDTO.getCreatedByParam());
            dto.setUpdatedByParam(abonnementPrelevementDTO.getUpdatedByParam());
            dto.setOrderDirection(abonnementPrelevementDTO.getOrderDirection());
            return dto;
        }
        return new ModeAbonnementDTO();
    }
}
