package com.africanb.africanb.utils.emailService;

import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.util.*;


@Component
public class EmailServiceBusiness implements EmailServiceInterface {

    @Autowired
    public JavaMailSender emailSender;

    private final FunctionalError functionalError;

    public EmailServiceBusiness(FunctionalError functionalError) {
        this.functionalError = functionalError;
    }

    @Override
    public Response<EmailDTO> sendSimpleEmail(Request<EmailDTO> request, Locale locale) {
        Response<EmailDTO> response = new Response<>();
        if(request.getData() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée",locale));
            response.setHasError(true);
            return response;
        }
        EmailDTO dto = request.getData();
        Map<String, Object> fieldsToVerify = new HashMap<>();
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
            response.setHasError(true);
            dto.setIsSent(false);
            response.setItem(dto);
            return response;
        }
        dto.setIsSent(true);
        response.setItem(dto);
        response.setHasError(false);
        return response;
    }

    @Override
    public Response<EmailDTO> sendEmailWithAttachment(Request<EmailDTO> request, Locale locale) throws FileNotFoundException, MessagingException {
        Response<EmailDTO> response = new Response<>();
        if(request.getData() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée",locale));
            response.setHasError(true);
            return response;
        }
        EmailDTO dto = request.getData();
        Map<String, Object> fieldsToVerify = new HashMap<>();
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
        return response;
    }
}
