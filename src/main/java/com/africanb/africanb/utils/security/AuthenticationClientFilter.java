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

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;


@Slf4j
@Component
@Order(2)
public class AuthenticationClientFilter extends HttpFilter {

    @Value("${server.id}")
    private String serverId;
    @Value("${client.id}")
    private String clientId;
    private FunctionalError functionalError;
    private ExceptionUtils exceptionUtils;

    @Autowired
    public AuthenticationClientFilter(FunctionalError functionalError, ExceptionUtils exceptionUtils) {
        this.functionalError = functionalError;
        this.exceptionUtils = exceptionUtils;
    }

    @Override
    public void doFilter(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        //Get Parameters Client
        Response<UsersDTO> resp = new Response<UsersDTO>();
        Locale locale     = new Locale("Fr", "");
        //Initialize Headers
        String serverIdProvider = servletRequest.getHeader("server_id");
        String clientIdProvider = servletRequest.getHeader("client_id");
        IdentificationClient identificationClient = new IdentificationClient();
        String serverIdConsumer =  serverId;
        String clientIdConsumer =  clientId;
        //

        //Check Options
        if (SecurityServices.checkIfRequestHasNotNeedAuthentication(servletRequest, servletResponse, chain)) return;
        //Check
        if(Utilities.isBlank(serverIdProvider) || Utilities.isBlank(clientIdProvider)){
                servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                resp.setStatus(functionalError.DATA_NOT_EXIST("Client non authentifie", locale));
                resp.setHasError(true);
                servletResponse.getWriter().write(String.valueOf(resp));
                return;
        }
        if(!(serverIdProvider.equals(serverIdConsumer) && clientIdProvider.equals(clientIdConsumer))){
            servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
            resp.setStatus(functionalError.DATA_NOT_EXIST("Client non authentifie", locale));
            resp.setHasError(true);
            servletResponse.getWriter().write(String.valueOf(resp));
            return;
        }
        chain.doFilter(servletRequest, servletResponse); // (4)
    }
}
