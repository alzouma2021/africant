package com.africanb.africanb.Business.design.factory.modePaiment;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementEnEspece;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementDTO;

public class ModePaiementEnEspeceFactory implements ModePaimentDTOFactory {
    @Override
    public ModePaiementDTO createProduct(Object object) {
        if(object instanceof ModePaiementEnEspece modePaiementEnEspece){
            ModePaiementDTO dto = new ModePaiementDTO();
            dto.setId(modePaiementEnEspece.getId());
            dto.setDesignation(modePaiementEnEspece.getDesignation());
            dto.setDescription(modePaiementEnEspece.getDescription());
            dto.setTypeModePaiementDesignation(modePaiementEnEspece.getTypeModePaiement().getDesignation());
            dto.setCompagnieTransportRaisonSociale(modePaiementEnEspece.getCompagnieTransport().getRaisonSociale());
            dto.setDeletedAt(modePaiementEnEspece.getDeletedAt()!=null?modePaiementEnEspece.getDeletedAt().toString():null);
            dto.setUpdatedAt(modePaiementEnEspece.getUpdatedAt()!=null?modePaiementEnEspece.getUpdatedAt().toString():null);
            dto.setCreatedAt(modePaiementEnEspece.getCreatedAt()!=null?modePaiementEnEspece.getCreatedAt().toString():null);
            dto.setCreatedBy(modePaiementEnEspece.getCreatedBy());
            dto.setIsDeleted(modePaiementEnEspece.getIsDeleted());
            dto.setDeletedBy(modePaiementEnEspece.getDeletedBy());
            dto.setUpdatedBy(modePaiementEnEspece.getUpdatedBy());
            return dto;
        }
        return new ModePaiementDTO();
    }
}
