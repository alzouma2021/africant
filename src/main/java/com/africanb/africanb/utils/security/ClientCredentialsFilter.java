package com.africanb.africanb.utils.security;

import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.security.UsersDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;


@Slf4j
@Component
@Order(2)
public class ClientCredentialsFilter extends OncePerRequestFilter {

    @Value("${server.id}")
    private String serverId;
    @Value("${client.id}")
    private String clientId;
    private final FunctionalError functionalError;


    @Autowired
    public ClientCredentialsFilter(FunctionalError functionalError) {
        this.functionalError = functionalError;
    }

    @Override
    public void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        //Get Parameters Client
        Response<UsersDTO> resp = new Response<>();
        Locale locale     = new Locale("Fr", "");

        if (JwtUtils.doesPathNotRequireAuthentication(servletRequest, servletResponse, chain)) return;

        String serverIdProvider = servletRequest.getHeader("server_id");
        String clientIdProvider = servletRequest.getHeader("client_id");
        String serverIdConsumer =  serverId;
        String clientIdConsumer =  clientId;

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
