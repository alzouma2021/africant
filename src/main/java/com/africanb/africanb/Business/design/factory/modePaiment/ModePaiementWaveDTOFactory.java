package com.africanb.africanb.Business.design.factory.modePaiment;

import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementWaveDTO;

public class ModePaiementWaveDTOFactory implements ModePaimentDTOFactory {
    @Override
    public ModePaiementDTO createProduct(Object object) {
        if(object instanceof ModePaiementWaveDTO modePaiementWaveDTO){
            ModePaiementDTO dto = new ModePaiementDTO();
            dto.setId(modePaiementWaveDTO.getId());
            dto.setDesignation(modePaiementWaveDTO.getDesignation());
            dto.setDescription(modePaiementWaveDTO.getDescription());
            dto.setTelephoneGenerique(modePaiementWaveDTO.getTelephoneWave());
            dto.setTypeModePaiementDesignation(modePaiementWaveDTO.getTypeModePaiementDesignation());
            dto.setCompagnieTransportRaisonSociale(modePaiementWaveDTO.getCompagnieTransportRaisonSociale());
            dto.setDeletedAt(modePaiementWaveDTO.getDeletedAt());
            dto.setUpdatedAt(modePaiementWaveDTO.getUpdatedAt());
            dto.setCreatedAt(modePaiementWaveDTO.getCreatedAt());
            dto.setCreatedBy(modePaiementWaveDTO.getCreatedBy());
            dto.setIsDeleted(modePaiementWaveDTO.getIsDeleted());
            dto.setDeletedBy(modePaiementWaveDTO.getDeletedBy());
            dto.setUpdatedBy(modePaiementWaveDTO.getUpdatedBy());
            dto.setIsDeletedParam(modePaiementWaveDTO.getIsDeletedParam());
            dto.setUpdatedAtParam(modePaiementWaveDTO.getUpdatedAtParam());
            dto.setCreatedAtParam(modePaiementWaveDTO.getCreatedAtParam());
            dto.setCreatedByParam(modePaiementWaveDTO.getCreatedByParam());
            dto.setUpdatedByParam(modePaiementWaveDTO.getUpdatedByParam());
            dto.setOrderDirection(modePaiementWaveDTO.getOrderDirection());
            return dto;
        }
        return new ModePaiementWaveDTO();
    }
}
