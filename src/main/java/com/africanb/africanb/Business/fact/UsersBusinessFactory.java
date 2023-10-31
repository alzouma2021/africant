package com.africanb.africanb.Business.fact;


import com.africanb.africanb.Business.security.UsersBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.security.UsersDTO;
import com.africanb.africanb.helper.enums.FunctionalityEnum;
import lombok.extern.java.Log;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Log
@Component
public class UsersBusinessFactory {


    private final FunctionalError functionalError;
    private final ExceptionUtils exceptionUtils;

    public UsersBusinessFactory(FunctionalError functionalError, ExceptionUtils exceptionUtils) {
        this.functionalError = functionalError;
        this.exceptionUtils = exceptionUtils;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<UsersDTO> login(UsersBusiness usersBusiness, Request<UsersDTO> request, FunctionalityEnum functionalityEnum, Locale locale) {
        Response<UsersDTO> response = new Response<UsersDTO>();
        try {
            response = usersBusiness.login(request, locale);
        } catch (PermissionDeniedDataAccessException e) {
            exceptionUtils.PERMISSION_DENIED_DATA_ACCESS_EXCEPTION(response, locale, e);
        } catch (DataAccessResourceFailureException e) {
            exceptionUtils.DATA_ACCESS_RESOURCE_FAILURE_EXCEPTION(response, locale, e);
        } catch (DataAccessException e) {
            exceptionUtils.DATA_ACCESS_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        } finally {
            if (response.isHasError() && response.getStatus() != null) {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
                throw new RuntimeException(response.getStatus().getCode() + ";" + response.getStatus().getMessage());
            }
        }
        return response;
    }
}
