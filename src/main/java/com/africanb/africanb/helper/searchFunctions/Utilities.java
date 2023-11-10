package com.africanb.africanb.helper.searchFunctions;

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
import com.africanb.africanb.utils.Constants.ProjectConstants;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

//@Slf4j
public class Utilities {

    private static final Logger logger = LogManager.getLogManager().getLogger("");

    /**
     *
     * @param dto
     * @return ValeurCaracteristiqueOffreVoyageDTO
     */
    public static ValeurCaracteristiqueOffreVoyageDTO transformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuTypeDeLaPropriete(ValeurCaracteristiqueOffreVoyageDTO dto) {
        if(dto!=null && dto.getTypeProprieteOffreVoyageDesignation()!=null) {
            switch (dto.getTypeProprieteOffreVoyageDesignation()) {
                case ProjectConstants.REF_ELEMENT_TYPE_LONG:
                    ValeurCaracteristiqueOffreVoyageLongDTO rtn = new ValeurCaracteristiqueOffreVoyageLongDTO();
                    rtn.setId(dto.getId());
                    rtn.setDesignation(dto.getDesignation());
                    rtn.setDescription(dto.getDescription());
                    rtn.setValeurTexte(dto.getValeurTexte());
                    rtn.setProprieteOffreVoyageDesignation(dto.getProprieteOffreVoyageDesignation());
                    rtn.setOffreVoyageDesignation(dto.getOffreVoyageDesignation());
                    rtn.setIsDeleted(dto.getIsDeleted());
                    rtn.setUpdatedAt(dto.getUpdatedAt());
                    rtn.setCreatedAt(dto.getCreatedAt());
                    rtn.setDeletedAt(dto.getDeletedAt());
                    rtn.setUpdatedBy(dto.getUpdatedBy());
                    rtn.setCreatedBy(dto.getCreatedBy());
                    rtn.setCreatedBy(dto.getCreatedBy());
                    rtn.setCreatedAtParam(dto.getCreatedAtParam());
                    rtn.setUpdatedAtParam(dto.getUpdatedAtParam());
                    rtn.setIsDeletedParam(dto.getIsDeletedParam());
                    rtn.setOrderField(dto.getOrderField());
                    rtn.setOrderDirection(dto.getOrderDirection());
                    return rtn;
                case ProjectConstants.REF_ELEMENT_TYPE_BOOLEAN:
                    ValeurCaracteristiqueOffreVoyageBooleanDTO rtnBoolean = new ValeurCaracteristiqueOffreVoyageBooleanDTO();
                    rtnBoolean.setId(dto.getId());
                    rtnBoolean.setDesignation(dto.getDesignation());
                    rtnBoolean.setDescription(dto.getDescription());
                    rtnBoolean.setValeurTexte(dto.getValeurTexte());
                    rtnBoolean.setProprieteOffreVoyageDesignation(dto.getProprieteOffreVoyageDesignation());
                    rtnBoolean.setOffreVoyageDesignation(dto.getOffreVoyageDesignation());
                    rtnBoolean.setIsDeleted(dto.getIsDeleted());
                    rtnBoolean.setUpdatedAt(dto.getUpdatedAt());
                    rtnBoolean.setCreatedAt(dto.getCreatedAt());
                    rtnBoolean.setDeletedAt(dto.getDeletedAt());
                    rtnBoolean.setUpdatedBy(dto.getUpdatedBy());
                    rtnBoolean.setCreatedBy(dto.getCreatedBy());
                    rtnBoolean.setCreatedBy(dto.getCreatedBy());
                    rtnBoolean.setCreatedAtParam(dto.getCreatedAtParam());
                    rtnBoolean.setUpdatedAtParam(dto.getUpdatedAtParam());
                    rtnBoolean.setIsDeletedParam(dto.getIsDeletedParam());
                    rtnBoolean.setOrderField(dto.getOrderField());
                    rtnBoolean.setOrderDirection(dto.getOrderDirection());
                    return rtnBoolean;
                case ProjectConstants.REF_ELEMENT_TYPE_STRING:
                    ValeurCaracteristiqueOffreVoyageStringDTO rtnString = new ValeurCaracteristiqueOffreVoyageStringDTO();
                    rtnString.setId(dto.getId());
                    rtnString.setDesignation(dto.getDesignation());
                    rtnString.setDescription(dto.getDescription());
                    rtnString.setValeurTexte(dto.getValeurTexte());
                    rtnString.setProprieteOffreVoyageDesignation(dto.getProprieteOffreVoyageDesignation());
                    rtnString.setOffreVoyageDesignation(dto.getOffreVoyageDesignation());
                    rtnString.setIsDeleted(dto.getIsDeleted());
                    rtnString.setUpdatedAt(dto.getUpdatedAt());
                    rtnString.setCreatedAt(dto.getCreatedAt());
                    rtnString.setDeletedAt(dto.getDeletedAt());
                    rtnString.setUpdatedBy(dto.getUpdatedBy());
                    rtnString.setCreatedBy(dto.getCreatedBy());
                    rtnString.setCreatedBy(dto.getCreatedBy());
                    rtnString.setCreatedAtParam(dto.getCreatedAtParam());
                    rtnString.setUpdatedAtParam(dto.getUpdatedAtParam());
                    rtnString.setIsDeletedParam(dto.getIsDeletedParam());
                    rtnString.setOrderField(dto.getOrderField());
                    rtnString.setOrderDirection(dto.getOrderDirection());
                    return rtnString;
                default:
                    return new ValeurCaracteristiqueOffreVoyageDTO();
            }
        }
        return new ValeurCaracteristiqueOffreVoyageDTO();
    }

    /**
     *
     * @param entity
     * @return  AbonnementPrelevement
     */
    public static AbonnementPrelevement transformerEntityModeAbonnementEnEntityAbonnementPrelevement(ModeAbonnement entity){
        if(entity==null) return new AbonnementPrelevement();

        AbonnementPrelevement rtn = new AbonnementPrelevement();
        AbonnementPrelevement entityToUpdate = new AbonnementPrelevement();
        entityToUpdate = (AbonnementPrelevement) entity;

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

    /**
     *
     * @param dto
     * @return ModeAbonnementDTO
     */
    public static ModeAbonnementDTO transformerLaClasseModeAbonnementEnClasseFilleCorrespondante(ModeAbonnementDTO dto) {
        if (dto != null && dto.getTypeModeAbonnementDesignation() != null){
            switch (dto.getTypeModeAbonnementDesignation()) {
                case ProjectConstants.REF_ELEMENT_ABONNEMENT_PERIODIQUE:
                    AbonnementPeriodiqueDTO rtnPeriodique = new AbonnementPeriodiqueDTO();
                    rtnPeriodique.setId(dto.getId());
                    rtnPeriodique.setDesignation(dto.getDesignation());
                    rtnPeriodique.setDescription(dto.getDescription());

                    rtnPeriodique.setDateDebutAbonnement(dto.getDateDebutAbonnement());
                    rtnPeriodique.setDateFinAbonnement(dto.getDateFinAbonnement());
                    rtnPeriodique.setRedevancePublicite(dto.getRedevancePublicite());
                    rtnPeriodique.setRedevance(dto.getRedevance());

                    rtnPeriodique.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    rtnPeriodique.setPeriodiciteAbonnementDesignation(dto.getPeriodiciteAbonnementDesignation());
                    rtnPeriodique.setTypeModeAbonnementDesignation(dto.getTypeModeAbonnementDesignation());

                    rtnPeriodique.setIsDeleted(dto.getIsDeleted());
                    rtnPeriodique.setUpdatedAt(dto.getUpdatedAt());
                    rtnPeriodique.setCreatedAt(dto.getCreatedAt());
                    rtnPeriodique.setDeletedAt(dto.getDeletedAt());
                    rtnPeriodique.setUpdatedBy(dto.getUpdatedBy());
                    rtnPeriodique.setCreatedBy(dto.getCreatedBy());
                    rtnPeriodique.setCreatedBy(dto.getCreatedBy());

                    rtnPeriodique.setCreatedAtParam(dto.getCreatedAtParam());
                    rtnPeriodique.setUpdatedAtParam(dto.getUpdatedAtParam());
                    rtnPeriodique.setIsDeletedParam(dto.getIsDeletedParam());
                    rtnPeriodique.setOrderField(dto.getOrderField());
                    rtnPeriodique.setOrderDirection(dto.getOrderDirection());

                    return rtnPeriodique;
                case ProjectConstants.REF_ELEMENT_ABONNEMENT_PRELEVEMENT:
                    AbonnementPrelevementDTO rtnPrelevement = new AbonnementPrelevementDTO();
                    rtnPrelevement.setId(dto.getId());
                    rtnPrelevement.setDesignation(dto.getDesignation());
                    rtnPrelevement.setDescription(dto.getDescription());

                    rtnPrelevement.setDateDebutAbonnement(dto.getDateDebutAbonnement());
                    rtnPrelevement.setDateFinAbonnement(dto.getDateFinAbonnement());
                    rtnPrelevement.setTaux(dto.getTaux());

                    rtnPrelevement.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    rtnPrelevement.setPeriodiciteAbonnementDesignation(dto.getPeriodiciteAbonnementDesignation());
                    rtnPrelevement.setTypeModeAbonnementDesignation(dto.getTypeModeAbonnementDesignation());

                    rtnPrelevement.setIsDeleted(dto.getIsDeleted());
                    rtnPrelevement.setUpdatedAt(dto.getUpdatedAt());
                    rtnPrelevement.setCreatedAt(dto.getCreatedAt());
                    rtnPrelevement.setDeletedAt(dto.getDeletedAt());
                    rtnPrelevement.setUpdatedBy(dto.getUpdatedBy());
                    rtnPrelevement.setCreatedBy(dto.getCreatedBy());
                    rtnPrelevement.setCreatedBy(dto.getCreatedBy());

                    rtnPrelevement.setCreatedAtParam(dto.getCreatedAtParam());
                    rtnPrelevement.setUpdatedAtParam(dto.getUpdatedAtParam());
                    rtnPrelevement.setIsDeletedParam(dto.getIsDeletedParam());
                    rtnPrelevement.setOrderField(dto.getOrderField());
                    rtnPrelevement.setOrderDirection(dto.getOrderDirection());
                    return rtnPrelevement;
                default:
                    return new ModeAbonnementDTO();
            }
        }
        return new ModeAbonnementDTO();
    }

    /**
     *
     * @param dto
     * @return ModePaiementDTO
     */
    public static ModePaiementDTO transformerLaClasseModePaiementtEnClasseFilleCorrespondante(ModePaiementDTO dto) {
        if (dto != null && dto.getTypeModePaiementDesignation() != null){
            switch (dto.getTypeModePaiementDesignation()) {
                case ProjectConstants.REF_ELEMENT_MODE_PAIEMENT_MTN_MONEY:
                    ModePaiementMtnMoneyDTO modePaiementMtnMoneyDTO = new ModePaiementMtnMoneyDTO();
                    modePaiementMtnMoneyDTO.setId(dto.getId());
                    modePaiementMtnMoneyDTO.setDesignation(dto.getDesignation());
                    modePaiementMtnMoneyDTO.setDescription(dto.getDescription());
                    modePaiementMtnMoneyDTO.setTelephoneMtnMoney(dto.getTelephoneGenerique()==null?null:dto.getTelephoneGenerique());

                    modePaiementMtnMoneyDTO.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    modePaiementMtnMoneyDTO.setTypeModePaiementDesignation(dto.getTypeModePaiementDesignation());

                    modePaiementMtnMoneyDTO.setIsDeleted(dto.getIsDeleted());
                    modePaiementMtnMoneyDTO.setUpdatedAt(dto.getUpdatedAt());
                    modePaiementMtnMoneyDTO.setCreatedAt(dto.getCreatedAt());
                    modePaiementMtnMoneyDTO.setDeletedAt(dto.getDeletedAt());
                    modePaiementMtnMoneyDTO.setUpdatedBy(dto.getUpdatedBy());
                    modePaiementMtnMoneyDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementMtnMoneyDTO.setCreatedBy(dto.getCreatedBy());

                    modePaiementMtnMoneyDTO.setCreatedAtParam(dto.getCreatedAtParam());
                    modePaiementMtnMoneyDTO.setUpdatedAtParam(dto.getUpdatedAtParam());
                    modePaiementMtnMoneyDTO.setIsDeletedParam(dto.getIsDeletedParam());
                    modePaiementMtnMoneyDTO.setOrderField(dto.getOrderField());
                    modePaiementMtnMoneyDTO.setOrderDirection(dto.getOrderDirection());

                    return modePaiementMtnMoneyDTO;
                case ProjectConstants.REF_ELEMENT_MODE_PAIEMENT_ORANGE_MONEY:
                    ModePaiementOrangeMoneyDTO modePaiementOrangeMoneyDTO = new ModePaiementOrangeMoneyDTO();
                    modePaiementOrangeMoneyDTO.setId(dto.getId());
                    modePaiementOrangeMoneyDTO.setDesignation(dto.getDesignation());
                    modePaiementOrangeMoneyDTO.setDescription(dto.getDescription());
                    modePaiementOrangeMoneyDTO.setTelephoneOrangeMoney(dto.getTelephoneGenerique()==null?null:dto.getTelephoneGenerique());

                    modePaiementOrangeMoneyDTO.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    modePaiementOrangeMoneyDTO.setTypeModePaiementDesignation(dto.getTypeModePaiementDesignation());

                    modePaiementOrangeMoneyDTO.setIsDeleted(dto.getIsDeleted());
                    modePaiementOrangeMoneyDTO.setUpdatedAt(dto.getUpdatedAt());
                    modePaiementOrangeMoneyDTO.setCreatedAt(dto.getCreatedAt());
                    modePaiementOrangeMoneyDTO.setDeletedAt(dto.getDeletedAt());
                    modePaiementOrangeMoneyDTO.setUpdatedBy(dto.getUpdatedBy());
                    modePaiementOrangeMoneyDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementOrangeMoneyDTO.setCreatedBy(dto.getCreatedBy());

                    modePaiementOrangeMoneyDTO.setCreatedAtParam(dto.getCreatedAtParam());
                    modePaiementOrangeMoneyDTO.setUpdatedAtParam(dto.getUpdatedAtParam());
                    modePaiementOrangeMoneyDTO.setIsDeletedParam(dto.getIsDeletedParam());
                    modePaiementOrangeMoneyDTO.setOrderField(dto.getOrderField());
                    modePaiementOrangeMoneyDTO.setOrderDirection(dto.getOrderDirection());

                    return modePaiementOrangeMoneyDTO;
                case ProjectConstants.REF_ELEMENT_MODE_PAIEMENT_MOOV_MONEY:
                    ModePaiementMoovMoneyDTO modePaiementMoovMoneyDTO = new ModePaiementMoovMoneyDTO();
                    modePaiementMoovMoneyDTO.setId(dto.getId());
                    modePaiementMoovMoneyDTO.setDesignation(dto.getDesignation());
                    modePaiementMoovMoneyDTO.setDescription(dto.getDescription());
                    modePaiementMoovMoneyDTO.setTelephoneMoovMoney(dto.getTelephoneGenerique()==null?null:dto.getTelephoneGenerique());

                    modePaiementMoovMoneyDTO.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    modePaiementMoovMoneyDTO.setTypeModePaiementDesignation(dto.getTypeModePaiementDesignation());

                    modePaiementMoovMoneyDTO.setIsDeleted(dto.getIsDeleted());
                    modePaiementMoovMoneyDTO.setUpdatedAt(dto.getUpdatedAt());
                    modePaiementMoovMoneyDTO.setCreatedAt(dto.getCreatedAt());
                    modePaiementMoovMoneyDTO.setDeletedAt(dto.getDeletedAt());
                    modePaiementMoovMoneyDTO.setUpdatedBy(dto.getUpdatedBy());
                    modePaiementMoovMoneyDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementMoovMoneyDTO.setCreatedBy(dto.getCreatedBy());

                    modePaiementMoovMoneyDTO.setCreatedAtParam(dto.getCreatedAtParam());
                    modePaiementMoovMoneyDTO.setUpdatedAtParam(dto.getUpdatedAtParam());
                    modePaiementMoovMoneyDTO.setIsDeletedParam(dto.getIsDeletedParam());
                    modePaiementMoovMoneyDTO.setOrderField(dto.getOrderField());
                    modePaiementMoovMoneyDTO.setOrderDirection(dto.getOrderDirection());

                    return modePaiementMoovMoneyDTO;
                case ProjectConstants.REF_ELEMENT_MODE_PAIEMENT_WAVE_MONEY:
                    ModePaiementWaveDTO modePaiementWaveDTO = new ModePaiementWaveDTO();
                    modePaiementWaveDTO.setId(dto.getId());
                    modePaiementWaveDTO.setDesignation(dto.getDesignation());
                    modePaiementWaveDTO.setDescription(dto.getDescription());
                    modePaiementWaveDTO.setTelephoneWave(dto.getTelephoneGenerique()==null?null:dto.getTelephoneGenerique());

                    modePaiementWaveDTO.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    modePaiementWaveDTO.setTypeModePaiementDesignation(dto.getTypeModePaiementDesignation());

                    modePaiementWaveDTO.setIsDeleted(dto.getIsDeleted());
                    modePaiementWaveDTO.setUpdatedAt(dto.getUpdatedAt());
                    modePaiementWaveDTO.setCreatedAt(dto.getCreatedAt());
                    modePaiementWaveDTO.setDeletedAt(dto.getDeletedAt());
                    modePaiementWaveDTO.setUpdatedBy(dto.getUpdatedBy());
                    modePaiementWaveDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementWaveDTO.setCreatedBy(dto.getCreatedBy());

                    modePaiementWaveDTO.setCreatedAtParam(dto.getCreatedAtParam());
                    modePaiementWaveDTO.setUpdatedAtParam(dto.getUpdatedAtParam());
                    modePaiementWaveDTO.setIsDeletedParam(dto.getIsDeletedParam());
                    modePaiementWaveDTO.setOrderField(dto.getOrderField());
                    modePaiementWaveDTO.setOrderDirection(dto.getOrderDirection());

                    return modePaiementWaveDTO;
                case ProjectConstants.REF_ELEMENT_MODE_PAIEMENT_EN_ESPECE:
                    ModePaiementEnEspeceDTO modePaiementEnEspeceDTO = new ModePaiementEnEspeceDTO();
                    modePaiementEnEspeceDTO.setId(dto.getId());
                    modePaiementEnEspeceDTO.setDesignation(dto.getDesignation());
                    modePaiementEnEspeceDTO.setDescription(dto.getDescription());

                    modePaiementEnEspeceDTO.setCompagnieTransportRaisonSociale(dto.getCompagnieTransportRaisonSociale());
                    modePaiementEnEspeceDTO.setTypeModePaiementDesignation(dto.getTypeModePaiementDesignation());

                    modePaiementEnEspeceDTO.setIsDeleted(dto.getIsDeleted());
                    modePaiementEnEspeceDTO.setUpdatedAt(dto.getUpdatedAt());
                    modePaiementEnEspeceDTO.setCreatedAt(dto.getCreatedAt());
                    modePaiementEnEspeceDTO.setDeletedAt(dto.getDeletedAt());
                    modePaiementEnEspeceDTO.setUpdatedBy(dto.getUpdatedBy());
                    modePaiementEnEspeceDTO.setCreatedBy(dto.getCreatedBy());
                    modePaiementEnEspeceDTO.setCreatedBy(dto.getCreatedBy());

                    modePaiementEnEspeceDTO.setCreatedAtParam(dto.getCreatedAtParam());
                    modePaiementEnEspeceDTO.setUpdatedAtParam(dto.getUpdatedAtParam());
                    modePaiementEnEspeceDTO.setIsDeletedParam(dto.getIsDeletedParam());
                    modePaiementEnEspeceDTO.setOrderField(dto.getOrderField());
                    modePaiementEnEspeceDTO.setOrderDirection(dto.getOrderDirection());
                    return modePaiementEnEspeceDTO;
                default:
                    return new ModePaiementDTO();
            }
        }
        return new ModePaiementDTO();
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0 || str.isEmpty()) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    public static boolean isTrue(Boolean b) {
        return b != null && b;
    }

    public static <T> boolean isNotEmpty(List<T> list) {
        return (list != null && !list.isEmpty());
    }

    public static <T> boolean isEmpty(List<T> list) {
        return (list == null || list.isEmpty());
    }

    public static boolean checkForSQLInjection(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if(fields.length == 0) return true;
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            if (fieldValue instanceof String) {
                String fieldValueStr = (String) fieldValue;
                if (containsSQLInjection(fieldValueStr)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean containsSQLInjection(String value) {
        String[] dangerousKeywords = {
                "SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "ALTER", "EXECUTE",
                "UNION", "JOIN", "HAVING", "FROM", "WHERE", "OR", "AND", "LIKE",
                "--", "/*", "*/", ";"
        };
        return Arrays.stream(dangerousKeywords)
                .anyMatch(keyword -> containsWord(value, keyword));
    }

    private static boolean containsWord(String text, String word) {
        String pattern = "\\b" + word + "\\b";
        return text.matches(".*" + pattern + ".*");
    }


   /*public static boolean notBlank(String str) {
        return str != null && !str.isEmpty() && !str.equals("\n")
                && org.apache.commons.lang3.StringUtils.isNotBlank(str);
    }*/

/*
    public static boolean blank(String str) {
        return !notBlank(str);
    }
*/

   /* public static <T> boolean searchParamIsNotEmpty(SearchParam<T> fieldParam) {
        return fieldParam != null && isNotBlank(fieldParam.getOperator())
                && OperatorEnum.IS_VALID_OPERATOR(fieldParam.getOperator())
                && ((OperatorEnum.IS_BETWEEN_OPERATOR(fieldParam.getOperator()) && fieldParam.getStart() != null
                && isNotBlank(fieldParam.getStart().toString()) && fieldParam.getEnd() != null
                && isNotBlank(fieldParam.getEnd().toString()))
                || (OperatorEnum.IS_IN_OPERATOR(fieldParam.getOperator()) && isNotEmpty(fieldParam.getDatas()))
                || (OperatorEnum.OPERATOR_NOT_NEEDS_ANY_VALUE(fieldParam.getOperator()))
                || (OperatorEnum.OPERATOR_NEEDS_FIELD_VALUE(fieldParam.getOperator())));
    }

    public static <T> boolean searchParamIsNotEmpty(SearchParam<T> fieldParam, T fieldValue) {
        return fieldParam != null && isNotBlank(fieldParam.getOperator())
                && OperatorEnum.IS_VALID_OPERATOR(fieldParam.getOperator())
                && ((OperatorEnum.IS_BETWEEN_OPERATOR(fieldParam.getOperator()) && fieldParam.getStart() != null
                && isNotBlank(fieldParam.getStart().toString()) && fieldParam.getEnd() != null
                && isNotBlank(fieldParam.getEnd().toString()))
                || (OperatorEnum.IS_IN_OPERATOR(fieldParam.getOperator()) && isNotEmpty(fieldParam.getDatas()))
                || (OperatorEnum.OPERATOR_NOT_NEEDS_ANY_VALUE(fieldParam.getOperator()))
                || (OperatorEnum.OPERATOR_NEEDS_FIELD_VALUE(fieldParam.getOperator()) && fieldValue != null
                && isNotBlank(fieldValue.toString())));
    }

    public static String getCurrentLocalDateTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }

    public static String encrypt(String str) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hashedBytes = digest.digest(str.getBytes("UTF-8"));

        return convertByteArrayToHexString(hashedBytes);
    }

    public static String findDateFormatByParsing(String date) {
        if (blank(date)) {
            return null;
        }
        List<String> datasPatterns = new ArrayList<String>();
        datasPatterns.addAll(Arrays.asList("dd/MM/yyyy", "dd-MM-yyyy", "dd.MM.yyyy", "ddMMyyyy"));
        datasPatterns.addAll(Arrays.asList("dd/MM/yyyy HH", "dd-MM-yyyy HH", "dd.MM.yyyy HH", "ddMMyyyy HH"));
        datasPatterns
                .addAll(Arrays.asList("dd/MM/yyyy HH:mm", "dd-MM-yyyy HH:mm", "dd.MM.yyyy HH:mm", "ddMMyyyy HH:mm"));
        datasPatterns.addAll(Arrays.asList("dd/MM/yyyy HH:mm:ss", "dd-MM-yyyy HH:mm:ss", "dd.MM.yyyy HH:mm:ss",
                "ddMMyyyy HH:mm:ss"));
        datasPatterns.addAll(Arrays.asList("dd/MM/yyyy HH:mm:ss.SSS", "dd-MM-yyyy HH:mm:ss.SSS",
                "dd.MM.yyyy HH:mm:ss.SSS", "ddMMyyyy HH:mm:ss.SSS"));

        datasPatterns.addAll(Arrays.asList("yyyy/MM/dd", "yyyy-MM-dd", "yyyy.MM.dd", "yyyyMMdd"));
        datasPatterns.addAll(Arrays.asList("yyyy/MM/dd HH", "yyyy-MM-dd HH", "yyyy.MM.dd HH", "yyyyMMdd HH"));
        datasPatterns
                .addAll(Arrays.asList("yyyy/MM/dd HH:mm", "yyyy-MM-dd HH:mm", "yyyy.MM.dd HH:mm", "yyyyMMdd HH:mm"));
        datasPatterns.addAll(Arrays.asList("yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss",
                "yyyyMMdd HH:mm:ss"));
        datasPatterns.addAll(Arrays.asList("yyyy/MM/dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss.SSS",
                "yyyy.MM.dd HH:mm:ss.SSS", "yyyyMMdd HH:mm:ss.SSS"));

        datasPatterns.addAll(Arrays.asList("dd/MM", "dd-MM", "dd.MM", "ddMM"));
        datasPatterns.addAll(Arrays.asList("MM/yyyy", "MM-yyyy", "MM.yyyy", "MMyyyy"));

        datasPatterns.addAll(Arrays.asList("MM/dd", "MM-dd", "MM.dd", "MMdd"));
        datasPatterns.addAll(Arrays.asList("yyyy/MM", "yyyy-MM", "yyyy.MM", "yyyyMM"));

        datasPatterns.addAll(Arrays.asList("yyyy"));
        datasPatterns.addAll(Arrays.asList("HH", "HH:mm", "HH:mm:ss", "HH:mm:ss.SSS"));

        for (String pattern : datasPatterns) {
            try {
                // SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                // sdf.setLenient(false);
                // sdf.parse(date);

                org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
                formatter.parseDateTime(date);
                return pattern;
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        return null;
    }
*/
    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date convertStringToDate(String dateString) throws ParseException {
        if(dateString==null)  return new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date=formatter.parse(dateString);
        return date;
    }

    public static String convertDateToString(Date date) throws ParseException {
        if(date==null)  return null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString=formatter.format(date);
        return dateString;
    }

    public static Boolean convertStringToBoolean(String str) {
        boolean rtn=Boolean.parseBoolean(str);
        return rtn;
    }

    public static Long convertStringToLong(String str){
        Long rtn = Long.parseLong(str);
        return rtn;
    }

    public static Double convertStringTODouble(String str){
        Double rtn = Double.parseDouble(str);
        return rtn;
    }

    public static boolean isValidID(Long id) {
        return id != null && id > 0;
    }
    public static boolean isValidIntegerID(Integer id) {
        return id != null && id > 0;
    }

    public static String getFrenchDayOfWeek(Date date) {
        LocalDate localDate = convertDateToLocalDate(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE", Locale.FRENCH);
        String dayOfWeekName = localDate.format(formatter);

        return dayOfWeekName;
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }
/*
    public static Integer getLoginUser() {//HttpRequest request doit etre en parametre
        return 1;
    }


    public static String createToken(Users users) {
        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .claim("id", users.getId())
                .claim("login", users.getLogin())
                .setSubject(users.getNom())
                .setId(users.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
                .signWith(hmacKey)
                .compact();
        return jwtToken;
    }

    public static Jws<Claims> decodeToken(String jwtString) {
        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString);

        return jwt;
    }

*/
}