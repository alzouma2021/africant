package com.africanb.africanb.utilities;


import com.africanb.africanb.Business.design.factory.modeAbonnement.ModeAbonnementUtils;
import com.africanb.africanb.Business.design.factory.modePaiment.ModePaiementUtils;
import com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage.ValeurCaracteristiqueOffreVoyageUtils;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPeriodiqueDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPrelevementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.ModeAbonnementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.*;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageBooleanDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageLongDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageStringDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Disabled
public class UtitiliesTests {

    @Test
    public void testTransformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuTypeLong(){
        ValeurCaracteristiqueOffreVoyageDTO valeurCaracteristiqueOffreVoyageDTO = new ValeurCaracteristiqueOffreVoyageDTO();
        valeurCaracteristiqueOffreVoyageDTO.setOffreVoyageDesignation("offreVoyageOne");
        valeurCaracteristiqueOffreVoyageDTO.setTypeProprieteOffreVoyageDesignation("refElementLong");
        valeurCaracteristiqueOffreVoyageDTO.setValeurTexte("12");

        ValeurCaracteristiqueOffreVoyageDTO rtn =  ValeurCaracteristiqueOffreVoyageUtils.transformAbstractClassIntoChildClass(valeurCaracteristiqueOffreVoyageDTO);

        assertNotNull(rtn);
        assertInstanceOf(ValeurCaracteristiqueOffreVoyageLongDTO.class,rtn);
        assertEquals(rtn.getValeurTexte(),valeurCaracteristiqueOffreVoyageDTO.getValeurTexte());
        assertEquals(rtn.getOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getOffreVoyageDesignation());
        assertEquals(rtn.getProprieteOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getProprieteOffreVoyageDesignation());
    }

    @Test
    public void testTransformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuBoolean(){
        ValeurCaracteristiqueOffreVoyageDTO valeurCaracteristiqueOffreVoyageDTO = new ValeurCaracteristiqueOffreVoyageDTO();
        valeurCaracteristiqueOffreVoyageDTO.setOffreVoyageDesignation("offreVoyageTwo");
        valeurCaracteristiqueOffreVoyageDTO.setTypeProprieteOffreVoyageDesignation("refElementBoolean");
        valeurCaracteristiqueOffreVoyageDTO.setValeurTexte("false");

        ValeurCaracteristiqueOffreVoyageDTO rtn =  ValeurCaracteristiqueOffreVoyageUtils.transformAbstractClassIntoChildClass(valeurCaracteristiqueOffreVoyageDTO);

        assertNotNull(rtn);
        assertInstanceOf(ValeurCaracteristiqueOffreVoyageBooleanDTO.class,rtn);
        assertEquals(rtn.getValeurTexte(),valeurCaracteristiqueOffreVoyageDTO.getValeurTexte());
        assertEquals(rtn.getOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getOffreVoyageDesignation());
        assertEquals(rtn.getProprieteOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getProprieteOffreVoyageDesignation());
    }

    @Test
    public void testTransformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuString(){
        ValeurCaracteristiqueOffreVoyageDTO valeurCaracteristiqueOffreVoyageDTO = new ValeurCaracteristiqueOffreVoyageDTO();
        valeurCaracteristiqueOffreVoyageDTO.setOffreVoyageDesignation("offreVoyageThree");
        valeurCaracteristiqueOffreVoyageDTO.setTypeProprieteOffreVoyageDesignation("refElementString");
        valeurCaracteristiqueOffreVoyageDTO.setValeurTexte("froid");

        ValeurCaracteristiqueOffreVoyageDTO rtn =  ValeurCaracteristiqueOffreVoyageUtils.transformAbstractClassIntoChildClass(valeurCaracteristiqueOffreVoyageDTO);

        assertNotNull(rtn);
        assertInstanceOf(ValeurCaracteristiqueOffreVoyageStringDTO.class,rtn);
        assertEquals(rtn.getValeurTexte(),valeurCaracteristiqueOffreVoyageDTO.getValeurTexte());
        assertEquals(rtn.getOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getOffreVoyageDesignation());
        assertEquals(rtn.getProprieteOffreVoyageDesignation(),valeurCaracteristiqueOffreVoyageDTO.getProprieteOffreVoyageDesignation());
    }


    @Test
    public void testIsBlank(){
        String str="";

        assertEquals(true, Utilities.isBlank(str));
    }

    @Test
    public void testIsNotBlank(){
        String str="isNotBlank";

        assertEquals(true, Utilities.isNotBlank(str));
    }

    @Test
    public void testConvertStringToDate() throws ParseException {
        Date date = new Date();
        String dateString="12/12/2023";

        assertNotNull(Utilities.convertStringToDate(dateString));
        assertNotNull(Utilities.convertStringToDate(dateString).toString(),dateString);
    }

    @Test
    public void testConvertDateToLocalDate(){
        Date date = new Date();

        assertNotNull(Utilities.convertDateToLocalDate(date));
        assertInstanceOf(LocalDate.class, Utilities.convertDateToLocalDate(date));
    }

    @Test
    public void testGetFrenchDayOfWeek(){
        String day = Utilities.getFrenchDayOfWeek(new Date());
        assertNotNull(day);
    }

    @Test
    public void testGetFrenchDayOfWeekWithFormatDDMMYYYY(){
        String day = Utilities.getFrenchDayOfWeek(new Date());
        assertNotNull(day);
    }

    @Test
    public void testGetFrenchDayOfWeekWithFormatYYYYMMDD(){
        String day = Utilities.getFrenchDayOfWeek(new Date());
        assertNotNull(day);
    }

    @Test
    public void testTransformerLaClasseModePaiementEnModePaiementMtnMoney(){
        ModePaiementDTO modePaiementDTO = new ModePaiementDTO();
        modePaiementDTO.setCompagnieTransportRaisonSociale("amm");
        modePaiementDTO.setDesignation("modePaiementMtnMoney");
        modePaiementDTO.setTypeModePaiementDesignation("ModePaiementMtnMoney");

        ModePaiementDTO rtn =  ModePaiementUtils.transformAbstractClassIntoChildClass(modePaiementDTO);

        assertNotNull(rtn);
        assertInstanceOf(ModePaiementMtnMoneyDTO.class,rtn);
        assertEquals(rtn.getTypeModePaiementDesignation(),modePaiementDTO.getTypeModePaiementDesignation());
        assertEquals(rtn.getCompagnieTransportRaisonSociale(),modePaiementDTO.getCompagnieTransportRaisonSociale());
    }

    @Test
    public void testTransformerLaClasseModePaiementtEnModePaiementOrangeMoney(){
        ModePaiementDTO modePaiementDTO = new ModePaiementDTO();
        modePaiementDTO.setCompagnieTransportRaisonSociale("amm");
        modePaiementDTO.setDesignation("modePaiementOrangeMoney");
        modePaiementDTO.setTypeModePaiementDesignation("ModePaiementOrangeMoney");

        ModePaiementDTO rtn = ModePaiementUtils.transformAbstractClassIntoChildClass(modePaiementDTO);

        assertNotNull(rtn);
        assertInstanceOf(ModePaiementOrangeMoneyDTO.class,rtn);
        assertEquals(rtn.getTypeModePaiementDesignation(),modePaiementDTO.getTypeModePaiementDesignation());
        assertEquals(rtn.getCompagnieTransportRaisonSociale(),modePaiementDTO.getCompagnieTransportRaisonSociale());
    }

    @Test
    public void testTransformerLaClasseModePaiementtEnModePaiementMoovMoney(){
        ModePaiementDTO modePaiementDTO = new ModePaiementDTO();
        modePaiementDTO.setCompagnieTransportRaisonSociale("amm");
        modePaiementDTO.setDesignation("modePaiementMoovMoney");
        modePaiementDTO.setTypeModePaiementDesignation("ModePaiementMoovMoney");

        ModePaiementDTO rtn =  ModePaiementUtils.transformAbstractClassIntoChildClass(modePaiementDTO);

        assertNotNull(rtn);
        assertInstanceOf(ModePaiementMoovMoneyDTO.class,rtn);
        assertEquals(rtn.getTypeModePaiementDesignation(),modePaiementDTO.getTypeModePaiementDesignation());
        assertEquals(rtn.getCompagnieTransportRaisonSociale(),modePaiementDTO.getCompagnieTransportRaisonSociale());
    }

    @Test
    public void testTransformerLaClasseModePaiementtEnModePaiementWaveMoney(){
        ModePaiementDTO modePaiementDTO = new ModePaiementDTO();
        modePaiementDTO.setCompagnieTransportRaisonSociale("amm");
        modePaiementDTO.setDesignation("modePaiementWaveMoney");
        modePaiementDTO.setTypeModePaiementDesignation("ModePaiementWaveMoney");

        ModePaiementDTO rtn =  ModePaiementUtils.transformAbstractClassIntoChildClass(modePaiementDTO);

        assertNotNull(rtn);
        assertInstanceOf(ModePaiementWaveDTO.class,rtn);
        assertEquals(rtn.getTypeModePaiementDesignation(),modePaiementDTO.getTypeModePaiementDesignation());
        assertEquals(rtn.getCompagnieTransportRaisonSociale(),modePaiementDTO.getCompagnieTransportRaisonSociale());
    }

    @Test
    public void TesttransformerLaClasseModePaiementtEnModePaiementEnEspece(){
        ModePaiementDTO modePaiementDTO = new ModePaiementDTO();
        modePaiementDTO.setCompagnieTransportRaisonSociale("amm");
        modePaiementDTO.setDesignation("modePaiementEnEspece");
        modePaiementDTO.setTypeModePaiementDesignation("ModePaiementEnEspece");

        ModePaiementDTO rtn =  ModePaiementUtils.transformAbstractClassIntoChildClass(modePaiementDTO);

        assertNotNull(rtn);
        assertInstanceOf(ModePaiementEnEspeceDTO.class,rtn);
        assertEquals(rtn.getTypeModePaiementDesignation(),modePaiementDTO.getTypeModePaiementDesignation());
        assertEquals(rtn.getCompagnieTransportRaisonSociale(),modePaiementDTO.getCompagnieTransportRaisonSociale());
    }

    @Test
    public void testTransformerLaClasseModeAbonnementEnAbonnementPeriodique(){
        ModeAbonnementDTO modeAbonnementDTO = new ModeAbonnementDTO();
        modeAbonnementDTO.setDateDebutAbonnement("10/11/2023");
        modeAbonnementDTO.setDateFinAbonnement("10/11/2023");
        modeAbonnementDTO.setCompagnieTransportRaisonSociale("amm");
        modeAbonnementDTO.setRedevance(10000);
        modeAbonnementDTO.setTaux(10);
        modeAbonnementDTO.setTypeModeAbonnementDesignation("AbonnementPeriodique");
        modeAbonnementDTO.setDesignation("abonnementPeriodique");
        modeAbonnementDTO.setPeriodiciteAbonnementDesignation("Mensuel");

        ModeAbonnementDTO rtn = ModeAbonnementUtils.transformAbstractClassIntoChildClass(modeAbonnementDTO);

        assertNotNull(rtn);
        assertInstanceOf(AbonnementPeriodiqueDTO.class,rtn);
        assertEquals(rtn.getTypeModeAbonnementDesignation(),modeAbonnementDTO.getTypeModeAbonnementDesignation());
        assertEquals(rtn.getRedevance(),modeAbonnementDTO.getRedevance());
        assertEquals(rtn.getDesignation(),modeAbonnementDTO.getDesignation());
    }

    @Test
    public void testTransformerLaClasseModeAbonnementEnAbonnementPrelevement(){
        ModeAbonnementDTO modeAbonnementDTO = new ModeAbonnementDTO();
        modeAbonnementDTO.setDateDebutAbonnement("10/11/2023");
        modeAbonnementDTO.setDateFinAbonnement("10/11/2023");
        modeAbonnementDTO.setCompagnieTransportRaisonSociale("amm");
        modeAbonnementDTO.setTaux(10);
        modeAbonnementDTO.setTypeModeAbonnementDesignation("AbonnementPrelevement");
        modeAbonnementDTO.setDesignation("abonnementPrelevement");
        modeAbonnementDTO.setPeriodiciteAbonnementDesignation("Mensuel");

        ModeAbonnementDTO rtn =  ModeAbonnementUtils.transformAbstractClassIntoChildClass(modeAbonnementDTO);

        assertNotNull(rtn);
        assertInstanceOf(AbonnementPrelevementDTO.class,rtn);
        assertEquals(rtn.getTypeModeAbonnementDesignation(),modeAbonnementDTO.getTypeModeAbonnementDesignation());
        assertEquals(rtn.getRedevance(),modeAbonnementDTO.getRedevance());
        assertEquals(rtn.getDesignation(),modeAbonnementDTO.getDesignation());
    }

    @Test
    public void testIsTrue() {
        assertTrue(Utilities.isTrue(true));
        assertFalse(Utilities.isTrue(false));
        assertFalse(Utilities.isTrue(null));
    }

    @Test
    public void testIsNotEmpty() {
        List<String> nonEmptyList = Arrays.asList("item1", "item2");
        List<String> emptyList = Collections.emptyList();
        List<String> nullList = null;

        assertTrue(Utilities.isNotEmpty(nonEmptyList));
        assertFalse(Utilities.isNotEmpty(emptyList));
        assertFalse(Utilities.isNotEmpty(nullList));
    }

    @Test
    public void testIsEmpty() {
        List<String> nonEmptyList = Arrays.asList("item1", "item2");
        List<String> emptyList = Collections.emptyList();
        List<String> nullList = null;

        assertFalse(Utilities.isEmpty(nonEmptyList));
        assertTrue(Utilities.isEmpty(emptyList));
        assertTrue(Utilities.isEmpty(nullList));
    }

    @Test
    public void testContainsWord() {
        String text = "This is a sample text.";

        assertTrue(Utilities.containsWord(text, "This"));
        assertTrue(Utilities.containsWord(text, "text"));
        assertTrue(Utilities.containsWord(text, "is"));
        assertFalse(Utilities.containsWord(text, "example"));
        assertFalse(Utilities.containsWord(text, "Text")); // Case-sensitive match
        assertFalse(Utilities.containsWord(null, "text"));
        assertFalse(Utilities.containsWord(text, null));
        assertFalse(Utilities.containsWord(null, null));
    }

}
