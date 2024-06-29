package com.africanb.africanb.utils.emailService;


public class EmailUtils {

    public static String bodyHtmlMailCreateCompagny() {
        return """
                Bonjour Madame/Monsieur,
                Par ce présent, nous venons vous informer de la création effective de votre compagnie de transport sur notre plateforme Africanb.
                Nous allons procéder à la vérification de votre demande d'adhésion à notre compagnie.
                Par conséquent, vous recevrez ultérieurement une reponse de notre part.
                Cordialement,
                """;
    }

    public static String bodyHtmlMailValidationCompagny() {
        return """
                Bonjour Madame/Monsieur,
                Par ce présent, nous aons le plaisir de vous informer de la validation effective de votre compagnie de transport sur notre plateforme Africanb.
                Donc, maintenant vous êtes en mesure de pouvoir créer vos offres de voyage à partir de votre compte.
                Nous sommes disponibles pour touutes informations complèmentaires.
                Cordialement,
                """;
    }

}
