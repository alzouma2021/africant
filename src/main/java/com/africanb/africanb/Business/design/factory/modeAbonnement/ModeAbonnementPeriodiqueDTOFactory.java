package com.africanb.africanb.Business.design.factory.modeAbonnement;

import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPeriodiqueDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.ModeAbonnementDTO;

public class ModeAbonnementPeriodiqueDTOFactory implements ModeAbonnementDTOFactory {
    @Override
    public ModeAbonnementDTO createProduct(Object object) {
        if(object instanceof AbonnementPeriodiqueDTO abonnementPeriodiqueDTO){
            ModeAbonnementDTO dto = new ModeAbonnementDTO();
            dto.setId(abonnementPeriodiqueDTO.getId());
            dto.setDesignation(abonnementPeriodiqueDTO.getDesignation());
            dto.setDescription(abonnementPeriodiqueDTO.getDescription());
            dto.setRedevance(abonnementPeriodiqueDTO.getRedevance());
            dto.setRedevancePublicite(abonnementPeriodiqueDTO.getRedevancePublicite());
            dto.setDateDebutAbonnement(abonnementPeriodiqueDTO.getDateDebutAbonnement());
            dto.setDateFinAbonnement(abonnementPeriodiqueDTO.getDateFinAbonnement());
            dto.setPeriodiciteAbonnementDesignation(abonnementPeriodiqueDTO.getPeriodiciteAbonnementDesignation());
            dto.setCompagnieTransportRaisonSociale(abonnementPeriodiqueDTO.getCompagnieTransportRaisonSociale());
            dto.setDeletedAt(abonnementPeriodiqueDTO.getDeletedAt());
            dto.setUpdatedAt(abonnementPeriodiqueDTO.getUpdatedAt());
            dto.setCreatedAt(abonnementPeriodiqueDTO.getCreatedAt());
            dto.setCreatedBy(abonnementPeriodiqueDTO.getCreatedBy());
            dto.setIsDeleted(abonnementPeriodiqueDTO.getIsDeleted());
            dto.setDeletedBy(abonnementPeriodiqueDTO.getDeletedBy());
            dto.setUpdatedBy(abonnementPeriodiqueDTO.getUpdatedBy());
            dto.setIsDeletedParam(abonnementPeriodiqueDTO.getIsDeletedParam());
            dto.setUpdatedAtParam(abonnementPeriodiqueDTO.getUpdatedAtParam());
            dto.setCreatedAtParam(abonnementPeriodiqueDTO.getCreatedAtParam());
            dto.setCreatedByParam(abonnementPeriodiqueDTO.getCreatedByParam());
            dto.setUpdatedByParam(abonnementPeriodiqueDTO.getUpdatedByParam());
            dto.setOrderDirection(abonnementPeriodiqueDTO.getOrderDirection());
            return dto;
        }
        return new ModeAbonnementDTO();
    }
}
