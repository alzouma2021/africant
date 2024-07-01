package com.africanb.africanb.Business.design.factory.modePaiment;

import com.africanb.africanb.helper.dto.compagnie.ModePaiement.*;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ModePaiementUtils {

    public static ModePaiementDTO transformAbstractClassIntoChildClass(ModePaiementDTO dto) {
        if (dto.getTypeModePaiementDesignation() != null){
            switch (dto.getTypeModePaiementDesignation()) {
                case ProjectConstants.REF_ELEMENT_MODE_PAIEMENT_MTN_MONEY -> {
                    ModePaiementMtnMoneyDTO modePaiementMtnMoneyDTO = new ModePaiementMtnMoneyDTO();
                    modePaiementMtnMoneyDTO.setId(dto.getId());
                    modePaiementMtnMoneyDTO.setDesignation(dto.getDesignation());
                    modePaiementMtnMoneyDTO.setDescription(dto.getDescription());
                    modePaiementMtnMoneyDTO.setTelephoneMtnMoney(dto.getTelephoneGenerique()==null?null:dto.getTelephoneGenerique());
                    modePaiementMtnMoneyDTO.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    modePaiementMtnMoneyDTO.setTypeModePaiementDesignation(dto.getTypeModePaiementDesignation());
                    modePaiementMtnMoneyDTO.setIsDeleted(dto.getIsDeleted());
                    modePaiementMtnMoneyDTO.setUpdatedAt(dto.getUpdatedAt());
                    modePaiementMtnMoneyDTO.setCreatedAt(dto.getCreatedAt());
                    modePaiementMtnMoneyDTO.setDeletedAt(dto.getDeletedAt());
                    modePaiementMtnMoneyDTO.setUpdatedBy(dto.getUpdatedBy());
                    modePaiementMtnMoneyDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementMtnMoneyDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementMtnMoneyDTO.setCreatedAtParam(dto.getCreatedAtParam());
                    modePaiementMtnMoneyDTO.setUpdatedAtParam(dto.getUpdatedAtParam());
                    modePaiementMtnMoneyDTO.setIsDeletedParam(dto.getIsDeletedParam());
                    modePaiementMtnMoneyDTO.setOrderField(dto.getOrderField());
                    modePaiementMtnMoneyDTO.setOrderDirection(dto.getOrderDirection());
                    return modePaiementMtnMoneyDTO;
                }
                case ProjectConstants.REF_ELEMENT_MODE_PAIEMENT_ORANGE_MONEY -> {
                    ModePaiementOrangeMoneyDTO modePaiementOrangeMoneyDTO = new ModePaiementOrangeMoneyDTO();
                    modePaiementOrangeMoneyDTO.setId(dto.getId());
                    modePaiementOrangeMoneyDTO.setDesignation(dto.getDesignation());
                    modePaiementOrangeMoneyDTO.setDescription(dto.getDescription());
                    modePaiementOrangeMoneyDTO.setTelephoneOrangeMoney(dto.getTelephoneGenerique()==null?null:dto.getTelephoneGenerique());
                    modePaiementOrangeMoneyDTO.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    modePaiementOrangeMoneyDTO.setTypeModePaiementDesignation(dto.getTypeModePaiementDesignation());
                    modePaiementOrangeMoneyDTO.setIsDeleted(dto.getIsDeleted());
                    modePaiementOrangeMoneyDTO.setUpdatedAt(dto.getUpdatedAt());
                    modePaiementOrangeMoneyDTO.setCreatedAt(dto.getCreatedAt());
                    modePaiementOrangeMoneyDTO.setDeletedAt(dto.getDeletedAt());
                    modePaiementOrangeMoneyDTO.setUpdatedBy(dto.getUpdatedBy());
                    modePaiementOrangeMoneyDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementOrangeMoneyDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementOrangeMoneyDTO.setCreatedAtParam(dto.getCreatedAtParam());
                    modePaiementOrangeMoneyDTO.setUpdatedAtParam(dto.getUpdatedAtParam());
                    modePaiementOrangeMoneyDTO.setIsDeletedParam(dto.getIsDeletedParam());
                    modePaiementOrangeMoneyDTO.setOrderField(dto.getOrderField());
                    modePaiementOrangeMoneyDTO.setOrderDirection(dto.getOrderDirection());
                    return modePaiementOrangeMoneyDTO;
                }
                case ProjectConstants.REF_ELEMENT_MODE_PAIEMENT_MOOV_MONEY -> {
                    ModePaiementMoovMoneyDTO modePaiementMoovMoneyDTO = new ModePaiementMoovMoneyDTO();
                    modePaiementMoovMoneyDTO.setId(dto.getId());
                    modePaiementMoovMoneyDTO.setDesignation(dto.getDesignation());
                    modePaiementMoovMoneyDTO.setDescription(dto.getDescription());
                    modePaiementMoovMoneyDTO.setTelephoneMoovMoney(dto.getTelephoneGenerique()==null?null:dto.getTelephoneGenerique());
                    modePaiementMoovMoneyDTO.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    modePaiementMoovMoneyDTO.setTypeModePaiementDesignation(dto.getTypeModePaiementDesignation());
                    modePaiementMoovMoneyDTO.setIsDeleted(dto.getIsDeleted());
                    modePaiementMoovMoneyDTO.setUpdatedAt(dto.getUpdatedAt());
                    modePaiementMoovMoneyDTO.setCreatedAt(dto.getCreatedAt());
                    modePaiementMoovMoneyDTO.setDeletedAt(dto.getDeletedAt());
                    modePaiementMoovMoneyDTO.setUpdatedBy(dto.getUpdatedBy());
                    modePaiementMoovMoneyDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementMoovMoneyDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementMoovMoneyDTO.setCreatedAtParam(dto.getCreatedAtParam());
                    modePaiementMoovMoneyDTO.setUpdatedAtParam(dto.getUpdatedAtParam());
                    modePaiementMoovMoneyDTO.setIsDeletedParam(dto.getIsDeletedParam());
                    modePaiementMoovMoneyDTO.setOrderField(dto.getOrderField());
                    modePaiementMoovMoneyDTO.setOrderDirection(dto.getOrderDirection());
                    return modePaiementMoovMoneyDTO;
                }
                case ProjectConstants.REF_ELEMENT_MODE_PAIEMENT_WAVE_MONEY -> {
                    ModePaiementWaveDTO modePaiementWaveDTO = new ModePaiementWaveDTO();
                    modePaiementWaveDTO.setId(dto.getId());
                    modePaiementWaveDTO.setDesignation(dto.getDesignation());
                    modePaiementWaveDTO.setDescription(dto.getDescription());
                    modePaiementWaveDTO.setTelephoneWave(dto.getTelephoneGenerique()==null?null:dto.getTelephoneGenerique());
                    modePaiementWaveDTO.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    modePaiementWaveDTO.setTypeModePaiementDesignation(dto.getTypeModePaiementDesignation());
                    modePaiementWaveDTO.setIsDeleted(dto.getIsDeleted());
                    modePaiementWaveDTO.setUpdatedAt(dto.getUpdatedAt());
                    modePaiementWaveDTO.setCreatedAt(dto.getCreatedAt());
                    modePaiementWaveDTO.setDeletedAt(dto.getDeletedAt());
                    modePaiementWaveDTO.setUpdatedBy(dto.getUpdatedBy());
                    modePaiementWaveDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementWaveDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementWaveDTO.setCreatedAtParam(dto.getCreatedAtParam());
                    modePaiementWaveDTO.setUpdatedAtParam(dto.getUpdatedAtParam());
                    modePaiementWaveDTO.setIsDeletedParam(dto.getIsDeletedParam());
                    modePaiementWaveDTO.setOrderField(dto.getOrderField());
                    modePaiementWaveDTO.setOrderDirection(dto.getOrderDirection());
                    return modePaiementWaveDTO;
                }
                case ProjectConstants.REF_ELEMENT_MODE_PAIEMENT_EN_ESPECE -> {
                    ModePaiementEnEspeceDTO modePaiementEnEspeceDTO = new ModePaiementEnEspeceDTO();
                    modePaiementEnEspeceDTO.setId(dto.getId());
                    modePaiementEnEspeceDTO.setDesignation(dto.getDesignation());
                    modePaiementEnEspeceDTO.setDescription(dto.getDescription());
                    modePaiementEnEspeceDTO.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    modePaiementEnEspeceDTO.setTypeModePaiementDesignation(dto.getTypeModePaiementDesignation());
                    modePaiementEnEspeceDTO.setIsDeleted(dto.getIsDeleted());
                    modePaiementEnEspeceDTO.setUpdatedAt(dto.getUpdatedAt());
                    modePaiementEnEspeceDTO.setCreatedAt(dto.getCreatedAt());
                    modePaiementEnEspeceDTO.setDeletedAt(dto.getDeletedAt());
                    modePaiementEnEspeceDTO.setUpdatedBy(dto.getUpdatedBy());
                    modePaiementEnEspeceDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementEnEspeceDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementEnEspeceDTO.setCreatedAtParam(dto.getCreatedAtParam());
                    modePaiementEnEspeceDTO.setUpdatedAtParam(dto.getUpdatedAtParam());
                    modePaiementEnEspeceDTO.setIsDeletedParam(dto.getIsDeletedParam());
                    modePaiementEnEspeceDTO.setOrderField(dto.getOrderField());
                    modePaiementEnEspeceDTO.setOrderDirection(dto.getOrderDirection());
                    return modePaiementEnEspeceDTO;
                }
                default -> {
                    return new ModePaiementDTO();
                }
            }
        }
        return new ModePaiementDTO();
    }
}
