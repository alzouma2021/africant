package com.africanb.africanb.Business.design.factory.modePaiment;


import com.africanb.africanb.dao.entity.compagnie.ModePaiment.*;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.*;

public class ModePaiementEntityCreator {

    public static ModePaiementDTO createModePaiementDTO(Object object){
        if(object instanceof ModePaiementWave){
            ModePaiementWaveFactory factory = new ModePaiementWaveFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ModePaiementMoovMoney){
            ModePaiementMoovMoneyFactory factory = new ModePaiementMoovMoneyFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ModePaiementOrangeMoney){
            ModePaiementOrangeMoneyFactory factory = new ModePaiementOrangeMoneyFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ModePaiementMtnMoney){
            ModePaiementMtnMoneyFactory factory = new ModePaiementMtnMoneyFactory();
            return factory.createProduct(object);
        }
        if(object instanceof ModePaiementEnEspece){
            ModePaiementEnEspeceFactory factory = new ModePaiementEnEspeceFactory();
            return factory.createProduct(object);
        }
        return new ModePaiementDTO();
    }

}
