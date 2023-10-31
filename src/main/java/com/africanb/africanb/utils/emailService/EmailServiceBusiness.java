package com.africanb.africanb.utils.emailService;

import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author ALZOUMA MOUSSA MAHAMADOU
 */
@Component
public class EmailServiceBusiness implements EmailServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceBusiness.class);

    @Autowired(required = true)
    public JavaMailSender emailSender;

    private Response<EmailDTO> response;

    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public EmailServiceBusiness(FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<EmailDTO> sendSimpleEmail(Request<EmailDTO> request, Locale locale) {
        Response<EmailDTO> response = new Response<EmailDTO>();
        if(request.getData() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée",locale));
            response.setHasError(true);
            return response;
        }
        EmailDTO dto = request.getData();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("toAddress", dto.getToAddress());
        fieldsToVerify.put("subject", dto.getSubject());
        fieldsToVerify.put("message", dto.getMessage());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(dto.getToAddress());
        simpleMailMessage.setSubject(dto.getSubject());
        simpleMailMessage.setText(dto.getMessage());
        try{
            emailSender.send(simpleMailMessage);
        }catch (MailException ex){
            log.info("_82 Affichage de log ="+ex.getMessage());
            response.setStatus(functionalError.SEND_MAIL_FAIL("Erreur d'envoi", locale));
            response.setHasError(true);
            dto.setIsSent(false);
            response.setItem(dto);
            return response;
        }
        dto.setIsSent(true);
        response.setItem(dto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<EmailDTO> sendEmailWithAttachment(Request<EmailDTO> request, Locale locale) throws MessagingException, FileNotFoundException {
        Response<EmailDTO> response = new Response<EmailDTO>();
        if(request.getData() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée",locale));
            response.setHasError(true);
            return response;
        }
        EmailDTO dto = request.getData();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("toAddress", dto.getToAddress());
        fieldsToVerify.put("subject", dto.getSubject());
        fieldsToVerify.put("message", dto.getMessage());
        fieldsToVerify.put("attachement", dto.getAttachment());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(dto.getToAddress());
        messageHelper.setSubject(dto.getSubject());
        messageHelper.setText(dto.getMessage());
        FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(dto.getAttachment()));
        messageHelper.addAttachment("Purchase Order", file);
        try{
            emailSender.send(mimeMessage);
        }catch (MailException ex){
            response.setStatus(functionalError.SEND_MAIL_FAIL("Erreur d'envoi", locale));
            response.setHasError(true);
            dto.setIsSent(false);
            response.setItem(dto);
            return response;
        }
        dto.setIsSent(true);
        response.setItem(dto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }
}
