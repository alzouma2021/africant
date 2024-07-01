package com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage;

import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageBooleanDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageLongDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageStringDTO;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValeurCaracteristiqueOffreVoyageUtils {

    public static ValeurCaracteristiqueOffreVoyageDTO transformAbstractClassIntoChildClass(ValeurCaracteristiqueOffreVoyageDTO dto) {
        if(dto.getTypeProprieteOffreVoyageDesignation() != null) {
            switch (dto.getTypeProprieteOffreVoyageDesignation()) {
                case ProjectConstants.REF_ELEMENT_TYPE_LONG -> {
                    ValeurCaracteristiqueOffreVoyageLongDTO rtn = new ValeurCaracteristiqueOffreVoyageLongDTO();
                    rtn.setId(dto.getId());
                    rtn.setDesignation(dto.getDesignation());
                    rtn.setDescription(dto.getDescription());
                    rtn.setValeurTexte(dto.getValeurTexte());
                    rtn.setProprieteOffreVoyageDesignation(dto.getProprieteOffreVoyageDesignation());
                    rtn.setOffreVoyageDesignation(dto.getOffreVoyageDesignation());
                    rtn.setIsDeleted(dto.getIsDeleted());
                    rtn.setUpdatedAt(dto.getUpdatedAt());
                    rtn.setCreatedAt(dto.getCreatedAt());
                    rtn.setDeletedAt(dto.getDeletedAt());
                    rtn.setUpdatedBy(dto.getUpdatedBy());
                    rtn.setCreatedBy(dto.getCreatedBy());
                    rtn.setCreatedBy(dto.getCreatedBy());
                    rtn.setCreatedAtParam(dto.getCreatedAtParam());
                    rtn.setUpdatedAtParam(dto.getUpdatedAtParam());
                    rtn.setIsDeletedParam(dto.getIsDeletedParam());
                    rtn.setOrderField(dto.getOrderField());
                    rtn.setOrderDirection(dto.getOrderDirection());
                    return rtn;
                }
                case ProjectConstants.REF_ELEMENT_TYPE_BOOLEAN -> {
                    ValeurCaracteristiqueOffreVoyageBooleanDTO rtnBoolean = new ValeurCaracteristiqueOffreVoyageBooleanDTO();
                    rtnBoolean.setId(dto.getId());
                    rtnBoolean.setDesignation(dto.getDesignation());
                    rtnBoolean.setDescription(dto.getDescription());
                    rtnBoolean.setValeurTexte(dto.getValeurTexte());
                    rtnBoolean.setProprieteOffreVoyageDesignation(dto.getProprieteOffreVoyageDesignation());
                    rtnBoolean.setOffreVoyageDesignation(dto.getOffreVoyageDesignation());
                    rtnBoolean.setIsDeleted(dto.getIsDeleted());
                    rtnBoolean.setUpdatedAt(dto.getUpdatedAt());
                    rtnBoolean.setCreatedAt(dto.getCreatedAt());
                    rtnBoolean.setDeletedAt(dto.getDeletedAt());
                    rtnBoolean.setUpdatedBy(dto.getUpdatedBy());
                    rtnBoolean.setCreatedBy(dto.getCreatedBy());
                    rtnBoolean.setCreatedBy(dto.getCreatedBy());
                    rtnBoolean.setCreatedAtParam(dto.getCreatedAtParam());
                    rtnBoolean.setUpdatedAtParam(dto.getUpdatedAtParam());
                    rtnBoolean.setIsDeletedParam(dto.getIsDeletedParam());
                    rtnBoolean.setOrderField(dto.getOrderField());
                    rtnBoolean.setOrderDirection(dto.getOrderDirection());
                    return rtnBoolean;
                }
                case ProjectConstants.REF_ELEMENT_TYPE_STRING -> {
                    ValeurCaracteristiqueOffreVoyageStringDTO rtnString = new ValeurCaracteristiqueOffreVoyageStringDTO();
                    rtnString.setId(dto.getId());
                    rtnString.setDesignation(dto.getDesignation());
                    rtnString.setDescription(dto.getDescription());
                    rtnString.setValeurTexte(dto.getValeurTexte());
                    rtnString.setProprieteOffreVoyageDesignation(dto.getProprieteOffreVoyageDesignation());
                    rtnString.setOffreVoyageDesignation(dto.getOffreVoyageDesignation());
                    rtnString.setIsDeleted(dto.getIsDeleted());
                    rtnString.setUpdatedAt(dto.getUpdatedAt());
                    rtnString.setCreatedAt(dto.getCreatedAt());
                    rtnString.setDeletedAt(dto.getDeletedAt());
                    rtnString.setUpdatedBy(dto.getUpdatedBy());
                    rtnString.setCreatedBy(dto.getCreatedBy());
                    rtnString.setCreatedBy(dto.getCreatedBy());
                    rtnString.setCreatedAtParam(dto.getCreatedAtParam());
                    rtnString.setUpdatedAtParam(dto.getUpdatedAtParam());
                    rtnString.setIsDeletedParam(dto.getIsDeletedParam());
                    rtnString.setOrderField(dto.getOrderField());
                    rtnString.setOrderDirection(dto.getOrderDirection());
                    return rtnString;
                }
                default ->{
                    return new ValeurCaracteristiqueOffreVoyageDTO();
                }
            }
        }
        return new ValeurCaracteristiqueOffreVoyageDTO();
    }

}
