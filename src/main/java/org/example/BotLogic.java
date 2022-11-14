package org.example;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.*;

/**
 *Класс для реализации логики бота
 */
public class BotLogic {
    Database db;
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

    public BotLogic(Database db) {
        this.db = db;
    }

    /**
     *Метод для выбора ценавого диапозона
     */
    public List<Object> priceForm(String start, String finish, long chatId) throws Exception {
        Globals global = Users.getUserGlobals(chatId);
        global.priceFrom = start;
        global.priceTo = finish;

        String answer = parseDB(chatId, true);

        return listAppend(answer, chatId, 6);
    }

    /**
     *Метод для формирования ответа при подборе товара
     */
    public String parseDB(long chatId, boolean addFilter) throws Exception {
        List<Object> products;
        products = db.giveDB(chatId, addFilter);

        StringBuilder answer = new StringBuilder();

        if (products.size() == 0) {
            return "Извините, в данное время нет таких товаров";
        } else {
            for (Object product : products) {
                answer.append("Вам подойдет ").append(product).append("\n");
            }
            return answer.toString();
        }
    }

    /**
     *Метод для формирования ответа при просмотре корзины
     */
    public String parseCart(long chatId) throws Exception {
        List<Object> products;
        products = db.giveCart(chatId);

        StringBuilder answer = new StringBuilder();

        if (products.size() == 0) {
            return "Ваша корзина пуста";
        } else {
            for (Object product : products) {
                answer.append("У вас в корзине ").append(product).append("\n");
            }
            return answer.toString();
        }
    }

    /**
     *Метод, который отвечает за обработку сообщений от пользователя
     */
    public List<Object> parseMessage(String textMsg, long chatId, String type_bot) throws Exception {
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
                return priceForm("0", "20000", chatId);
            case "от 20000 до 40000":
                return priceForm("20000","40000", chatId);
            case "от 40000 до 60000":
                return priceForm("40000","60000", chatId);
            case "больше 60000":
                priceForm("60000","100000000", chatId);
                return db.giveDB(chatId, true);
            case "Корзина":
                return listAppend("Корзина", chatId, 7);
            case "Добавить":
                db.addToCart(chatId);
                return listAppend("Товар успешно добавлен в корзину!", chatId, 0);
            case "Не добавлять":
                return listAppend("Не добавлять", chatId, 0);
            case "Посмотреть корзину":
                return listAppend(parseCart(chatId), chatId, 0);
            case "Очистить корзину":
                db.cleanCart(chatId);
                return listAppend("Корзина очищена", chatId, 0);
            case "Мои фильтры":
                return listAppend("Мои фильтры", chatId, 12);
            default:
                return listAppend(text_map.get("ERROR_TEXT"), chatId, 0);
        }
    }

    /**
     *Метод для формирования сообщения
     */
    public List<Object> listAppend(String text, Long chatId, Integer actionType) {
        List<Object> params = Arrays.asList(text, chatId, actionType);

        return params;
    }
}