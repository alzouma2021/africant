package com.africanb.africanb.rest;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestInterceptor extends HandlerInterceptorAdapter {

    private static final String	defaultTenant	= "null";

    private static final String defaultLanguage = "fr";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
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
        return true;
    }
}