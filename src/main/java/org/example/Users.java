package org.example;

import java.util.HashMap;

/**
 *Класс привязки глобальных переменных к конкретному пользователю
 */
public class Users {
    private static HashMap<Long, Globals> data = new HashMap<Long, Globals>();

    /**
     *Получение значений глобальных переменных
     */
    public static Globals getUserGlobals(Long key) {
        if (!data.containsKey(key)) {
            data.put(key, new Globals());
        }
        return data.get(key);
    }

    /**
     *Установка значений глобальных переменных
     */
    public static void setUserGlobals(Long key, Globals value) {
        data.put(key, value);
    }
}
