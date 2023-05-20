package com.africanb.africanb.utils.emailService;

import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.FamilleStatusUtilDTO;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.util.Locale;

/**
 * @Author ALZOUMA MOUSSA MAHAMADOU
 */
public interface EmailServiceInterface {
    public Response<EmailDTO> sendSimpleEmail(Request<EmailDTO> request, Locale locale);

    public Response<EmailDTO> sendEmailWithAttachment(Request<EmailDTO> request, Locale locale) throws MessagingException, FileNotFoundException;
}
