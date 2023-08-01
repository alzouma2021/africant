package com.africanb.africanb.utils.security;

import com.africanb.africanb.dao.entity.security.Users;
import com.africanb.africanb.dao.repository.security.UsersRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.contrat.RequestBase;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.security.UsersDTO;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Slf4j
@Component
@WebFilter(filterName = "Filter", urlPatterns = {"/roles"})
@Order(2)
public class SecurityServletFilter extends HttpFilter {

    @Autowired
    private FunctionalError functionalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private UsersRepository usersRepository;

    private static String	defaultTenant	= "null";
    private static String defaultLanguage = "fr";

    @SneakyThrows
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)  {
        log.info("filter method begin");
        log.info(request.getRequestURI());

        SecurityServices.languageManager(request);
        String        languageID = (String) request.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale     = new Locale(languageID, "");
        Response<UsersDTO> resp = new Response<UsersDTO>();

        //ULRS exemptions authorization
        List<String> listUrlDoNotHaveAuthentication = Collections.synchronizedList(new ArrayList<String>());
        listUrlDoNotHaveAuthentication.add("/users/login");
        inializationResponseParamHeaders((HttpServletResponse) response);
        //Check Options
        if (SecurityServices.checkIfRequestHasNotNeedAuthentication(request, response, chain)) return;
        try {
            String path = request.getServletPath();
            String token = SecurityServices.extractToken(request);
            if (token==null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                resp.setStatus( functionalError.DATA_NOT_EXIST("Authentification Echouée", locale));
                resp.setHasError(true);
                response.getWriter().write(String.valueOf(resp));
                return;
            }
            Token entityToken = SecurityServices.decodeAndValidateToken(token);
            String status = entityToken.getStatus();
            if (status != null) {
                switch (status) {
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
            } else {
                if (entityToken.getJwt() == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                    resp.setStatus( functionalError.DATA_NOT_EXIST("Authentification Echouée", locale));
                    resp.setHasError(true);
                    response.getWriter().write(String.valueOf(resp));
                    return;
                }
                boolean isAuthenticated = authenticateUser(entityToken.getJwt());
                if (!isAuthenticated) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401.
                    resp.setStatus( functionalError.DATA_NOT_EXIST("Utilisateur non authentifié", locale));
                    resp.setHasError(true);
                    response.getWriter().write(String.valueOf(resp));
                    return;
                }
                RequestBase.userID = Integer.valueOf(entityToken.getJwt().getBody().getId());
                chain.doFilter(request, response);
            }
        } catch (IOException | ServletException e) {
            exceptionUtils.PERMISSION_DENIED_DATA_ACCESS_EXCEPTION(resp, locale, e);
        } finally {
            if (resp.isHasError() && resp.getStatus() != null) {
                log.info("Erreur| code: {} -  message: {}", resp.getStatus().getCode(), resp.getStatus().getMessage());
                //throw new RuntimeException(resp.getStatus().getCode() + ";" + resp.getStatus().getMessage());
            }
        }
    }


    private void inializationResponseParamHeaders(HttpServletResponse response) {

        HttpServletResponse res = response;
        res.setHeader("Access-Control-Allow-Origin","*");
        res.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE");
        res.setHeader("Access-Control-Allow-Credentials","true");
        res.setHeader("Access-Control-Allow-Headers","Origin, Access-Control-Allow-Methods, " +
                "Accept, X-Requested-With, Content-Type, Access-Control-Allow-Origin, Access-Control-Request-Method, Authorization," +
                "Access-Control-Request-Headers, Show-Success-Message, Show-Loader, Show-Error-Message, sessionUser, lang, user, token, sessionuser, xsrf-token");
        res.addHeader("Access-Control-Expose-Headers", " xsrf-token");

    }

    private boolean authenticateUser(Jws<Claims> token) {
        Long userId = Long.valueOf(Integer.valueOf(token.getBody().getId()));
        Users user = usersRepository.findOne(userId,false);
        if(user == null){
            return false;
        }
        return true;
    }
}
