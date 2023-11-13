package com.africanb.africanb.utilities;

import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.utils.emailService.EmailDTO;
import com.africanb.africanb.utils.emailService.EmailServiceBusiness;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EmailServiceTests {

    @InjectMocks
    private EmailServiceBusiness emailServiceBusiness;
    @MockBean
    private JavaMailSender emailSender;

    @MockBean
    private FunctionalError functionalError;

    @MockBean
    private MessageSource messageSource;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendSimpleEmail_Success() {
        Request<EmailDTO> request = new Request<>();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setToAddress("test@example.com");
        emailDTO.setSubject("Test Subject");
        emailDTO.setMessage("Test Message");
        request.setData(emailDTO);

        Locale locale = new Locale("fr","");

        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        Response<EmailDTO> response = emailServiceBusiness.sendSimpleEmail(request, locale);

        assertFalse(response.isHasError());
        assertNotNull(response.getItem());
        assertTrue(response.getItem().getIsSent());
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }


    @Test
    public void testSendSimpleEmail_Fail() {
        Request<EmailDTO> request = new Request<>();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setToAddress("test@example.com");
        emailDTO.setSubject("Test Subject");
        emailDTO.setMessage("Test Message");
        request.setData(emailDTO);

        Locale locale = Locale.ENGLISH;

        doThrow(new MailSendException("Mail sending failed")).when(emailSender).send(any(SimpleMailMessage.class));

        Response<EmailDTO> response = emailServiceBusiness.sendSimpleEmail(request, locale);

        assertTrue(response.isHasError());
        assertNotNull(response.getItem());
        assertFalse(response.getItem().getIsSent());
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    

}
