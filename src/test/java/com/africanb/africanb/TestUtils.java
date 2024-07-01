package com.africanb.africanb;

import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPrelevement;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.ModeAbonnement;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {

    public static AbonnementPrelevement buildAbonnementPrelevementEntity(ModeAbonnement entity){
        if(entity == null) return new AbonnementPrelevement();
        if(entity instanceof AbonnementPrelevement entityToUpdate){
            AbonnementPrelevement rtn = new AbonnementPrelevement();
            rtn.setId(entityToUpdate.getId());
            rtn.setDesignation(entityToUpdate.getDesignation());
            rtn.setDescription(entityToUpdate.getDescription());
            rtn.setDateDebutAbonnement(entityToUpdate.getDateDebutAbonnement());
            rtn.setDateFinAbonnement(entityToUpdate.getDateFinAbonnement());
            rtn.setTaux(entityToUpdate.getTaux());
            rtn.setCompagnieTransport(entityToUpdate.getCompagnieTransport());
            rtn.setPeriodiciteAbonnement(entityToUpdate.getPeriodiciteAbonnement());
            rtn.setTypeModeAbonnement(entityToUpdate.getTypeModeAbonnement());
            rtn.setIsDeleted(entityToUpdate.getIsDeleted());
            rtn.setUpdatedAt(entityToUpdate.getUpdatedAt());
            rtn.setCreatedAt(entityToUpdate.getCreatedAt());
            rtn.setDeletedAt(entityToUpdate.getDeletedAt());
            rtn.setUpdatedBy(entityToUpdate.getUpdatedBy());
            rtn.setCreatedBy(entityToUpdate.getCreatedBy());
            rtn.setCreatedBy(entityToUpdate.getCreatedBy());
            return rtn;
        }
        return  new AbonnementPrelevement();
    }

}
