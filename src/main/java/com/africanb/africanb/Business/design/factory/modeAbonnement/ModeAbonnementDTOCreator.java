package com.africanb.africanb.Business.design.factory.modeAbonnement;

import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPeriodiqueDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPrelevementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.ModeAbonnementDTO;

public class ModeAbonnementDTOCreator {

    public static ModeAbonnementDTO createModeAbonnementDTO(Object object){
        if(object instanceof AbonnementPeriodiqueDTO){
            ModeAbonnementPeriodiqueDTOFactory factory = new ModeAbonnementPeriodiqueDTOFactory();
            return factory.createProduct(object);
        }
        if(object instanceof AbonnementPrelevementDTO){
            ModeAbonnementPeriodiqueDTOFactory factory = new ModeAbonnementPeriodiqueDTOFactory();
            return factory.createProduct(object);
        }
        return new ModeAbonnementDTO();
    }

}
