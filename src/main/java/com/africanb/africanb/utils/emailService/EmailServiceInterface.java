package com.africanb.africanb.utils.emailService;

import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import jakarta.mail.MessagingException;

import java.io.FileNotFoundException;
import java.util.Locale;


public interface EmailServiceInterface {
    Response<EmailDTO> sendSimpleEmail(Request<EmailDTO> request, Locale locale);

    Response<EmailDTO> sendEmailWithAttachment(Request<EmailDTO> request, Locale locale) throws FileNotFoundException, MessagingException;
}
