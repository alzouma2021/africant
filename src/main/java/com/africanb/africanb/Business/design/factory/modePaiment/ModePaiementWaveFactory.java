package com.africanb.africanb.Business.design.factory.modePaiment;

import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementWave;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementWaveDTO;

public class ModePaiementWaveFactory implements ModePaimentDTOFactory {
    @Override
    public ModePaiementDTO createProduct(Object object) {
        if(object instanceof ModePaiementWave modePaiementWave){
            ModePaiementDTO dto = new ModePaiementDTO();
            dto.setId(modePaiementWave.getId());
            dto.setDesignation(modePaiementWave.getDesignation());
            dto.setDescription(modePaiementWave.getDescription());
            dto.setTelephoneGenerique(modePaiementWave.getTelephoneWave());
            dto.setTypeModePaiementDesignation(modePaiementWave.getTypeModePaiement().getDesignation());
            dto.setCompagnieTransportRaisonSociale(modePaiementWave.getCompagnieTransport().getRaisonSociale());
            dto.setDeletedAt(modePaiementWave.getDeletedAt()!=null?modePaiementWave.getDeletedAt().toString():null);
            dto.setUpdatedAt(modePaiementWave.getUpdatedAt()!=null?modePaiementWave.getUpdatedAt().toString():null);
            dto.setCreatedAt(modePaiementWave.getCreatedAt()!=null?modePaiementWave.getCreatedAt().toString():null);
            dto.setCreatedBy(modePaiementWave.getCreatedBy());
            dto.setIsDeleted(modePaiementWave.getIsDeleted());
            dto.setDeletedBy(modePaiementWave.getDeletedBy());
            dto.setUpdatedBy(modePaiementWave.getUpdatedBy());
            return dto;
        }
        return new ModePaiementWaveDTO();
    }
}
