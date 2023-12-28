package com.africanb.africanb.utils.security;

import com.africanb.africanb.dao.repository.security.UsersRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.contrat.RequestBase;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.security.UsersDTO;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Slf4j
@Component
@WebFilter(filterName = "Filter", urlPatterns = {"/roles"})
@Order(3)
public class SecurityServletFilter extends HttpFilter {

    private final FunctionalError functionalError;
    private final ExceptionUtils exceptionUtils;
    private final UsersRepository usersRepository;

    private static String	defaultTenant	= "null";
    private static String defaultLanguage = "fr";

    public SecurityServletFilter(FunctionalError functionalError, ExceptionUtils exceptionUtils, UsersRepository usersRepository) {
        this.functionalError = functionalError;
        this.exceptionUtils = exceptionUtils;
        this.usersRepository = usersRepository;
    }

    @SneakyThrows
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)  {
        log.info("filter method begin");
        log.info(request.getRequestURI());

        SecurityUtils.languageManager(request);
        String        languageID = (String) request.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale     = new Locale(languageID, "");
        Response<UsersDTO> resp = new Response<UsersDTO>();

        List<String> listUrlDoNotHaveAuthentication = Collections.synchronizedList(new ArrayList<String>());
        listUrlDoNotHaveAuthentication.add("/users/login");

        if (SecurityUtils.doesPathNotRequireAuthentication(request, response, chain)) return;
        try {
            String path = request.getServletPath();
            String token = SecurityUtils.extractToken(request);
            if (token==null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                resp.setStatus( functionalError.DATA_NOT_EXIST("Authentification Echouée", locale));
                resp.setHasError(true);
                response.getWriter().write(String.valueOf(resp));
                return;
            }
            TokenData entityTokenData = SecurityUtils.decodeAndValidateToken(token);
            log.info("_67 Affichage du entityToken ="+ entityTokenData.toString());
            if(Optional.ofNullable(entityTokenData.getStatus())
                    .map(status -> !status.equalsIgnoreCase(ProjectConstants.VERIFY_TOKEN_VALIDE))
                    .orElse(false)) {
                switch (entityTokenData.getStatus()) {
                    case ProjectConstants.VERIFY_TOKEN_EXPIRE:
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                        resp.setStatus( functionalError.DATA_NOT_EXIST(ProjectConstants.VERIFY_TOKEN_EXPIRE, locale));
                        resp.setHasError(true);
                        response.getWriter().write(String.valueOf(resp));
                        break;
                    case ProjectConstants.VERIFY_TOKEN_INVALIDE:
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                        resp.setStatus( functionalError.DATA_NOT_EXIST(ProjectConstants.VERIFY_TOKEN_INVALIDE, locale));
                        resp.setHasError(true);
                        response.getWriter().write(String.valueOf(resp));
                        break;
                    case ProjectConstants.VERIFY_TOKEN_MAUVAIS:
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                        resp.setStatus( functionalError.DATA_NOT_EXIST("Authentification Echouée", locale));
                        resp.setHasError(true);
                        response.getWriter().write(String.valueOf(resp));
                        break;
                    default:
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                        resp.setStatus(functionalError.DATA_NOT_EXIST("Authentification impossible", locale));
                        resp.setHasError(true);
                        response.getWriter().write(String.valueOf(resp));
                        break;
                }
            }
            if (entityTokenData.getClaims() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                resp.setStatus( functionalError.DATA_NOT_EXIST("Authentification Echouée", locale));
                resp.setHasError(true);
                response.getWriter().write(String.valueOf(resp));
                return;
            }
            boolean isAuthenticated = authenticateUser(entityTokenData.getClaims());
            if (!isAuthenticated) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                resp.setStatus( functionalError.DATA_NOT_EXIST("Utilisateur non authentifié", locale));
                resp.setHasError(true);
                response.getWriter().write(String.valueOf(resp));
                return;
            }
            RequestBase.userID = Long.valueOf(entityTokenData.getClaims().getId());
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            exceptionUtils.PERMISSION_DENIED_DATA_ACCESS_EXCEPTION(resp, locale, e);
        } finally {
            if (resp.isHasError() && resp.getStatus() != null) {
                log.info("Erreur| code: {} -  message: {}", resp.getStatus().getCode(), resp.getStatus().getMessage());
            }
        }
    }

    private boolean authenticateUser(Claims token) {
        return Optional.ofNullable(usersRepository.findOne(Long.valueOf(token.getId()), false)).isPresent();
    }

}
