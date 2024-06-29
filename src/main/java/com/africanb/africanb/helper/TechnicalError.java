package com.africanb.africanb.helper;

import com.africanb.africanb.helper.status.Status;
import com.africanb.africanb.helper.status.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;


@Component
public class TechnicalError {

    private String			code;
    private String			message;
    private  final static Logger log = Logger.getLogger("");

    @Autowired
    private MessageSource messageSource;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechnicalError that = (TechnicalError) o;
        return Objects.equals(code, that.code) && Objects.equals(message, that.message) && Objects.equals(messageSource, that.messageSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, messageSource);
    }
    //private static Status	status	= new Status();

    public Status DB_NOT_CONNECT(String message, Locale locale) {
        Status	status	= new Status();
        status.setCode(StatusCode.TECH_DB_NOT_CONNECT);
        status.setMessage(messageSource.getMessage("StatusMessage.TECH_DB_NOT_CONNECT", new Object[] {}, locale) + ": " + message);
        return status;
    }

    public Status DB_FAIL(String message, Locale locale) {
        Status	status	= new Status();
        status.setCode(StatusCode.TECH_DB_FAIL);
        status.setMessage(messageSource.getMessage("StatusMessage.TECH_DB_FAIL", new Object[] {}, locale) + ": " + message);
        return status;
    }

    public Status INTERN_ERROR(String message, Locale locale) {
        Status	status	= new Status();
        status.setCode(StatusCode.TECH_INTERN_ERROR);
        String msg = messageSource.getMessage("StatusMessage.TECH_INTERN_ERROR", new Object[] {}, locale);
        status.setMessage(msg + ": " + message);
        return status;
    }

    public Status DB_PERMISSION_DENIED(String message, Locale locale) {
        Status	status	= new Status();
        status.setCode(StatusCode.TECH_DB_PERMISSION_DENIED);
        status.setMessage(messageSource.getMessage("StatusMessage.TECH_DB_PERMISSION_DENIED", new Object[] {}, locale) + ": " + message);
        return status;
    }

    public Status DB_QUERY_REFUSED(String message, Locale locale) {
        Status	status	= new Status();
        status.setCode(StatusCode.TECH_DB_QUERY_REFUSED);
        status.setMessage(messageSource.getMessage("StatusMessage.TECH_DB_QUERY_REFUSED", new Object[] {}, locale) + ": " + message);
        return status;
    }

    public Status ERROR(String message, Locale locale) {
        Status	status	= new Status();
        String[] msgTab = message.split(";");
        if (msgTab != null && msgTab.length > 1) {
            status.setCode(msgTab[0]);
            String errorMessage = msgTab[1];
            log.info(errorMessage);
            if(msgTab.length > 2) {
                for (int i=2; i<msgTab.length; i++) {
                    errorMessage += ";"+msgTab[i];
                }
            }
            status.setMessage(errorMessage);
        } else {
            status.setCode(StatusCode.FUNC_FAIL);
            status.setMessage(message);
        }
        return status;
    }
}