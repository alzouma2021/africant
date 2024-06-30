package com.africanb.africanb.Business.design.factory.modePaiment;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementOrangeMoney;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementDTO;

public class ModePaiementOrangeMoneyFactory implements ModePaimentDTOFactory {

    @Override
    public ModePaiementDTO createProduct(Object object) {
        if(object instanceof ModePaiementOrangeMoney modePaiementOrangeMoney){
            ModePaiementDTO dto = new ModePaiementDTO();
            dto.setId(modePaiementOrangeMoney.getId());
            dto.setDesignation(modePaiementOrangeMoney.getDesignation());
            dto.setDescription(modePaiementOrangeMoney.getDescription());
            dto.setTelephoneGenerique(modePaiementOrangeMoney.getTelephoneOrangeMoney());
            dto.setTypeModePaiementDesignation(modePaiementOrangeMoney.getTypeModePaiement().getDesignation());
            dto.setCompagnieTransportRaisonSociale(modePaiementOrangeMoney.getCompagnieTransport().getRaisonSociale());
            dto.setDeletedAt(modePaiementOrangeMoney.getDeletedAt()!=null?modePaiementOrangeMoney.getDeletedAt().toString():null);
            dto.setUpdatedAt(modePaiementOrangeMoney.getUpdatedAt()!=null?modePaiementOrangeMoney.getUpdatedAt().toString():null);
            dto.setCreatedAt(modePaiementOrangeMoney.getCreatedAt()!=null?modePaiementOrangeMoney.getCreatedAt().toString():null);
            dto.setCreatedBy(modePaiementOrangeMoney.getCreatedBy());
            dto.setIsDeleted(modePaiementOrangeMoney.getIsDeleted());
            dto.setDeletedBy(modePaiementOrangeMoney.getDeletedBy());
            dto.setUpdatedBy(modePaiementOrangeMoney.getUpdatedBy());
            return dto;
        }
        return new ModePaiementDTO();
    }

}
