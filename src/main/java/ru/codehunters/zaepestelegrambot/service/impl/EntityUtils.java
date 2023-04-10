package ru.codehunters.zaepestelegrambot.service.impl;

import java.lang.reflect.Field;

public class EntityUtils {
    public static void copyNonNullFields(Object fromObj, Object toObj) {
        Field[] fields = fromObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(fromObj);
                if (value != null) {
                    field.set(toObj, value);
                }
            } catch (IllegalAccessException e) {
                System.out.println("Что-то пошло не так");
            }
        }
    }
}
