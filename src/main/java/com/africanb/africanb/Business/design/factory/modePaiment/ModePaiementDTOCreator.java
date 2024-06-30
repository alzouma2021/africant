package com.africanb.africanb.Business.design.factory.modePaiment;


import com.africanb.africanb.helper.dto.compagnie.ModePaiement.*;

public class ModePaiementDTOCreator {

    public static ModePaiementDTO createModePaiementDTO(Object object){
        if(object instanceof ModePaiementWaveDTO){
            ModePaiementWaveDTOFactory factory = new ModePaiementWaveDTOFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ModePaiementMoovMoneyDTO){
            ModePaiementMoovMoneyDTOFactory factory = new ModePaiementMoovMoneyDTOFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ModePaiementOrangeMoneyDTO){
            ModePaiementOrangeMoneyDTOFactory factory = new ModePaiementOrangeMoneyDTOFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ModePaiementMtnMoneyDTO){
            ModePaiementMtnMoneyDTOFactory factory = new ModePaiementMtnMoneyDTOFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ModePaiementEnEspeceDTO){
            ModePaiementEnEspeceDTOFactory factory = new ModePaiementEnEspeceDTOFactory();
            return factory.createProduct(object);
        }
        return new ModePaiementDTO();
    }

}
