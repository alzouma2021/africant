package com.africanb.africanb.Business.design.factory.modePaiment;

import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementOrangeMoneyDTO;

public class ModePaiementOrangeMoneyDTOFactory implements ModePaimentDTOFactory {
    @Override
    public ModePaiementDTO createProduct(Object object) {
        if(object instanceof ModePaiementOrangeMoneyDTO orangeMoneyDTO){
            ModePaiementDTO dto = new ModePaiementDTO();
            dto.setId(orangeMoneyDTO.getId());
            dto.setDesignation(orangeMoneyDTO.getDesignation());
            dto.setDescription(orangeMoneyDTO.getDescription());
            dto.setTelephoneGenerique(orangeMoneyDTO.getTelephoneOrangeMoney());
            dto.setTypeModePaiementDesignation(orangeMoneyDTO.getTypeModePaiementDesignation());
            dto.setCompagnieTransportRaisonSociale(orangeMoneyDTO.getCompagnieTransportRaisonSociale());
            dto.setDeletedAt(orangeMoneyDTO.getDeletedAt());
            dto.setUpdatedAt(orangeMoneyDTO.getUpdatedAt());
            dto.setCreatedAt(orangeMoneyDTO.getCreatedAt());
            dto.setCreatedBy(orangeMoneyDTO.getCreatedBy());
            dto.setIsDeleted(orangeMoneyDTO.getIsDeleted());
            dto.setDeletedBy(orangeMoneyDTO.getDeletedBy());
            dto.setUpdatedBy(orangeMoneyDTO.getUpdatedBy());
            dto.setIsDeletedParam(orangeMoneyDTO.getIsDeletedParam());
            dto.setUpdatedAtParam(orangeMoneyDTO.getUpdatedAtParam());
            dto.setCreatedAtParam(orangeMoneyDTO.getCreatedAtParam());
            dto.setCreatedByParam(orangeMoneyDTO.getCreatedByParam());
            dto.setUpdatedByParam(orangeMoneyDTO.getUpdatedByParam());
            dto.setOrderDirection(orangeMoneyDTO.getOrderDirection());
            return dto;
        }
        return new ModePaiementDTO();
    }
}
