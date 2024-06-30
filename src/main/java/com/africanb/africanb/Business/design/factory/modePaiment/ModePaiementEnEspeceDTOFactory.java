package com.africanb.africanb.Business.design.factory.modePaiment;

import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementEnEspeceDTO;

public class ModePaiementEnEspeceDTOFactory implements ModePaimentDTOFactory {
    @Override
    public ModePaiementDTO createProduct(Object object) {
        if(object instanceof ModePaiementEnEspeceDTO enEspeceDTO){
            ModePaiementDTO dto = new ModePaiementDTO();
            dto.setId(enEspeceDTO.getId());
            dto.setDesignation(enEspeceDTO.getDesignation());
            dto.setDescription(enEspeceDTO.getDescription());
            dto.setTypeModePaiementDesignation(enEspeceDTO.getTypeModePaiementDesignation());
            dto.setCompagnieTransportRaisonSociale(enEspeceDTO.getCompagnieTransportRaisonSociale());
            dto.setDeletedAt(enEspeceDTO.getDeletedAt());
            dto.setUpdatedAt(enEspeceDTO.getUpdatedAt());
            dto.setCreatedAt(enEspeceDTO.getCreatedAt());
            dto.setCreatedBy(enEspeceDTO.getCreatedBy());
            dto.setIsDeleted(enEspeceDTO.getIsDeleted());
            dto.setDeletedBy(enEspeceDTO.getDeletedBy());
            dto.setUpdatedBy(enEspeceDTO.getUpdatedBy());
            dto.setIsDeletedParam(enEspeceDTO.getIsDeletedParam());
            dto.setUpdatedAtParam(enEspeceDTO.getUpdatedAtParam());
            dto.setCreatedAtParam(enEspeceDTO.getCreatedAtParam());
            dto.setCreatedByParam(enEspeceDTO.getCreatedByParam());
            dto.setUpdatedByParam(enEspeceDTO.getUpdatedByParam());
            dto.setOrderDirection(enEspeceDTO.getOrderDirection());
            return dto;
        }
        return new ModePaiementDTO();
    }
}
