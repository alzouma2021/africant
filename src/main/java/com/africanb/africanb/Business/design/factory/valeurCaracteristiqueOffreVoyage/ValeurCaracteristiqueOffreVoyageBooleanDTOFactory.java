package com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage;

import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageBooleanDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;

public class ValeurCaracteristiqueOffreVoyageBooleanDTOFactory implements ValeurCaracteristiqueOffreVoyageFactory {
    @Override
    public ValeurCaracteristiqueOffreVoyageDTO createProduct(Object object) {
        if(object instanceof ValeurCaracteristiqueOffreVoyageBooleanDTO voyageBooleanDTO){
            ValeurCaracteristiqueOffreVoyageDTO dto = new ValeurCaracteristiqueOffreVoyageDTO();
                dto.setId(voyageBooleanDTO.getId());
                dto.setDesignation(voyageBooleanDTO.getDesignation());
                dto.setDescription(voyageBooleanDTO.getDescription());
                dto.setValeurTexte(voyageBooleanDTO.getValeur().toString());
                dto.setDeletedAt(voyageBooleanDTO.getDeletedAt());
                dto.setUpdatedAt(voyageBooleanDTO.getUpdatedAt());
                dto.setCreatedAt(voyageBooleanDTO.getCreatedAt());
                dto.setCreatedBy(voyageBooleanDTO.getCreatedBy());
                dto.setIsDeleted(voyageBooleanDTO.getIsDeleted());
                dto.setDeletedBy(voyageBooleanDTO.getDeletedBy());
                dto.setUpdatedBy(voyageBooleanDTO.getUpdatedBy());
                dto.setIsDeletedParam(voyageBooleanDTO.getIsDeletedParam());
                dto.setUpdatedAtParam(voyageBooleanDTO.getUpdatedAtParam());
                dto.setCreatedAtParam(voyageBooleanDTO.getCreatedAtParam());
                dto.setCreatedByParam(voyageBooleanDTO.getCreatedByParam());
                dto.setUpdatedByParam(voyageBooleanDTO.getUpdatedByParam());
                dto.setOffreVoyageDesignation(voyageBooleanDTO.getOffreVoyageDesignation());
                dto.setOrderDirection(voyageBooleanDTO.getOrderDirection());
                dto.setProprieteOffreVoyageDesignation(voyageBooleanDTO.getProprieteOffreVoyageDesignation());
                dto.setTypeProprieteOffreVoyageDesignation(voyageBooleanDTO.getTypeProprieteOffreVoyageDesignation());
            return dto;
        }
        return new ValeurCaracteristiqueOffreVoyageDTO();
    }
}
