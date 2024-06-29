package com.africanb.africanb.Business.design.factory.modeAbonnement;

import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPeriodique;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.ModeAbonnementDTO;

public class ModeAbonnementPeriodiqueFactory implements ModeAbonnementDTOFactory {
    @Override
    public ModeAbonnementDTO createProduct(Object object) {
        if(object instanceof AbonnementPeriodique abonnementPeriodique){
            ModeAbonnementDTO rtn = new ModeAbonnementDTO();
            rtn.setId( abonnementPeriodique.getId());
            rtn.setDesignation( abonnementPeriodique.getDesignation());
            rtn.setDescription( abonnementPeriodique.getDescription());
            rtn.setRedevance( abonnementPeriodique.getRedevance());
            rtn.setRedevancePublicite( abonnementPeriodique.getRedevancePublicite());
            rtn.setDateDebutAbonnement(abonnementPeriodique.getDateDebutAbonnement()!=null?abonnementPeriodique.getDateDebutAbonnement().toString():null);
            rtn.setDateFinAbonnement(abonnementPeriodique.getDateFinAbonnement()!=null?abonnementPeriodique.getDateFinAbonnement().toString():null);
            rtn.setPeriodiciteAbonnementDesignation(abonnementPeriodique.getPeriodiciteAbonnement().getDesignation());
            rtn.setCompagnieTransportRaisonSociale(abonnementPeriodique.getCompagnieTransport().getRaisonSociale());
            rtn.setDeletedAt( abonnementPeriodique.getDeletedAt()!=null?abonnementPeriodique.getDeletedAt().toString():null);
            rtn.setUpdatedAt( abonnementPeriodique.getUpdatedAt()!=null?abonnementPeriodique.getUpdatedAt().toString():null);
            rtn.setCreatedAt( abonnementPeriodique.getCreatedAt()!=null?abonnementPeriodique.getCreatedAt().toString():null);
            rtn.setCreatedBy( abonnementPeriodique.getCreatedBy());
            rtn.setIsDeleted( abonnementPeriodique.getIsDeleted());
            rtn.setDeletedBy( abonnementPeriodique.getDeletedBy());
            rtn.setUpdatedBy( abonnementPeriodique.getUpdatedBy());
            return rtn;
        }
        return new ModeAbonnementDTO();
    }
}
