package com.africanb.africanb.helper;

import com.africanb.africanb.helper.contrat.ResponseBase;
import com.africanb.africanb.helper.status.StatusCode;
import com.africanb.africanb.helper.status.StatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ExceptionUtils {

    private static Logger slf4jLogger;

    private final  TechnicalError	technicalError;

    public ExceptionUtils(TechnicalError technicalError) {
        this.technicalError = technicalError;
        slf4jLogger = LoggerFactory.getLogger(getClass());
    }


    public void PERMISSION_DENIED_DATA_ACCESS_EXCEPTION(ResponseBase response, Locale locale, Exception e) {
        // Permission non accordée pour acceder au serveur de BD
        e.printStackTrace();
        response.setHasError(Boolean.TRUE);
        response.setStatus(technicalError.DB_PERMISSION_DENIED(e.getMessage(), locale));
        slf4jLogger.warn("Erreur| code: {} -  message: {} - cause: {}  - SysMessage: {}", StatusCode.TECH_DB_PERMISSION_DENIED, StatusMessage.TECH_DB_PERMISSION_DENIED, e.getCause(), e.getMessage());
    }


    public void DATA_ACCESS_RESOURCE_FAILURE_EXCEPTION(ResponseBase response, Locale locale, Exception e) {
        // base de données indisponible
        e.printStackTrace();
        response.setHasError(Boolean.TRUE);
        response.setStatus(technicalError.DB_FAIL(e.getMessage(), locale));
        slf4jLogger.warn("Erreur| code: {} -  message: {} - cause: {}  - SysMessage: {}", StatusCode.TECH_DB_FAIL, StatusMessage.TECH_DB_FAIL, e.getCause(), e.getMessage());
    }


    public void DATA_ACCESS_EXCEPTION(ResponseBase response, Locale locale, Exception e) {
        // Serveur a refusé la requete
        e.printStackTrace();
        response.setHasError(Boolean.TRUE);
        response.setStatus(technicalError.DB_QUERY_REFUSED(e.getMessage(), locale));
        slf4jLogger.warn("Erreur| code: {} -  message: {} - cause: {}  - SysMessage: {}", StatusCode.TECH_DB_QUERY_REFUSED, StatusMessage.TECH_DB_QUERY_REFUSED, e.getCause(), e.getMessage());
    }


    public void RUNTIME_EXCEPTION(ResponseBase response, Locale locale, Exception e) {
        // Erreur interne
        e.printStackTrace();
        response.setHasError(Boolean.TRUE);
        slf4jLogger.info("exception error "+ e.getMessage());
        response.setStatus(technicalError.ERROR(e.getMessage(), locale));
        slf4jLogger.info("Erreur| code: {} -  message: {} - cause: {}  - SysMessage: {}", response.getStatus().getCode(), StatusMessage.FUNC_FAIL, e.getCause(), response.getStatus().getMessage());
    }


    public void EXCEPTION(ResponseBase response, Locale locale, Exception e) {
        e.printStackTrace();
        response.setHasError(Boolean.TRUE);
        response.setStatus(technicalError.INTERN_ERROR(e.getMessage(), locale));
        slf4jLogger.warn("Erreur| code: {} -  message: {} - cause: {}  - SysMessage: {}", StatusCode.TECH_INTERN_ERROR, StatusMessage.TECH_INTERN_ERROR, e.getCause(), e.getMessage());
        e.printStackTrace();

    }


    public void CANNOT_CREATE_TRANSACTION_EXCEPTION(ResponseBase response, Locale locale, Exception e) {
        e.printStackTrace();
        response.setHasError(Boolean.TRUE);
        response.setStatus(technicalError.DB_NOT_CONNECT(e.getMessage(), locale));
        slf4jLogger.warn("Erreur| code: {} -  message: {} - cause: {}  - SysMessage: {}", StatusCode.TECH_DB_NOT_CONNECT, StatusMessage.TECH_DB_NOT_CONNECT, e.getCause(), e.getMessage());
    }


    public void TRANSACTION_SYSTEM_EXCEPTION(ResponseBase response, Locale locale, Exception e) {
        e.printStackTrace();
        response.setHasError(Boolean.TRUE);
        response.setStatus(technicalError.DB_FAIL(e.getMessage(), locale));
        slf4jLogger.warn("Erreur| code: {} -  message: {} - cause: {}  - SysMessage: {}", StatusCode.TECH_DB_FAIL, StatusMessage.TECH_DB_FAIL, e.getCause(), e.getMessage());
    }

}