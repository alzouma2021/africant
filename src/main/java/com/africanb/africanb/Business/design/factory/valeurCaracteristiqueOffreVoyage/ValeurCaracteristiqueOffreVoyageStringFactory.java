package com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageString;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;

public class ValeurCaracteristiqueOffreVoyageStringFactory implements ValeurCaracteristiqueOffreVoyageFactory {

    @Override
    public ValeurCaracteristiqueOffreVoyageDTO createProduct(Object object) {
        if(object instanceof ValeurCaracteristiqueOffreVoyageString itemString){
            ValeurCaracteristiqueOffreVoyageDTO dto = new ValeurCaracteristiqueOffreVoyageDTO();
                dto.setId(itemString.getId());
                dto.setDesignation(itemString.getDesignation());
                dto.setDescription(itemString.getDescription());
                dto.setValeurTexte(itemString.getValeur());
                dto.setDeletedAt(itemString.getDeletedAt()==null?null:itemString.getDeletedAt().toString());
                dto.setUpdatedAt(itemString.getUpdatedAt()==null?null:itemString.getUpdatedAt().toString());
                dto.setCreatedAt(itemString.getCreatedAt()==null?null:itemString.getCreatedAt().toString());
                dto.setCreatedBy(itemString.getCreatedBy());
                dto.setIsDeleted(itemString.getIsDeleted());
                dto.setDeletedBy(itemString.getDeletedBy());
                dto.setUpdatedBy(itemString.getUpdatedBy());
                dto.setOffreVoyageDesignation(itemString.getOffreVoyage().getDesignation());
                dto.setProprieteOffreVoyageDesignation(itemString.getProprieteOffreVoyage().getDesignation());
            return dto;
        }
        return new ValeurCaracteristiqueOffreVoyageDTO();
    }
}
