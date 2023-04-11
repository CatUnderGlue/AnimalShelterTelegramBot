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

    /**
     * Метод для проверки полей объекта на равенство null или пустой строке
     *
     * @param obj Объект для проверки
     */
    public static String findNullOrBlankField(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value == null || (value instanceof String && ((String) value).isBlank())) {
                    return field.getName();
                }
            } catch (IllegalAccessException e) {
                System.out.println("Что-то пошло не так");
            }
        }
        return null;
    }
}
