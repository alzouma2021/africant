package com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageBoolean;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageLong;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageString;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;

public class ValeurCaracteristiqueOffreVoyageEntityCreator {

    public static ValeurCaracteristiqueOffreVoyageDTO createValeurOffreVoyageDTO(Object object){
        if(object instanceof ValeurCaracteristiqueOffreVoyageString){
            ValeurCaracteristiqueOffreVoyageStringFactory factory = new ValeurCaracteristiqueOffreVoyageStringFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ValeurCaracteristiqueOffreVoyageLong){
            ValeurCaracteristiqueOffreVoyageLongFactory factory = new ValeurCaracteristiqueOffreVoyageLongFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ValeurCaracteristiqueOffreVoyageBoolean){
            ValeurCaracteristiqueOffreVoyageBooleanFactory factory = new ValeurCaracteristiqueOffreVoyageBooleanFactory();
            return factory.createProduct(object);
        }
        return new ValeurCaracteristiqueOffreVoyageDTO();
    }

}
