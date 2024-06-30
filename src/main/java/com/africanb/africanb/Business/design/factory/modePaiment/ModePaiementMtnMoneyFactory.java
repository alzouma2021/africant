package com.africanb.africanb.Business.design.factory.modePaiment;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementMtnMoney;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementDTO;


public class ModePaiementMtnMoneyFactory implements ModePaimentDTOFactory {
    @Override
    public ModePaiementDTO createProduct(Object object) {
        if(object instanceof ModePaiementMtnMoney modePaiementMtnMoney){
            ModePaiementDTO dto = new ModePaiementDTO();
            dto.setId(modePaiementMtnMoney.getId());
            dto.setDesignation(modePaiementMtnMoney.getDesignation());
            dto.setDescription(modePaiementMtnMoney.getDescription());
            dto.setTelephoneGenerique(modePaiementMtnMoney.getTelephoneMtnMoney());
            dto.setTypeModePaiementDesignation(modePaiementMtnMoney.getTypeModePaiement().getDesignation());
            dto.setCompagnieTransportRaisonSociale(modePaiementMtnMoney.getCompagnieTransport().getRaisonSociale());
            dto.setDeletedAt(modePaiementMtnMoney.getDeletedAt()!=null?modePaiementMtnMoney.getDeletedAt().toString():null);
            dto.setUpdatedAt(modePaiementMtnMoney.getUpdatedAt()!=null?modePaiementMtnMoney.getUpdatedAt().toString():null);
            dto.setCreatedAt(modePaiementMtnMoney.getCreatedAt()!=null?modePaiementMtnMoney.getCreatedAt().toString():null);
            dto.setCreatedBy(modePaiementMtnMoney.getCreatedBy());
            dto.setIsDeleted(modePaiementMtnMoney.getIsDeleted());
            dto.setDeletedBy(modePaiementMtnMoney.getDeletedBy());
            dto.setUpdatedBy(modePaiementMtnMoney.getUpdatedBy());
            return dto;
        }
        return new ModePaiementDTO();
    }
}
