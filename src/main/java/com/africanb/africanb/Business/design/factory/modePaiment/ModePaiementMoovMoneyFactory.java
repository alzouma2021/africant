package com.africanb.africanb.Business.design.factory.modePaiment;


import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementMoovMoney;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementDTO;

public class ModePaiementMoovMoneyFactory implements ModePaimentDTOFactory {
    @Override
    public ModePaiementDTO createProduct(Object object) {
        if(object instanceof ModePaiementMoovMoney modePaiementMoovMoney){
            ModePaiementDTO dto = new ModePaiementDTO();
            dto.setId(modePaiementMoovMoney.getId());
            dto.setDesignation(modePaiementMoovMoney.getDesignation());
            dto.setDescription(modePaiementMoovMoney.getDescription());
            dto.setTelephoneGenerique(modePaiementMoovMoney.getTelephoneMoovMoney());
            dto.setTypeModePaiementDesignation(modePaiementMoovMoney.getTypeModePaiement().getDesignation());
            dto.setCompagnieTransportRaisonSociale(modePaiementMoovMoney.getCompagnieTransport().getRaisonSociale());
            dto.setDeletedAt(modePaiementMoovMoney.getDeletedAt()!=null?modePaiementMoovMoney.getDeletedAt().toString():null);
            dto.setUpdatedAt(modePaiementMoovMoney.getUpdatedAt()!=null?modePaiementMoovMoney.getUpdatedAt().toString():null);
            dto.setCreatedAt(modePaiementMoovMoney.getCreatedAt()!=null?modePaiementMoovMoney.getCreatedAt().toString():null);
            dto.setCreatedBy(modePaiementMoovMoney.getCreatedBy());
            dto.setIsDeleted(modePaiementMoovMoney.getIsDeleted());
            dto.setDeletedBy(modePaiementMoovMoney.getDeletedBy());
            dto.setUpdatedBy(modePaiementMoovMoney.getUpdatedBy());
            return dto;
        }
        return new ModePaiementDTO();
    }
}
