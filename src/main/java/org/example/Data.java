package org.example;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    public HashMap products() {
        HashMap<String, ArrayList> data = new HashMap();
        ArrayList<String> laptops = new ArrayList();
        ArrayList<String> phones = new ArrayList<>();
        laptops.add("MacBook Pro 14");
        laptops.add("Dell XPS 13");
        phones.add("iPhone 13");
        phones.add("Samsung Galaxy S20");
        data.put("Ноутбуки", laptops);
        data.put("Смартфоны", phones);
        return data;
    }
}
