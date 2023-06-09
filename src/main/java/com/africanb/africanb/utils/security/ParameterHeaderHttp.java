package com.africanb.africanb.utils.security;

import javax.servlet.http.HttpServletResponse;

/**
 * Cette methode permet de définir parametres Header autorisés
 *
 * @Author Alzouma Moussa Mahamadou
 */

public class ParameterHeaderHttp {

    public static  void initializeResponseParamHeaders(HttpServletResponse response) {
        HttpServletResponse res = response;
        res.setHeader("Access-Control-Allow-Origin","*");
        res.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE");
        res.setHeader("Access-Control-Allow-Credentials","true");
        res.setHeader("Access-Control-Allow-Headers","Origin, Access-Control-Allow-Methods, " +
                "Accept, X-Requested-With, Content-Type, Access-Control-Allow-Origin, Access-Control-Request-Method, Authorization," +
                "Access-Control-Request-Headers, Show-Success-Message, Show-Loader, Show-Error-Message, sessionUser, lang, user, token, sessionuser, xsrf-token, server_id, client_id");
        res.addHeader("Access-Control-Expose-Headers", " xsrf-token");

    }

}
