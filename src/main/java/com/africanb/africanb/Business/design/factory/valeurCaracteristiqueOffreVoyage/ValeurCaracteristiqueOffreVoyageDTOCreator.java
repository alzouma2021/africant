package com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage;

import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageBooleanDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageLongDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageStringDTO;

public class ValeurCaracteristiqueOffreVoyageDTOCreator {

    public static ValeurCaracteristiqueOffreVoyageDTO createValeurOffreVoyageDTO(Object object){
        if(object instanceof ValeurCaracteristiqueOffreVoyageStringDTO){
            ValeurCaracteristiqueOffreVoyageStringDTOFactory factory = new ValeurCaracteristiqueOffreVoyageStringDTOFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ValeurCaracteristiqueOffreVoyageLongDTO){
            ValeurCaracteristiqueOffreVoyageLongDTOFactory factory = new ValeurCaracteristiqueOffreVoyageLongDTOFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ValeurCaracteristiqueOffreVoyageBooleanDTO){
            ValeurCaracteristiqueOffreVoyageBooleanDTOFactory factory = new ValeurCaracteristiqueOffreVoyageBooleanDTOFactory();
            return factory.createProduct(object);
        }
        return new ValeurCaracteristiqueOffreVoyageDTO();
    }

}
