package com.africanb.africanb.helper;

import com.africanb.africanb.helper.status.Status;
import com.africanb.africanb.helper.status.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Locale;


@XmlRootElement
@Component
public class FunctionalError {
    private String code;
    private String message;
    @Autowired
    private MessageSource messageSource;

    //private static Status	status	= new Status();

    public Status FIELD_EMPTY(String message, Locale locale) {
        Status status = new Status();
        status.setCode(StatusCode.FUNC_FIELD_EMPTY);
        status.setMessage(messageSource.getMessage("StatusMessage.FUNC_FIELD_EMPTY", new Object[] {}, locale) + ": " + message);
        return status;
    }

    public Status SUCCESS(String message, Locale locale) {
        Status status = new Status();
        status.setCode(StatusCode.SUCCESS);
        status.setMessage(messageSource.getMessage("StatusMessage.SUCCESS", new Object[]{}, locale) + ": " + message);
        return status;
    }
    public Status SAVE_FAIL(String message, Locale locale) {
        Status status = new Status();
        status.setCode(StatusCode.FUNC_SAVE_FAIL);
        status.setMessage(messageSource.getMessage("StatusMessage.FUNC_SAVE_FAIL", new Object[] {}, locale) + ": " + message);
        return status;
    }

    public Status SEND_MAIL_FAIL(String message, Locale locale) {
        Status status = new Status();
        status.setCode(StatusCode.FUNC_SEND_MAIL_FAIL);
        status.setMessage(messageSource.getMessage("StatusMessage.FUNC_SAVE_FAIL", new Object[] {}, locale) + ": " + message);
        return status;
    }
    public Status DATA_NOT_DELETABLE(String message, Locale locale) {
        Status status = new Status();
        status.setCode(StatusCode.FUNC_DATA_NOT_DELETABLE);
        status.setMessage(messageSource.getMessage("StatusMessage.FUNC_DATA_NOT_DELETABLE", new Object[] {}, locale) + ": " + message);
        return status;
    }
    public Status DATA_NOT_EXIST(String message, Locale locale) {
        Status status = new Status();
        status.setCode(StatusCode.FUNC_DATA_NOT_EXIST);
        status.setMessage(messageSource.getMessage("StatusMessage.FUNC_DATA_NOT_EXIST", new Object[] {}, locale) + ": " + message);
        return status;
    }
    public Status DATA_EXIST(String message, Locale locale) {
        Status status = new Status();
        status.setCode(StatusCode.FUNC_DATA_EXIST);
        status.setMessage(messageSource.getMessage("StatusMessage.FUNC_DATA_EXIST", new Object[] {}, locale) + ": " + message);
        return status;
    }
    public Status DATA_DUPLICATE(String message, Locale locale) {
        Status status = new Status();
        status.setCode(StatusCode.FUNC_DATA_DUPLICATE);
        status.setMessage(messageSource.getMessage("StatusMessage.FUNC_DATA_DUPLICATE", new Object[] {}, locale) + ": " + message);
        return status;
    }

    public Status DATA_VERSION_NULL(String message, Locale locale) {
        Status status = new Status();
        status.setCode(StatusCode.FUNC_DATA_VERSION_NULL);
        status.setMessage(messageSource.getMessage("StatusMessage.FUNC_DATA_VERSION_NULL", new Object[] {}, locale) + ": " + message);
        return status;
    }

}