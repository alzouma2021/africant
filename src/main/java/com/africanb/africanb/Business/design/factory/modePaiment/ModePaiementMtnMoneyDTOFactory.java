package com.africanb.africanb.Business.design.factory.modePaiment;

import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementMtnMoneyDTO;

public class ModePaiementMtnMoneyDTOFactory implements ModePaimentDTOFactory {
    @Override
    public ModePaiementDTO createProduct(Object object) {
        if(object instanceof ModePaiementMtnMoneyDTO mtnMoneyDTO){
            ModePaiementDTO dto = new ModePaiementDTO();
            dto.setId(mtnMoneyDTO.getId());
            dto.setDesignation(mtnMoneyDTO.getDesignation());
            dto.setDescription(mtnMoneyDTO.getDescription());
            dto.setTelephoneGenerique(mtnMoneyDTO.getTelephoneMtnMoney());
            dto.setTypeModePaiementDesignation(mtnMoneyDTO.getTypeModePaiementDesignation());
            dto.setCompagnieTransportRaisonSociale(mtnMoneyDTO.getCompagnieTransportRaisonSociale());
            dto.setDeletedAt(mtnMoneyDTO.getDeletedAt());
            dto.setUpdatedAt(mtnMoneyDTO.getUpdatedAt());
            dto.setCreatedAt(mtnMoneyDTO.getCreatedAt());
            dto.setCreatedBy(mtnMoneyDTO.getCreatedBy());
            dto.setIsDeleted(mtnMoneyDTO.getIsDeleted());
            dto.setDeletedBy(mtnMoneyDTO.getDeletedBy());
            dto.setUpdatedBy(mtnMoneyDTO.getUpdatedBy());
            dto.setIsDeletedParam(mtnMoneyDTO.getIsDeletedParam());
            dto.setUpdatedAtParam(mtnMoneyDTO.getUpdatedAtParam());
            dto.setCreatedAtParam(mtnMoneyDTO.getCreatedAtParam());
            dto.setCreatedByParam(mtnMoneyDTO.getCreatedByParam());
            dto.setUpdatedByParam(mtnMoneyDTO.getUpdatedByParam());
            dto.setOrderDirection(mtnMoneyDTO.getOrderDirection());
            return dto;
        }
        return new ModePaiementDTO();
    }
}
