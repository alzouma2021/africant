package com.africanb.africanb.Business.design.factory.modeAbonnement;

import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPeriodique;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPrelevement;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPeriodiqueDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPrelevementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.ModeAbonnementDTO;

public class ModeAbonnementFromEntityCreator {

    public static ModeAbonnementDTO createModeAbonnementDTO(Object object){
        if(object instanceof AbonnementPeriodique){
            ModeAbonnementPeriodiqueFactory factory = new ModeAbonnementPeriodiqueFactory();
            return factory.createProduct(object);
        }
        if(object instanceof AbonnementPrelevement){
            ModeAbonnementPrelevementFactory factory = new ModeAbonnementPrelevementFactory();
            return factory.createProduct(object);
        }
        return new ModeAbonnementDTO();
    }

}
