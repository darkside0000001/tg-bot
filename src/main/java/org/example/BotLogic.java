package org.example;

import java.sql.SQLException;
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

    // 0 - sendMessage
    // 1 - categories
    // 2 - products
    // 3 - questionType
    // 4 - ask_obj
    // 5 - ask_price
    // 6 - give_device
    public void priceform(String start, String finish, long chatId, int i){
        Globals global = Users.getUserGlobals(chatId);
        global.priceFrom = start;
        global.priceTo = finish;
        //return listAppend("funny text", chatId, i);
    }


    public List<Object> parseMessage(String textMsg, long chatId, String type_bot) throws SQLException, ClassNotFoundException {
        String txt;
        Globals global = Users.getUserGlobals(chatId);

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
                global.type = "smartphone";
                return listAppend("smartphone", chatId, 4);
            case "ноутбуки":
            case "/notebook":
                global.type = "notebook";
                return listAppend("notebook", chatId, 4);
            case "Игры":
                global.obj = "gaming";
                return listAppend("gaming", chatId, 5);
            case "Работа":
                global.obj = "work";
                return listAppend("work", chatId, 5);
            case "Учеба":
                global.obj = "study";
                return listAppend("study", chatId, 5);
            case "меньше 20000":
                priceform("0", "20000", chatId, 6);
                return giveDB(chatId, true);
            case "от 20000 до 40000":
                priceform("20000","40000", chatId, 6);
                return giveDB(chatId, true);
            case "от 40000 до 60000":
                priceform("40000","60000", chatId, 6);
                return giveDB(chatId, true);
            case "больше 60000":
                priceform("60000","100000000", chatId, 6);
                return giveDB(chatId, true);
            case "Корзина":
                return listAppend("Корзина", chatId, 7);
            case "Добавить":
                addToCart(chatId);
                return listAppend("Товар успешно добавлен в корзину!", chatId, 0);
            case "Не добавлять":
                return listAppend("Не добавлять", chatId, 0);
            case "Посмотреть корзину":
                return giveCart(chatId);
            case "Очистить корзину":
                return deleteCart(chatId);
            case "Мои фильтры":
                return listAppend("Мои фильтры", chatId, 12);
            default:
                return listAppend(text_map.get("ERROR_TEXT"), chatId, 0);
        }
    }

    public List<Object> listAppend(String text, Long chatId, Integer actionType) {
        List<Object> params = Arrays.asList(text, chatId, actionType);

        return params;
    }

    public List<Object> giveCart(long chatId) throws ClassNotFoundException, SQLException{
        Database.Conn();
        ArrayList<String> device = Database.getCart(chatId);
        if(device.contains("пусто")){
            return listAppend("Ваша корзина пуста ", chatId, 0);
        }
        else{
            for (int i = 0; i < device.size(); i++) {
                return listAppend("У вас в корзине " + device.get(i), chatId, 0);
            }
        }

        return listAppend("Произошла какая-то ошибка", chatId, 0);
    }

    public List<Object> giveDB(long chatId, boolean addFilter) throws SQLException, ClassNotFoundException {
        Database.Conn();
        Globals global = Users.getUserGlobals(chatId);
        ArrayList<String> device = Database.ReadDB(global.type, global.obj, Integer.parseInt(global.priceFrom), Integer.parseInt(global.priceTo));
        if (addFilter) {
            Database.addFilter(chatId, global.type, global.obj, Integer.parseInt(global.priceFrom), Integer.parseInt(global.priceTo));
        }
        ArrayList<String> aa = new ArrayList<String>();
        if(device.contains("Нет таких товаров")){
            aa.add("Нету");
            return listAppend("Извините, в данное время нет таких товаров", chatId, 0);
        }
        else{
            for (int i = 0; i < device.size(); i++) {
                aa.add(device.get(i));
                global.cart = aa;
                return listAppend("Вам подойдет " + device.get(i), chatId, 6);
            }
        }

        return listAppend("Произошла какая-то ошибка", chatId, 0);
    }

    void addToCart(long chatId) throws ClassNotFoundException, SQLException{
        Database.Conn();
        Globals global = Users.getUserGlobals(chatId);
        ArrayList<String> products = global.cart;
        Database.addCart(chatId, products.get(0));
    }

    public List<Object> deleteCart(long chatId) throws SQLException, ClassNotFoundException{
        Database.Conn();
        Database.deleteCart(chatId);
        return listAppend("Корзина очищена", chatId, 0);
    }
}