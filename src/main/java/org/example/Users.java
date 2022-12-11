package org.example;

import java.util.HashMap;

/**
 *Класс привязки глобальных переменных к конкретному пользователю
 */
public class Users {
    public static HashMap<Long, Globals> data = new HashMap<>();

    /**
     *Метод, который возвращает,
     *прикрепляенные к пользователю
     *глобальные переменные
     */
    public static Globals getUserGlobals(Long key) {
        if (!data.containsKey(key)) {
            data.put(key, new Globals());
        }
        return data.get(key);
    }

    /**
     *Метод, который прикрепляет к пользователю глобальные переменные
     */
    public static void setUserGlobals(Long key, Globals value) {
        data.put(key, value);
    }
}