package com.africanb.africanb.helper.enums;

public enum FunctionalityEnum {

    DEFAULT("DEFAULT"),

    // PAYS
    VIEW_PAYS("VIEW_PAYS"),
    CREATE_PAYS("CREATE_PAYS"),
    UPDATE_PAYS("UPDATE_PAYS"),
    DELETE_PAYS("DELETE_PAYS"),

    //FamilleStatusUtil
    VIEW_FAMILLESTATUSUTIL("VIEW_FAMILLESTATUSUTIL"),
    CREATE_FAMILLESTATUSUTIL("CREATE_FAMILLESTATUSUTIL"),
    UPDATE_FAMILLESTATUSUTIL("UPDATE_FAMILLESTATUSUTIL"),
    DELETE_FAMILLESTATUSUTIL("DELETE_FAMILLESTATUSUTIL"),

    //ReferenceFamille
    VIEW_REFERENCEFAMILLE("VIEW_REFERENCEFAMILLE"),
    CREATE_REFERENCEFAMILLE("CREATE_REFERENCEFAMILLE"),
    UPDATE_REFERENCEFAMILLE("UPDATE_REFERENCEFAMILLE"),
    DELETE_REFERENCEFAMILLE("DELETE_REFERENCEFAMILE"),


    //Reference
    VIEW_REFERENCE("VIEW_REFERENCE"),
    CREATE_REFERENCE("CREATE_REFERENCE"),
    UPDATE_REFERENCE("UPDATE_REFERENCE"),
    DELETE_REFERENCE("DELETE_REFERENCE"),


    //CompagnieTransport
    VIEW_COMPAGNIETRANSPORT("VIEW_COMPAGNIETRANSPORT"),
    CREATE_COMPAGNIETRANSPORT("CREATE_COMPAGNIETRANSPORT"),
    UPDATE_COMPAGNIETRANSPORT("UPDATE_COMPAGNIETRANSPORT"),
    DELETE_COMPAGNIETRANSPORT("DELETE_COMPAGNIETRANSPORT"),

    //PrixOffreVoyage
    VIEW_PRIXOFFREVOYAGE("VIEW_PRIXOFFREVOYAGE"),
    CREATE_PRIXOFFREVOYAGE("CREATE_PRIXOFFREVOYAGE"),
    UPDATE_PRIXOFFREVOYAGE("UPDATE_PRIXOFFREVOYAGE"),
    DELETE_PRIXOFFREVOYAGE("DELETE_PRIXOFFREVOYAGE"),

    //OffreVoyage
    VIEW_OFFREVOYAGE("VIEW_OFFREVOYAGE"),
    CREATE_OFFREVOYAGE("CREATE_OFFREVOYAGE"),
    UPDATE_OFFREVOYAGE("UPDATE_OFFREVOYAGE"),
    DELETE_OFFREVOYAGE("DELETE_OFFREVOYAGE"),


    //OffreVoyage
    VIEW_JOURSEMAINE("VIEW_JOURSEMAINE"),
    CREATE_JOURSEMAINE("CREATE_JOURSEMAINE"),
    UPDATE_JOURSEMAINE("UPDATE_JOURSEMAINE"),
    DELETE_JOURSEMAINE("DELETE_JOURSEMAINE"),


    //OffreVoyage
    VIEW_VILLEESCALE("VIEW_VILLEESCALE"),
    CREATE_VILLEESCALE("CREATE_VILLEESCALE"),
    UPDATE_VILLEESCALE("UPDATE_VILLEESCALE"),
    DELETE_VILLEESCALE("DELETE_VILLEESCALE"),

    //OffreVoyage
    VIEW_BUS("VIEW_BUS"),
    CREATE_BUS("CREATE_BUS"),
    UPDATE_BUS("UPDATE_BUS"),
    DELETE_BUS("DELETE_BUS"),

    //Functionnality
    VIEW_FUNCTIONALITY("VIEW_FUNCTIONALITY"),
    CREATE_FUNCTIONALITY("CREATE_FUNCTIONALITY"),
    UPDATE_FUNCTIONALITY("UPDATE_FUNCTIONALITY"),
    DELETE_FUNCTIONALITY("DELETE_FUNCTIONALITY"),

    //Role
    VIEW_ROLE("VIEW_ROLe"),
    CREATE_ROLE("CREATE_ROLE"),
    UPDATE_ROLE("UPDATE_ROLE"),
    DELETE_ROLE("DELETE_ROLE"),


    //OffreVoyage
    VIEW_PROPRIETEOFFREVOYAGE("VIEW_PROPRIETEOFFREVOYAGEE"),
    CREATE_PROPRIETEOFFREVOYAGE("CREATE_PROPRIETEOFFREVOYAGE"),
    UPDATE_PROPRIETEOFFREVOYAGE("UPDATE_PROPRIETEOFFREVOYAGE"),
    DELETE_PROPRIETEOFFREVOYAGE("DELETE_PROPRIETEOFFREVOYAGE");


    private final String value;
    public String getValue() {
        return value;
    }
    FunctionalityEnum(String value) {
        this.value = value;
    }


}
