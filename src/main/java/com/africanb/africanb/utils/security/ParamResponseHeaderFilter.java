package com.africanb.africanb.utils.security;

import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.security.UsersDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;


@Slf4j
@Component
@Order(1)
public class ParamResponseHeaderFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Headers", "Authorization, server_id, client_id, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Allow-Origin, Access-Control-Request-Method, Access-Control-Request-Headers, Show-Success-Message, Show-Loader, Show-Error-Message, sessionUser, lang, user, token, sessionuser, xsrf-token");
        res.setHeader("Access-Control-Expose-Headers", "xsrf-token");

        chain.doFilter(servletRequest, servletResponse);
    }

}
