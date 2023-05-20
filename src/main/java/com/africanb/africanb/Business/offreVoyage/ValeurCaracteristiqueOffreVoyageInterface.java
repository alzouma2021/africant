package com.africanb.africanb.Business.offreVoyage;

import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyage;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageLongDTO;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public interface ValeurCaracteristiqueOffreVoyageInterface {
    public ValeurCaracteristiqueOffreVoyageDTO saveValeurCaracteristiqueOffreVoyageDTOEnFonctionDuTypeDeLaPropriete(ValeurCaracteristiqueOffreVoyageDTO valeurCaracteristiqueOffreVoyageDTO, Locale locale) throws ParseException;

    public  List<ValeurCaracteristiqueOffreVoyageDTO> convertValeurCaracteristiqueOffreVoyagesFilleToValeurCaracteristiqueOffreVoyageDTO(List<ValeurCaracteristiqueOffreVoyage> valeurCaracteristiqueOffreVoyageList);
}
