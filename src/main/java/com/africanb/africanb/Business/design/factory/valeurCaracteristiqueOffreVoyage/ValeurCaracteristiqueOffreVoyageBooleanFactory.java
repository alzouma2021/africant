package com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageBoolean;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;

public class ValeurCaracteristiqueOffreVoyageBooleanFactory implements ValeurCaracteristiqueOffreVoyageFactory {
    @Override
    public ValeurCaracteristiqueOffreVoyageDTO createProduct(Object object) {
        if(object instanceof ValeurCaracteristiqueOffreVoyageBoolean itemBoolean){
            ValeurCaracteristiqueOffreVoyageDTO dto = new ValeurCaracteristiqueOffreVoyageDTO();
                dto.setId( itemBoolean.getId());
                dto.setDesignation( itemBoolean.getDesignation());
                dto.setDescription( itemBoolean.getDescription());
                dto.setValeurTexte(itemBoolean.getValeur().toString());
                dto.setDeletedAt( itemBoolean.getDeletedAt()==null?null:itemBoolean.getDeletedAt().toString());
                dto.setUpdatedAt( itemBoolean.getUpdatedAt()==null?null:itemBoolean.getUpdatedAt().toString());
                dto.setCreatedAt( itemBoolean.getCreatedAt()==null?null:itemBoolean.getCreatedAt().toString());
                dto.setCreatedBy( itemBoolean.getCreatedBy());
                dto.setIsDeleted( itemBoolean.getIsDeleted());
                dto.setDeletedBy( itemBoolean.getDeletedBy());
                dto.setUpdatedBy( itemBoolean.getUpdatedBy());
                dto.setOffreVoyageDesignation(itemBoolean.getOffreVoyage().getDesignation());
                dto.setProprieteOffreVoyageDesignation(itemBoolean.getProprieteOffreVoyage().getDesignation());
            return dto;
        }
        return new ValeurCaracteristiqueOffreVoyageDTO();
    }
}
