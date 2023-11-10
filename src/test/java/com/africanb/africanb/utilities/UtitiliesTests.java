package com.africanb.africanb.utilities;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPrelevement;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.ModeAbonnement;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPeriodiqueDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPrelevementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.ModeAbonnementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.*;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageBooleanDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageLongDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageStringDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.utils.Reference.Reference;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UtitiliesTests {

    @Test
    public void transformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuTypeLongTest(){
        ValeurCaracteristiqueOffreVoyageDTO valeurCaracteristiqueOffreVoyageDTO = new ValeurCaracteristiqueOffreVoyageDTO();
        valeurCaracteristiqueOffreVoyageDTO.setOffreVoyageDesignation("offreVoyageOne");
        valeurCaracteristiqueOffreVoyageDTO.setTypeProprieteOffreVoyageDesignation("refElementLong");
        valeurCaracteristiqueOffreVoyageDTO.setValeurTexte("12");

        ValeurCaracteristiqueOffreVoyageDTO rtn =  Utilities.transformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuTypeDeLaPropriete(valeurCaracteristiqueOffreVoyageDTO);

        assertNotNull(rtn);
        assertInstanceOf(ValeurCaracteristiqueOffreVoyageLongDTO.class,rtn);
        assertEquals(rtn.getValeurTexte(),valeurCaracteristiqueOffreVoyageDTO.getValeurTexte());
        assertEquals(rtn.getOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getOffreVoyageDesignation());
        assertEquals(rtn.getProprieteOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getProprieteOffreVoyageDesignation());
    }

    @Test
    public void transformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuBooleanTest(){
        ValeurCaracteristiqueOffreVoyageDTO valeurCaracteristiqueOffreVoyageDTO = new ValeurCaracteristiqueOffreVoyageDTO();
        valeurCaracteristiqueOffreVoyageDTO.setOffreVoyageDesignation("offreVoyageTwo");
        valeurCaracteristiqueOffreVoyageDTO.setTypeProprieteOffreVoyageDesignation("refElementBoolean");
        valeurCaracteristiqueOffreVoyageDTO.setValeurTexte("false");

        ValeurCaracteristiqueOffreVoyageDTO rtn =  Utilities.transformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuTypeDeLaPropriete(valeurCaracteristiqueOffreVoyageDTO);

        assertNotNull(rtn);
        assertInstanceOf(ValeurCaracteristiqueOffreVoyageBooleanDTO.class,rtn);
        assertEquals(rtn.getValeurTexte(),valeurCaracteristiqueOffreVoyageDTO.getValeurTexte());
        assertEquals(rtn.getOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getOffreVoyageDesignation());
        assertEquals(rtn.getProprieteOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getProprieteOffreVoyageDesignation());
    }

    @Test
    public void transformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuStringTest(){
        ValeurCaracteristiqueOffreVoyageDTO valeurCaracteristiqueOffreVoyageDTO = new ValeurCaracteristiqueOffreVoyageDTO();
        valeurCaracteristiqueOffreVoyageDTO.setOffreVoyageDesignation("offreVoyageThree");
        valeurCaracteristiqueOffreVoyageDTO.setTypeProprieteOffreVoyageDesignation("refElementString");
        valeurCaracteristiqueOffreVoyageDTO.setValeurTexte("froid");

        ValeurCaracteristiqueOffreVoyageDTO rtn =  Utilities.transformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuTypeDeLaPropriete(valeurCaracteristiqueOffreVoyageDTO);

        assertNotNull(rtn);
        assertInstanceOf(ValeurCaracteristiqueOffreVoyageStringDTO.class,rtn);
        assertEquals(rtn.getValeurTexte(),valeurCaracteristiqueOffreVoyageDTO.getValeurTexte());
        assertEquals(rtn.getOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getOffreVoyageDesignation());
        assertEquals(rtn.getProprieteOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getProprieteOffreVoyageDesignation());
    }

    @Disabled
    @Test
    public void transformerEntityModeAbonnementEnEntityAbonnementPrelevementTest(){
        ModeAbonnement modeAbonnement = new ModeAbonnement();
        modeAbonnement.setDateDebutAbonnement(Date.from(Instant.now()));
        modeAbonnement.setDateFinAbonnement(Date.from(Instant.now()));
            CompagnieTransport compagnieTransport = new CompagnieTransport();
            compagnieTransport.setDesignation("compagnieTransport001");
            compagnieTransport.setRaisonSociale("amm");
            compagnieTransport.setEmail("test@gmail.com");
        modeAbonnement.setCompagnieTransport(compagnieTransport);
            Reference periodiciteAbonnement = new Reference();
            periodiciteAbonnement.setDesignation("Mensuel");
        modeAbonnement.setPeriodiciteAbonnement(periodiciteAbonnement);
            Reference typeModeAbonnement = new Reference();
            typeModeAbonnement.setDesignation("Prelevement");
        modeAbonnement.setTypeModeAbonnement(typeModeAbonnement);

        AbonnementPrelevement rtn = Utilities.transformerEntityModeAbonnementEnEntityAbonnementPrelevement(modeAbonnement);

        assertNotNull(rtn);
        assertInstanceOf(AbonnementPrelevement.class,rtn);
        assertEquals(rtn.getTypeModeAbonnement().getDesignation(),"Prelevement");
        assertEquals(rtn.getPeriodiciteAbonnement().getDesignation(),"Mensuel");
        assertEquals(rtn.getCompagnieTransport().getDesignation(),"compagnieTransport001");
    }

    @Test
    public void isBlankTest(){
        String str="";

        assertEquals(true, Utilities.isBlank(str));
    }

    @Test
    public void isNotBlankTest(){
        String str="isNotBlank";

        assertEquals(true, Utilities.isNotBlank(str));
    }

    @Test
    public void convertStringToDateTest() throws ParseException {
        Date date = new Date();
        String dateString="12/12/2023";

        assertNotNull(Utilities.convertStringToDate(dateString));
        assertNotNull(Utilities.convertStringToDate(dateString).toString(),dateString);
    }

    @Test
    public void convertDateToLocalDateTest(){
        Date date = new Date();

        assertNotNull(Utilities.convertDateToLocalDate(date));
        assertInstanceOf(LocalDate.class, Utilities.convertDateToLocalDate(date));
    }

    @Test
    public void getFrenchDayOfWeekTest(){
        Date date = new Date();

        String day = Utilities.getFrenchDayOfWeek(date);

        assertNotNull(day);
        assertEquals(day, "vendredi");
    }

    @Test
    public void getFrenchDayOfWeekWithFormatDDMMYYYYTest(){
        String dateString = "10/11/2023";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date date = new Date();

        String day = Utilities.getFrenchDayOfWeek(date);

        assertNotNull(day);
        assertEquals(day, "vendredi");
    }

    @Test
    public void getFrenchDayOfWeekWithFormatYYYYMMDDTest(){
        String dateString = "2023-10-11";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = new Date();
        String day = Utilities.getFrenchDayOfWeek(date);
        assertNotNull(day);
        assertEquals(day, "vendredi");
    }

    @Test
    public void transformerLaClasseModePaiementtEnModePaiementMtnMoneyTest(){
        ModePaiementDTO modePaiementDTO = new ModePaiementDTO();
        modePaiementDTO.setCompagnieTransportRaisonSociale("amm");
        modePaiementDTO.setDesignation("modePaiementMtnMoney");
        modePaiementDTO.setTypeModePaiementDesignation("ModePaiementMtnMoney");

        ModePaiementDTO rtn =  Utilities.transformerLaClasseModePaiementtEnClasseFilleCorrespondante(modePaiementDTO);

        assertNotNull(rtn);
        assertInstanceOf(ModePaiementMtnMoneyDTO.class,rtn);
        assertEquals(rtn.getTypeModePaiementDesignation(),modePaiementDTO.getTypeModePaiementDesignation());
        assertEquals(rtn.getCompagnieTransportRaisonSociale(),modePaiementDTO.getCompagnieTransportRaisonSociale());
    }

    @Test
    public void transformerLaClasseModePaiementtEnModePaiementOrangeMoneyTest(){
        ModePaiementDTO modePaiementDTO = new ModePaiementDTO();
        modePaiementDTO.setCompagnieTransportRaisonSociale("amm");
        modePaiementDTO.setDesignation("modePaiementOrangeMoney");
        modePaiementDTO.setTypeModePaiementDesignation("ModePaiementOrangeMoney");

        ModePaiementDTO rtn =  Utilities.transformerLaClasseModePaiementtEnClasseFilleCorrespondante(modePaiementDTO);

        assertNotNull(rtn);
        assertInstanceOf(ModePaiementOrangeMoneyDTO.class,rtn);
        assertEquals(rtn.getTypeModePaiementDesignation(),modePaiementDTO.getTypeModePaiementDesignation());
        assertEquals(rtn.getCompagnieTransportRaisonSociale(),modePaiementDTO.getCompagnieTransportRaisonSociale());
    }

    @Test
    public void transformerLaClasseModePaiementtEnModePaiementMoovMoneyTest(){
        ModePaiementDTO modePaiementDTO = new ModePaiementDTO();
        modePaiementDTO.setCompagnieTransportRaisonSociale("amm");
        modePaiementDTO.setDesignation("modePaiementMoovMoney");
        modePaiementDTO.setTypeModePaiementDesignation("ModePaiementMoovMoney");

        ModePaiementDTO rtn =  Utilities.transformerLaClasseModePaiementtEnClasseFilleCorrespondante(modePaiementDTO);

        assertNotNull(rtn);
        assertInstanceOf(ModePaiementMoovMoneyDTO.class,rtn);
        assertEquals(rtn.getTypeModePaiementDesignation(),modePaiementDTO.getTypeModePaiementDesignation());
        assertEquals(rtn.getCompagnieTransportRaisonSociale(),modePaiementDTO.getCompagnieTransportRaisonSociale());
    }

    @Test
    public void transformerLaClasseModePaiementtEnModePaiementWaveMoneyTest(){
        ModePaiementDTO modePaiementDTO = new ModePaiementDTO();
        modePaiementDTO.setCompagnieTransportRaisonSociale("amm");
        modePaiementDTO.setDesignation("modePaiementWaveMoney");
        modePaiementDTO.setTypeModePaiementDesignation("ModePaiementWaveMoney");

        ModePaiementDTO rtn =  Utilities.transformerLaClasseModePaiementtEnClasseFilleCorrespondante(modePaiementDTO);

        assertNotNull(rtn);
        assertInstanceOf(ModePaiementWaveDTO.class,rtn);
        assertEquals(rtn.getTypeModePaiementDesignation(),modePaiementDTO.getTypeModePaiementDesignation());
        assertEquals(rtn.getCompagnieTransportRaisonSociale(),modePaiementDTO.getCompagnieTransportRaisonSociale());
    }

    @Test
    public void transformerLaClasseModePaiementtEnModePaiementEnEspeceTest(){
        ModePaiementDTO modePaiementDTO = new ModePaiementDTO();
        modePaiementDTO.setCompagnieTransportRaisonSociale("amm");
        modePaiementDTO.setDesignation("modePaiementEnEspece");
        modePaiementDTO.setTypeModePaiementDesignation("ModePaiementEnEspece");

        ModePaiementDTO rtn =  Utilities.transformerLaClasseModePaiementtEnClasseFilleCorrespondante(modePaiementDTO);

        assertNotNull(rtn);
        assertInstanceOf(ModePaiementEnEspeceDTO.class,rtn);
        assertEquals(rtn.getTypeModePaiementDesignation(),modePaiementDTO.getTypeModePaiementDesignation());
        assertEquals(rtn.getCompagnieTransportRaisonSociale(),modePaiementDTO.getCompagnieTransportRaisonSociale());
    }

    @Test
    public void transformerLaClasseModeAbonnementEnAbonnementPeriodiqueTest(){
        ModeAbonnementDTO modeAbonnementDTO = new ModeAbonnementDTO();
        modeAbonnementDTO.setDateDebutAbonnement("10/11/2023");
        modeAbonnementDTO.setDateFinAbonnement("10/11/2023");
        modeAbonnementDTO.setCompagnieTransportRaisonSociale("amm");
        modeAbonnementDTO.setRedevance(10000);
        modeAbonnementDTO.setTaux(10);
        modeAbonnementDTO.setTypeModeAbonnementDesignation("AbonnementPeriodique");
        modeAbonnementDTO.setDesignation("abonnementPeriodique");
        modeAbonnementDTO.setPeriodiciteAbonnementDesignation("Mensuel");

        ModeAbonnementDTO rtn =  Utilities.transformerLaClasseModeAbonnementEnClasseFilleCorrespondante(modeAbonnementDTO);

        assertNotNull(rtn);
        assertInstanceOf(AbonnementPeriodiqueDTO.class,rtn);
        assertEquals(rtn.getTypeModeAbonnementDesignation(),modeAbonnementDTO.getTypeModeAbonnementDesignation());
        assertEquals(rtn.getRedevance(),modeAbonnementDTO.getRedevance());
        assertEquals(rtn.getDesignation(),modeAbonnementDTO.getDesignation());
    }

    @Test
    public void transformerLaClasseModeAbonnementEnAbonnementPrelevementTest(){
        ModeAbonnementDTO modeAbonnementDTO = new ModeAbonnementDTO();
        modeAbonnementDTO.setDateDebutAbonnement("10/11/2023");
        modeAbonnementDTO.setDateFinAbonnement("10/11/2023");
        modeAbonnementDTO.setCompagnieTransportRaisonSociale("amm");
        modeAbonnementDTO.setTaux(10);
        modeAbonnementDTO.setTypeModeAbonnementDesignation("AbonnementPrelevement");
        modeAbonnementDTO.setDesignation("abonnementPrelevement");
        modeAbonnementDTO.setPeriodiciteAbonnementDesignation("Mensuel");

        ModeAbonnementDTO rtn =  Utilities.transformerLaClasseModeAbonnementEnClasseFilleCorrespondante(modeAbonnementDTO);

        assertNotNull(rtn);
        assertInstanceOf(AbonnementPrelevementDTO.class,rtn);
        assertEquals(rtn.getTypeModeAbonnementDesignation(),modeAbonnementDTO.getTypeModeAbonnementDesignation());
        assertEquals(rtn.getRedevance(),modeAbonnementDTO.getRedevance());
        assertEquals(rtn.getDesignation(),modeAbonnementDTO.getDesignation());
    }

}
