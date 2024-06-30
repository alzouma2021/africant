package com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage;


import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageLongDTO;

public class ValeurCaracteristiqueOffreVoyageLongDTOFactory implements ValeurCaracteristiqueOffreVoyageFactory {

    @Override
    public ValeurCaracteristiqueOffreVoyageDTO createProduct(Object object) {
        if(object instanceof ValeurCaracteristiqueOffreVoyageLongDTO offreVoyageLongDTO){
            ValeurCaracteristiqueOffreVoyageDTO dto = new ValeurCaracteristiqueOffreVoyageDTO();
                dto.setId(offreVoyageLongDTO.getId());
                dto.setDesignation(offreVoyageLongDTO.getDesignation());
                dto.setDescription(offreVoyageLongDTO.getDescription());
                dto.setValeurTexte(offreVoyageLongDTO.getValeur().toString());
                dto.setDeletedAt(offreVoyageLongDTO.getDeletedAt());
                dto.setUpdatedAt(offreVoyageLongDTO.getUpdatedAt());
                dto.setCreatedAt(offreVoyageLongDTO.getCreatedAt());
                dto.setCreatedBy(offreVoyageLongDTO.getCreatedBy());
                dto.setIsDeleted(offreVoyageLongDTO.getIsDeleted());
                dto.setDeletedBy(offreVoyageLongDTO.getDeletedBy());
                dto.setUpdatedBy(offreVoyageLongDTO.getUpdatedBy());
                dto.setIsDeletedParam(offreVoyageLongDTO.getIsDeletedParam());
                dto.setUpdatedAtParam(offreVoyageLongDTO.getUpdatedAtParam());
                dto.setCreatedAtParam(offreVoyageLongDTO.getCreatedAtParam());
                dto.setCreatedByParam(offreVoyageLongDTO.getCreatedByParam());
                dto.setUpdatedByParam(offreVoyageLongDTO.getUpdatedByParam());
                dto.setOffreVoyageDesignation(offreVoyageLongDTO.getOffreVoyageDesignation());
                dto.setOrderDirection(offreVoyageLongDTO.getOrderDirection());
                dto.setProprieteOffreVoyageDesignation(offreVoyageLongDTO.getProprieteOffreVoyageDesignation());
                dto.setTypeProprieteOffreVoyageDesignation(offreVoyageLongDTO.getTypeProprieteOffreVoyageDesignation());
            return dto;
        }

        return new ValeurCaracteristiqueOffreVoyageDTO();
    }
}
