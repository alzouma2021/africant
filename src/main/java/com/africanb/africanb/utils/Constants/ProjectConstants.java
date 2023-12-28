package com.africanb.africanb.utils.Constants;

public class ProjectConstants {

    //Statuts Util Constants
    public final static String COMPAGNIE_TRANSPORT_ENCOURS_TRAITEMENT = "compagnieTransportEnCoursTraitement";
    public final static String COMPAGNIE_TRANSPORT_VALIDE = "compagnieTransportValide";

    //Type Propriete Constant
    public final static String REF_ELEMENT_TYPE_INTEGER="refElementInteger";
    public final static String REF_ELEMENT_TYPE_BOOLEAN="refElementBoolean";
    public final static String REF_ELEMENT_TYPE_LONG="refElementLong";
    public final static String REF_ELEMENT_TYPE_DOUBLE="refElementDouble";
    public final static String REF_ELEMENT_TYPE_STRING="refElementString";
    public final static String REF_ELEMENT_TYPE_DATE="refElementDate";
    public final static String REF_ELEMENT_TYPE_DATETIME="refElementDateTime";
    public final static String REF_ELEMENT_TYPE_FLOAT="refElementFloat";

    //ModeAbonnement
    public final static String REF_ELEMENT_ABONNEMENT_PERIODIQUE="AbonnementPeriodique";
    public final static String REF_ELEMENT_ABONNEMENT_PRELEVEMENT="AbonnementPrelevement";

    //ModePaiement
    public final static String REF_ELEMENT_MODE_PAIEMENT_ORANGE_MONEY="ModePaiementOrangeMoney";
    public final static String REF_ELEMENT_MODE_PAIEMENT_MTN_MONEY="ModePaiementMtnMoney";
    public final static String REF_ELEMENT_MODE_PAIEMENT_MOOV_MONEY="ModePaiementMoovMoney";
    public final static String REF_ELEMENT_MODE_PAIEMENT_WAVE_MONEY="ModePaiementWaveMoney";
    public final static String REF_ELEMENT_MODE_PAIEMENT_EN_ESPECE="ModePaiementEnEspece";

    //ReservationBillet
    public final static String REF_ELEMENT_RESERVATION_CREE="ReservationCree";
    public final static String REF_ELEMENT_RESERVATION_PAYEE_ET_NON_EFFECTIVE="ReservationPayeeEtNonEffective";
    public final static String REF_ELEMENT_RESERVATION_PAYEE_ET_EFFECTIVE="ReservationPayeeEtEffective";

    //Differents roles
    public final static String ROLE_ADMIN_SOCIETE_MERE="RoleAdminSocieteMere";
    public final static String ROLE_ADMIN_COMPAGNIE_TRANSPORT="RoleAdminCompagnieTransport";
    public final static String ROLE_UTI_GARE_COMPAGNIE_TRANSPORT="RoleUtiGareCompagnieTransport";
    public final static String ROLE_UTI_SIMPLE="RoleUtiSimple";

    //Differentes fonctionnalites
    public final static String FUNCTIONNALITY_CREATE_REFERENCE="FunctionnalityCreateReference";
    public final static String FUNCTIONNALITY_LISTING_REFERENCE="FunctionnalityListingReference";
    public final static String FUNCTIONNALITY_UPDATE_REFERENCE="FunctionnalityUpdateReference";
    public final static String FUNCTIONNALITY_DELETE_REFERENCE="FunctionnalityDeleteReference";

    public final static String FUNCTIONNALITY_CREATE_PAYS="FunctionnalityCreatePays";
    public final static String FUNCTIONNALITY_LISTING_PAYS="FunctionnalityListingPays";
    public final static String FUNCTIONNALITY_UPDATE_PAYS="FunctionnalityUpdatePays";
    public final static String FUNCTIONNALITY_DELETE_PAYS="FunctionnalityDeletePays";

    public final static String FUNCTIONNALITY_CREATE_VILLE="FunctionnalityCreateVille";
    public final static String FUNCTIONNALITY_LISTING_VILLE="FunctionnalityListingVille";
    public final static String FUNCTIONNALITY_UPDATE_VILLE="FunctionnalityUpdateVille";
    public final static String FUNCTIONNALITY_DELETE_VILLE="FunctionnalityDeleteVille";

    public final static String FUNCTIONNALITY_DEMANDE_ADHESION="FunctionnalityDemandeAdhesion";
    public final static String FUNCTIONNALITY_CREATE_COMPAGNIE_TRANSPORT="FunctionnalityCreateCompagnieTransport";
    public final static String FUNCTIONNALITY_UPDATE_COMPAGNIE_TRANSPORT="FunctionnalityUpdateCompagnieTransport";
    public final static String FUNCTIONNALITY_DELETE_COMPAGNIE_TRANSPORT="FunctionnalityDeleteCompagnieTransport";
    public final static String FUNCTIONNALITY_LISTING_COMPAGNIE_TRANSPORT="FunctionnalityListingCompagnieTransport";
    public final static String FUNCTIONNALITY_LISTING_COMPAGNIE_TRANSPORT_VALIDE="FunctionnalityListingCompagnieTransportValide";
    public final static String FUNCTIONNALITY_VALIDATE_COMPAGNIE_TRANSPORT="FunctionnalityValidateCompagnieTransport";
    public final static String FUNCTIONNALITY_RATTACHE_ATTESTION_TRANSPORT="FunctionnalityRattacheAttestionTransport";
    public final static String FUNCTIONNALITY_READ_RATTACHE_ATTESTION_TRANSPORT="FunctionnalityReadRattacheAttestionTransport";

    public final static String FUNCTIONNALITY_CREATE_OFFRE_VOYAGE="FunctionnalityCreateOffreVoyage";
    public final static String FUNCTIONNALITY_UPDATE_OFFRE_VOYAGE="FunctionnalityUpdateOffreVoyage";
    public final static String FUNCTIONNALITY_DELETE_OFFRE_VOYAGE="FunctionnalityDeleteOffreVoyage";
    public final static String FUNCTIONNALITY_LISTING_OFFRE_VOYAGE="FunctionnalityListingOffreVoyage";
    public final static String FUNCTIONNALITY_ENABLE_OFFRE_VOYAGE="FunctionnalityEnableOffreVoyage";
    public final static String FUNCTIONNALITY_DISABLE_OFFRE_VOYAGE="FunctionnalityDisableOffreVoyage";

    public final static String FUNCTIONNALITY_CREATE_PRIX_OFFRE_VOYAGE="FunctionnalityCreatePrixOffreVoyage";
    public final static String FUNCTIONNALITY_UPDATE_PRIX_OFFRE_VOYAGE="FunctionnalityUpdatePrixOffreVoyage";
    public final static String FUNCTIONNALITY_DELETE_PRIX_OFFRE_VOYAGE="FunctionnalityDeletePrixOffreVoyage";
    public final static String FUNCTIONNALITY_LISTING_PRIX_OFFRE_VOYAGE="FunctionnalityListingPrixOffreVoyage";

    public final static String FUNCTIONNALITY_CREATE_BUS_OFFRE_VOYAGE="FunctionnalityCreateBusOffreVoyage";
    public final static String FUNCTIONNALITY_UPDATE_BUS_OFFRE_VOYAGE="FunctionnalityUpdateBusOffreVoyage";
    public final static String FUNCTIONNALITY_DELETE_BUS_OFFRE_VOYAGE="FunctionnalityDeleteBusOffreVoyage";
    public final static String FUNCTIONNALITY_LISTING_BUS_OFFRE_VOYAGE="FunctionnalityListingBusOffreVoyage";

    public final static String FUNCTIONNALITY_CREATE_JOUR_OFFRE_VOYAGE="FunctionnalityCreateJourOffreVoyage";
    public final static String FUNCTIONNALITY_UPDATE_JOUR_OFFRE_VOYAGE="FunctionnalityUpdateJourOffreVoyage";
    public final static String FUNCTIONNALITY_DELETE_JOUR_OFFRE_VOYAGE="FunctionnalityDeleteJourOffreVoyage";
    public final static String FUNCTIONNALITY_LISTING_JOUR_OFFRE_VOYAGE="FunctionnalityListingJourOffreVoyage";

    public final static String FUNCTIONNALITY_CREATE_PROGRAMME_OFFRE_VOYAGE="FunctionnalityCreateProgrammeOffreVoyage";
    public final static String FUNCTIONNALITY_UPDATE_PROGRAMME_OFFRE_VOYAGE="FunctionnalityUpdateProgrammeOffreVoyage";
    public final static String FUNCTIONNALITY_DELETE_PROGRAMME_OFFRE_VOYAGE="FunctionnalityDeleteProgrammeOffreVoyage";
    public final static String FUNCTIONNALITY_LISTING_PROGRAMME_OFFRE_VOYAGE="FunctionnalityListingProgrammeOffreVoyage";

    public final static String FUNCTIONNALITY_CREATE_VILLE_ESCALE="FunctionnalityCreateVilleEscale";
    public final static String FUNCTIONNALITY_UPDATE_VILLE_ESCALE="FunctionnalityUpdateVilleEscale";
    public final static String FUNCTIONNALITY_DELETE_VILLE_ESCALE="FunctionnalityDeleteVilleEscale";
    public final static String FUNCTIONNALITY_LISTING_VILLE_ESCALE="FunctionnalityListingVilleEscale";

    public final static String FUNCTIONNALITY_CREATE_PROPRIETE_OFFRE_VOYAGE="FunctionnalityCreateProprieteOffreVoyage";
    public final static String FUNCTIONNALITY_UPDATE_PROPRIETE_OFFRE_VOYAGE="FunctionnalityUpdateProprieteOffreVoyage";
    public final static String FUNCTIONNALITY_DELETE_PROPRIETE_OFFRE_VOYAGE="FunctionnalityDeleteProprieteOffreVoyage";
    public final static String FUNCTIONNALITY_LISTING_PROPRIETE_OFFRE_VOYAGE="FunctionnalityListingProprieteOffreVoyage";

    public final static String FUNCTIONNALITY_CREATE_CARACTERISTIQUE_OFFRE_VOYAGE="FunctionnalityCreateCaracteristiqueOffreVoyage";
    public final static String FUNCTIONNALITY_UPDATE_CARACTERISTIQUE_OFFRE_VOYAGE="FunctionnalityUpdateCaracteristiqueOffreVoyage";
    public final static String FUNCTIONNALITY_DELETE_CARACTERISTIQUE_OFFRE_VOYAGE="FunctionnalityDeleteCaracteristiqueOffreVoyage";
    public final static String FUNCTIONNALITY_LISTING_CARACTERISTIQUEE_OFFRE_VOYAGE="FunctionnalityListingCaracteristiqueOffreVoyage";

    public final static String FUNCTIONNALITY_CREATE_GARE_TRANSPORT="FunctionnalityCreateGareTransport";
    public final static String FUNCTIONNALITY_UPDATE_GARE_TRANSPORT="FunctionnalityUpdateGareTransport";
    public final static String FUNCTIONNALITY_DELETE_GARE_TRANSPORT="FunctionnalityDeleteGareTransport";
    public final static String FUNCTIONNALITY_LISTING_GARE_TRANSPORT="FunctionnalityListingGareTransport";

    public final static String FUNCTIONNALITY_CREATE_BAGAGE="FunctionnalityCreateBagage";
    public final static String FUNCTIONNALITY_UPDATE_BAGAGE="FunctionnalityUpdateBagage";
    public final static String FUNCTIONNALITY_DELETE_BAGAGE="FunctionnalityDeleteBagage";
    public final static String FUNCTIONNALITY_LISTING_BAGAGE="FunctionnalityListingBagage";

    public final static String FUNCTIONNALITY_CREATE_MODE_ABONNEMENT="FunctionnalityCreateModeAbonnement";
    public final static String FUNCTIONNALITY_UPDATE_MODE_ABONNEMENT="FunctionnalityUpdateModeAbonnement";
    public final static String FUNCTIONNALITY_DELETE_MODE_ABONNEMENT="FunctionnalityDeleteModeAbonnement";
    public final static String FUNCTIONNALITY_LISTING_MODE_ABONNEMENT="FunctionnalityListingModeAbonnement";

    public final static String FUNCTIONNALITY_CREATE_MODE_PAIEMENT="FunctionnalityCreateModePaiement";
    public final static String FUNCTIONNALITY_UPDATE_MODE_PAIEMENT="FunctionnalityUpdateModePaiement";
    public final static String FUNCTIONNALITY_DELETE_MODE_PAIEMENT="FunctionnalityDeleteModePaiement";
    public final static String FUNCTIONNALITY_LISTING_MODE_PAIEMENT="FunctionnalityListingModePaiement";

    //Variables constantes authentication

    public static final String SESSION_TOKEN_FILED_USER_ID = "id";
    public static final String SESSION_TOKEN_FILED_USER_NOM = "nom";
    public static final String SESSION_TOKEN_FILED_USER_PRENOM = "prenom";
    public static final String SESSION_TOKEN_FILED_USER_MAIL = "mail";
    public static final String SESSION_TOKEN_FILED_USER_LOGIN = "login";
    public static final String SESSION_TOKEN_FILED_USER_ROLE = "role";
    public static final String SESSION_TOKEN_FILED_USER_COMPAGNIE = "raisonSociale";

    public static final String VERIFY_TOKEN_VALIDE = "valide";
    public static final String VERIFY_TOKEN_EXPIRE = "expire";
    public static final String VERIFY_TOKEN_INVALIDE = "invalide";
    public static final String VERIFY_TOKEN_MAUVAIS = "mauvais";

    //Default Password
    public static final String USER_PASSWORD_DEFAULT="0123456789";

}
