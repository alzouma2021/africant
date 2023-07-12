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
@Order(1)
public class FilterAuthenticationClient extends HttpFilter {


    @Value("${server.id}")
    private String serverId;
    @Value("${client.id}")
    private String clientId;
    private FunctionalError functionalError;
    private ExceptionUtils exceptionUtils;


    @Autowired
    public FilterAuthenticationClient(FunctionalError functionalError, ExceptionUtils exceptionUtils) {
        this.functionalError = functionalError;
        this.exceptionUtils = exceptionUtils;

    }

    @Override
    public void doFilter(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        //Get Parameters Client
        Response<UsersDTO> resp = new Response<UsersDTO>();
        Locale locale     = new Locale("Fr", "");
        //Initialize Headers
        ParameterHeaderHttp.initializeResponseParamHeaders(servletResponse);
        String serverIdProvider = servletRequest.getHeader("server_id");
        String clientIdProvider = servletRequest.getHeader("client_id");
        log.info("_43 Identifiant du serveur :: Value="+serverIdProvider);
        log.info("_44 Identifiant du client  :: Value="+clientIdProvider);
        IdentificationClient identificationClient = new IdentificationClient();
        String serverIdConsumer =  serverId;
        String clientIdConsumer =  clientId;
        log.info("_43 Identifiant du serveur defini :: Value="+serverIdConsumer);
        log.info("_44 Identifiant du client defini  :: Value="+clientIdConsumer);
        //Check
        if(Utilities.isBlank(serverIdProvider) || Utilities.isBlank(clientIdProvider)){
                servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                resp.setStatus(functionalError.DATA_NOT_EXIST("Client non authentifie", locale));
                resp.setHasError(true);
                servletResponse.getWriter().write(String.valueOf(resp));
                return;
        }
        //Check if paramaters correspond
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
