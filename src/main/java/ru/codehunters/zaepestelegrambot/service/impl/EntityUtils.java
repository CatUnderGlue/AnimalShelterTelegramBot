package ru.codehunters.zaepestelegrambot.service.impl;

import java.lang.reflect.Field;

public class EntityUtils {
    /**
     * Метод для копирования из одного объекта в другой
     * игнорируя поля в которых есть null
     *
     * @param fromObj объект от которого копируем
     * @param toObj   объект в который копируем
     */
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

    /**Проверка объекта на наличие в полях null
     *
     * @param obj объект изучения
     * @return true или false
     */
    public static boolean checkForObjectNullEmptyOrWhitespace(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty() || str.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
