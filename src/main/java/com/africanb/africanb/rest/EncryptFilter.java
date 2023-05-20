
package com.africanb.africanb.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EncryptFilter extends HttpFilter {

    private static final String	defaultTenant	= "null";
    private static final String defaultLanguage = "fr";
    private Logger slf4jLogger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        slf4jLogger.info("begin doFilter");

        languageManager(request);
        String  languageId = (String) request.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageId,"");

        HttpServletRequest  req  =  request;
        HttpServletResponse res  = response;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        res.setHeader("Access-Control-Allow-Headers",
                "Origin, access-control-allow-methods,Accept, X-Requested-With, Content-Type,Access-Control-Allow-Origin, Access-Control-Request-Method, Access-Control-Request-Headers, Show-Success-Message, Show-Loader, Show-Error-Message,sessionUser,lang,user,token,sessionuser,content-type");
        res.setHeader("X-XSS-Protection", "1; mode=block");
        res.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        res.setHeader("X-Content-Type-Options", "nosniff");
        res.setHeader("Cache-control", "no-store, no-cache");
        res.setHeader("X-Frame-Options", "DENY");
        chain.doFilter(request, response);
        slf4jLogger.info("end doFilter");
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
    }

    private static void languageManager(HttpServletRequest req) {
        String tenantValue = req.getHeader("tenantID");
        if (tenantValue != null) {
            req.setAttribute("CURRENT_TENANT_IDENTIFIER", tenantValue);
        } else {
            req.setAttribute("CURRENT_TENANT_IDENTIFIER", defaultTenant);
        }

        String langValue = req.getHeader("lang");
        if (langValue != null) {
            req.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", langValue);
        } else {
            req.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", defaultLanguage);
        }
    }

}
