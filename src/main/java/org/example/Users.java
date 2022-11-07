package org.example;

import java.util.HashMap;

public class Users {
    private static HashMap<Long, Globals> data = new HashMap<Long, Globals>();

    public static Globals getUserGlobals(Long key) {
        if (!data.containsKey(key)) {
            data.put(key, new Globals());
        }
        return data.get(key);
    }

    public static void setUserGlobals(Long key, Globals value) {
        data.put(key, value);
    }
}
