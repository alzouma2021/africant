package com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage;


import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageStringDTO;

public class ValeurCaracteristiqueOffreVoyageStringDTOFactory implements ValeurCaracteristiqueOffreVoyageFactory {

    @Override
    public ValeurCaracteristiqueOffreVoyageDTO createProduct(Object object) {
        if(object instanceof ValeurCaracteristiqueOffreVoyageStringDTO offreVoyageStringDTO){
            ValeurCaracteristiqueOffreVoyageDTO dto = new ValeurCaracteristiqueOffreVoyageDTO();
                dto.setId(offreVoyageStringDTO.getId());
                dto.setDesignation(offreVoyageStringDTO.getDesignation());
                dto.setDescription(offreVoyageStringDTO.getDescription());
                dto.setValeurTexte(offreVoyageStringDTO.getValeur());
                dto.setDeletedAt(offreVoyageStringDTO.getDeletedAt());
                dto.setUpdatedAt(offreVoyageStringDTO.getUpdatedAt());
                dto.setCreatedAt(offreVoyageStringDTO.getCreatedAt());
                dto.setCreatedBy(offreVoyageStringDTO.getCreatedBy());
                dto.setIsDeleted(offreVoyageStringDTO.getIsDeleted());
                dto.setDeletedBy(offreVoyageStringDTO.getDeletedBy());
                dto.setUpdatedBy(offreVoyageStringDTO.getUpdatedBy());
                dto.setIsDeletedParam(offreVoyageStringDTO.getIsDeletedParam());
                dto.setUpdatedAtParam(offreVoyageStringDTO.getUpdatedAtParam());
                dto.setCreatedAtParam(offreVoyageStringDTO.getCreatedAtParam());
                dto.setCreatedByParam(offreVoyageStringDTO.getCreatedByParam());
                dto.setUpdatedByParam(offreVoyageStringDTO.getUpdatedByParam());
                dto.setOffreVoyageDesignation(offreVoyageStringDTO.getOffreVoyageDesignation());
                dto.setOrderDirection(offreVoyageStringDTO.getOrderDirection());
                dto.setProprieteOffreVoyageDesignation(offreVoyageStringDTO.getProprieteOffreVoyageDesignation());
                dto.setTypeProprieteOffreVoyageDesignation(offreVoyageStringDTO.getTypeProprieteOffreVoyageDesignation());
            return dto;
        }
        return new ValeurCaracteristiqueOffreVoyageDTO();
    }
}
