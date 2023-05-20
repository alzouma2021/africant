package com.africanb.africanb.helper.validation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Validate {

    private String field;
    private boolean good;
    private static Validate validate = new Validate();
    private static Logger slf4jLogger = LoggerFactory.getLogger(Validate.class);

    public static Validate getValidate() {
        return validate;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public static void setValidate(Validate validate) {
        Validate.validate = validate;
    }

    public static Logger getSlf4jLogger() {
        return slf4jLogger;
    }

    public static void setSlf4jLogger(Logger slf4jLogger) {
        Validate.slf4jLogger = slf4jLogger;
    }

    public static Validate RequiredValue(Map<String, Object> listAverifier) {
        for (Map.Entry<String, Object> item : listAverifier.entrySet()) {
            if (item.getValue() instanceof List<?>) {
                validate.setGood(item.getValue() != null && ((List<?>) item.getValue()).stream().anyMatch(v -> v != null && !v.toString().isEmpty()));
            } else {
                validate.setGood(item.getValue() != null && !item.getValue().toString().isEmpty());
            }
            if (!validate.isGood()) {
                validate.setField(item.getKey());
                break;
            }
        }
        return validate;
    }

}
