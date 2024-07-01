package com.africanb.africanb.Business.design.factory.modeAbonnement;

import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPeriodiqueDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPrelevementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.ModeAbonnementDTO;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ModeAbonnementUtils {

    public static ModeAbonnementDTO transformAbstractClassIntoChildClass(ModeAbonnementDTO dto) {
        if (dto.getTypeModeAbonnementDesignation() != null ){
            switch (dto.getTypeModeAbonnementDesignation()) {
                case ProjectConstants.REF_ELEMENT_ABONNEMENT_PERIODIQUE -> {
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
                }
                case ProjectConstants.REF_ELEMENT_ABONNEMENT_PRELEVEMENT -> {
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
                }
                default ->{
                    return new ModeAbonnementDTO();
                }
            }
        }
        return new ModeAbonnementDTO();
    }
}
