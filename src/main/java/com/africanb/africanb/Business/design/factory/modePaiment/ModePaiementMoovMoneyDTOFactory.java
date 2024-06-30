package com.africanb.africanb.Business.design.factory.modePaiment;


import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementMoovMoneyDTO;

public class ModePaiementMoovMoneyDTOFactory implements ModePaimentDTOFactory {
    @Override
    public ModePaiementDTO createProduct(Object object) {
        if(object instanceof ModePaiementMoovMoneyDTO moovMoneyDTO){
            ModePaiementDTO dto = new ModePaiementDTO();
            dto.setId(moovMoneyDTO.getId());
            dto.setDesignation(moovMoneyDTO.getDesignation());
            dto.setDescription(moovMoneyDTO.getDescription());
            dto.setTelephoneGenerique(moovMoneyDTO.getTelephoneMoovMoney());
            dto.setTypeModePaiementDesignation(moovMoneyDTO.getTypeModePaiementDesignation());
            dto.setCompagnieTransportRaisonSociale(moovMoneyDTO.getCompagnieTransportRaisonSociale());
            dto.setDeletedAt(moovMoneyDTO.getDeletedAt());
            dto.setUpdatedAt(moovMoneyDTO.getUpdatedAt());
            dto.setCreatedAt(moovMoneyDTO.getCreatedAt());
            dto.setCreatedBy(moovMoneyDTO.getCreatedBy());
            dto.setIsDeleted(moovMoneyDTO.getIsDeleted());
            dto.setDeletedBy(moovMoneyDTO.getDeletedBy());
            dto.setUpdatedBy(moovMoneyDTO.getUpdatedBy());
            dto.setIsDeletedParam(moovMoneyDTO.getIsDeletedParam());
            dto.setUpdatedAtParam(moovMoneyDTO.getUpdatedAtParam());
            dto.setCreatedAtParam(moovMoneyDTO.getCreatedAtParam());
            dto.setCreatedByParam(moovMoneyDTO.getCreatedByParam());
            dto.setUpdatedByParam(moovMoneyDTO.getUpdatedByParam());
            dto.setOrderDirection(moovMoneyDTO.getOrderDirection());
            return dto;
        }
        return new ModePaiementDTO();
    }
}
