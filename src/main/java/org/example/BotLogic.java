package org.example;

import java.util.*;

public class BotLogic {
    Map<String, String> text_map = Map.of("HELP_TEXT", "Это онлайн магазин в котором есть опции простотра товаров и подборки товаров под себя",
            "START_TEXT", "Приветствую в нашем магазине. Выберите опцию",
            "LIST_OF_PRODUCT_TEXT", "Сегодня у нас в наличии смартфоны и ноутбуки",
            "MODELS_TEXT", "Модели каких товаров хотите посмотреть?",
            "ERROR_TEXT", "Извините, команда не существует",
            "START_TEXT_cons", "Приветствую в нашем магазине. Наберите /help для просмотра списка команд",
            "HELP_TEXT_cons", "/get - вывод списка товаров, /seeModels - вывод моделей товаров, /pick - подобрать",
            "MODELS_TEXT_cons", "Какие товары хотите посмотреть? /smartphone и /notebooks");

    /*
    *0 - sendMessage
    *1 - categories
    *2 - products
    *3 - questionType
    *4 - ask_obj
    *5 - ask_price
    *6 - give_device
     */
    public List<Object> parseMessage(String textMsg, long chatId, String type_bot) {
        String txt;

        switch (textMsg) {
            case "Старт":
            case "/start":
                txt = text_map.get("START_TEXT");
                if (type_bot.equals("cons")) {
                    txt = text_map.get("START_TEXT_cons");
                }
                return listAppend(txt, chatId, 0);
            case "Помощь":
            case "/help":
                txt = text_map.get("HELP_TEXT");
                if (type_bot.equals("cons")) {
                    txt = text_map.get("HELP_TEXT_cons");
                }
                return listAppend(txt, chatId, 0);
            case "Список товаров":
            case "/get":
                return listAppend(text_map.get("LIST_OF_PRODUCT_TEXT"), chatId, 0);
            case "Модели товаров":
            case "/seeModels":
                txt = text_map.get("MODELS_TEXT");
                if (type_bot.equals("cons")) {
                    txt = text_map.get("MODELS_TEXT_cons");
                }
                return listAppend(txt, chatId, 1);
            case "Смартфоны":
                return listAppend("smartphone", chatId, 2);
            case "Ноутбуки":
                return listAppend("notebook", chatId, 2);
            case "Подобрать товар":
            case "/pick":
                return listAppend("Что вас интересует?", chatId, 3);
            case "смартфоны":
            case "/smartphone":
                return listAppend("smartphone", chatId, 4);
            case "ноутбуки":
            case "/notebook":
                return listAppend("notebook", chatId, 4);
            case "Игры":
                return listAppend("gaming", chatId, 5);
            case "Работа":
                return listAppend("work", chatId, 5);
            case "Учеба":
                return listAppend("study", chatId, 5);
            case "20000":
                return listAppend("20000", chatId, 6);
            case "30000":
                return listAppend("30000", chatId, 6);
            case "40000":
                return listAppend("40000", chatId, 6);
            case "50000":
                return listAppend("50000", chatId, 6);
            default:
                return listAppend(text_map.get("ERROR_TEXT"), chatId, 0);
        }
    }

    public List<Object> listAppend(String text, Long chatId, Integer actionType) {
        List<Object> params = Arrays.asList(text, chatId, actionType);

        return params;
    }
}