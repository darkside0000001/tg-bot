package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class BotLogic {
    public String parseMessage(String textMsg) {
        Data data = new Data();
        String response = "";
        if (textMsg.equals("/start"))
            response = "Приветствую в нашем магазине. Наберите /help для просмотра списка команд";
        else if (textMsg.equals("/get")) {
            for (Object key: data.products().keySet()) {
                System.out.print(key + " ");
            }
        }

        else if (textMsg.equals("/seeModels")) {
            response = "Модели какого товара хотите посмотреть?";
        }
        else if (textMsg.equals("Смартфоны") || textMsg.equals("Ноутбуки")) {
            for (Object model: (ArrayList) data.products().get(textMsg)) {
                System.out.print(model + " ");
            }
        }
        else if (textMsg.equals("/help")) {
            response = "/get - вывод списка товаров, /seeModels - вывод моделей товаров";
        }
        else {
            response = "Сообщение не распознано";
        }
        return response;
    }
}
