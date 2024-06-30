package com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageLong;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageLongDTO;

public class ValeurCaracteristiqueOffreVoyageLongFactory implements ValeurCaracteristiqueOffreVoyageFactory {

    @Override
    public ValeurCaracteristiqueOffreVoyageDTO createProduct(Object object) {
        if(object instanceof ValeurCaracteristiqueOffreVoyageLong itemLong){
            ValeurCaracteristiqueOffreVoyageDTO dto = new ValeurCaracteristiqueOffreVoyageDTO();
                dto.setId(itemLong.getId());
                dto.setDesignation(itemLong.getDesignation());
                dto.setDescription(itemLong.getDescription());
                dto.setValeurTexte(itemLong.getValeur().toString());
                dto.setDeletedAt(itemLong.getDeletedAt()==null?null:itemLong.getDeletedAt().toString());
                dto.setUpdatedAt(itemLong.getUpdatedAt()==null?null:itemLong.getDeletedAt().toString());
                dto.setCreatedAt(itemLong.getCreatedAt()==null?null:itemLong.getDeletedAt().toString());
                dto.setCreatedBy(itemLong.getCreatedBy());
                dto.setIsDeleted(itemLong.getIsDeleted());
                dto.setDeletedBy(itemLong.getDeletedBy());
                dto.setUpdatedBy(itemLong.getUpdatedBy());
                dto.setOffreVoyageDesignation(itemLong.getOffreVoyage().getDesignation());
                dto.setProprieteOffreVoyageDesignation(itemLong.getProprieteOffreVoyage().getDesignation());
            return dto;
        }

        return new ValeurCaracteristiqueOffreVoyageDTO();
    }
}
