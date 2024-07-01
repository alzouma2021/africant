package com.africanb.africanb.helper.searchFunctions;

import com.africanb.africanb.helper.validation.Validate;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;


public class Utilities {

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    public static boolean isTrue(Boolean b) {
        return b != null && b;
    }

    public static <T> boolean isNotEmpty(List<T> list) {
        return (list != null && !list.isEmpty());
    }

    public static <T> boolean isEmpty(List<T> list) {
        return (list == null || list.isEmpty());
    }

    public static boolean checkForSQLInjection(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if(fields.length == 0) return true;
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(object);
            if (fieldValue instanceof String fieldValueStr) {
                if (containsSQLInjection(fieldValueStr)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean containsSQLInjection(String value) {
        String[] dangerousKeywords = {
                "SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "ALTER", "EXECUTE",
                "UNION", "JOIN", "HAVING", "FROM", "WHERE", "OR", "AND", "LIKE",
                "--", "/*", "*/", ";"
        };
        return Arrays.stream(dangerousKeywords)
                .anyMatch(keyword -> containsWord(value, keyword));
    }

    public static boolean containsWord(String text, String word) {
        String pattern = "\\b" + word + "\\b";
        return text != null && text.matches(".*" + pattern + ".*");
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date convertStringToDate(String dateString) throws ParseException {
        if(dateString==null)  return new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(dateString);
    }

    public static String convertDateToString(Date date) {
        if(date==null)  return null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static Boolean convertStringToBoolean(String str) {
        return Boolean.parseBoolean(str);
    }

    public static Long convertStringToLong(String str){
        return Long.parseLong(str);
    }


    public static boolean isValidID(Long id) {
        return id != null && id > 0;
    }

    public static String getFrenchDayOfWeek(Date date) {
        LocalDate localDate = convertDateToLocalDate(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE", Locale.FRENCH);
        return localDate.format(formatter);
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @SafeVarargs
    private boolean validateFields(Object object, Function<Object, Object>... fieldExtractors) {
        Map<String, Object> fieldsToVerify = new HashMap<>();
        for (Function<Object, Object> fieldExtractor : fieldExtractors) {
            String fieldName = fieldExtractor.apply(object).toString();
            fieldsToVerify.put(fieldName, fieldExtractor.apply(object));
        }
        return Validate.RequiredValue(fieldsToVerify).isGood();
    }

}