package org.example.Helpers;

import java.sql.SQLException;
import java.util.*;

/**
 *Класс для реализации логики бота
 */
public class BotLogic {
    private HashMap<Long, User> userData = new HashMap<>();
    Database db;
    Map<String, String> textMap = Map.of("HELP_TEXT", "Это онлайн магазин в котором есть опции простотра товаров и подборки товаров под себя",
            "START_TEXT", "Приветствую в нашем магазине. Выберите опцию",
            "LIST_OF_PRODUCT_TEXT", "Сегодня у нас в наличии смартфоны и ноутбуки",
            "MODELS_TEXT", "Модели каких товаров хотите посмотреть?",
            "ERROR_TEXT", "Извините, команда не существует",
            "START_TEXT_cons", "Приветствую в нашем магазине. Наберите /help для просмотра списка команд",
            "HELP_TEXT_cons", "/get - вывод списка товаров, /seeModels - вывод моделей товаров, /pick - подобрать, Мои фильтры - просмотр фильтров, Корзина - меню корзины, Скидки - меню скидок, Отзывы - меню отзывов",
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
        User global = getUserGlobals(chatId);
        global.priceFrom = start;
        global.priceTo = finish;

        String answer = parseDB(chatId, false);

        return listAppend(answer, chatId, 6);
    }

    /**
     *Метод для формирования ответа при подборе товара
     */
    public String parseDB(long chatId, boolean addFilter) throws Exception {
        List<Object> products;
        User userData = getUserGlobals(chatId);
        products = db.giveDB(chatId, addFilter, userData);

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
        List<String> products;
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
     *Метод, который реализует добавление товара в корзину по id пользователя
     */
    public void addToCart(long chatId) throws ClassNotFoundException, SQLException{
        User global = getUserGlobals(chatId);
        List<String> products = global.cart;
        db.addCart(chatId, products.get(0));
    }


    /**
     *Метод для формирования ответы при просмотре товаров со скидками
     */
    private String parseDiscounts() throws SQLException{
        List<String> products;
        products = db.giveDiscounts();
        StringBuilder answer = new StringBuilder();
        if (products.contains("нету ничего")) {
            answer.append("Извините, в данное время нет скидок");
        } else {
            for (int i = 0; i < products.size() - 1; i += 2) {
                answer.append("Сегодня продается ").append(products.get(i)).append(" с ").append(products.get(i + 1)).append("% скидкой!").append("\n");
            }
        }
        return answer.toString();
    }

    /**
     *Метод, который реализует очистку корзины
     */
    public void cleanCart(long chatId) throws SQLException, ClassNotFoundException {
        db.deleteCart(chatId);
    }

    private String parseReviews(String name) {
        try {
            return db.ShowReviews(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *Метод, который отвечает за обработку сообщений от пользователя
     */
    public List<Object> parseMessage(String textMsg, long chatId, String typeBot, String name) throws Exception {
        String txt;
        User global = getUserGlobals(chatId);
        if(global.need_review == 1){
            global.review = textMsg;
            db.AddProductReview(global.product_to_review, textMsg);
            global.need_review = 0;
            return listAppend("Отзыв успешно добавлен", chatId, 0);
        }

        switch (textMsg) {
            case "Старт":
            case "/start":
                txt = textMap.get("START_TEXT");
                if (typeBot.equals("cons")) {
                    txt = textMap.get("START_TEXT_cons");
                }
                return listAppend(txt, chatId, 0);
            case "Помощь":
            case "/help":
                txt = textMap.get("HELP_TEXT");
                if (typeBot.equals("cons")) {
                    txt = textMap.get("HELP_TEXT_cons");
                }
                return listAppend(txt, chatId, 0);
            case "Список товаров":
            case "/get":
                return listAppend(textMap.get("LIST_OF_PRODUCT_TEXT"), chatId, 0);
            case "Модели товаров":
            case "/seeModels":
                txt = textMap.get("MODELS_TEXT");
                if (typeBot.equals("cons")) {
                    txt = textMap.get("MODELS_TEXT_cons");
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
                return priceForm("60000","100000000", chatId);
                //return db.giveDB(chatId, true);
            case "Корзина":
                return listAppend("Корзина", chatId, 7);
            case "Добавить":
                addToCart(chatId);
                return listAppend("Товар успешно добавлен в корзину!", chatId, 0);
            case "Не добавлять":
                return listAppend("Не добавлять", chatId, 0);
            case "Посмотреть корзину":
                return listAppend(parseCart(chatId), chatId, 0);
            case "Очистить корзину":
                cleanCart(chatId);
                return listAppend("Корзина очищена", chatId, 0);
            case "Мои фильтры":
                return listAppend("Мои фильтры", chatId, 12);
            case "Скидки":
                return listAppend("Скидки", chatId, 13);
            case "Посмотреть скидки":
                return listAppend(parseDiscounts(), chatId, 14);
            case "Включить уведомления о скидках":
                return listAppend("Включить уведомления о скидках", chatId, 15);
            case "30 секунд":
                db.addInterval(chatId, 30);
                return listAppend("Включены уведомления на 30 секунд", chatId, 0);
            case "1 час":
                db.addInterval(chatId, 3600);
                return listAppend("Включены уведомления на 1 час", chatId, 0);
            case "1 день":
                db.addInterval(chatId, 86400);
                return listAppend("Включены уведомления на 1 день", chatId, 0);
            case "Отписаться":
                db.deleteSubs(chatId);
                return listAppend("Уведомления выключены", chatId, 0);
            case "Отзывы":
                return listAppend("Отзывы", chatId, 16);
            case "Посмотреть отзывы":
                return listAppend(parseReviews(name), chatId, 17);
            case "Написать отзыв":
                return listAppend("Написать отзыв", chatId, 18);
            default:
                return listAppend(textMap.get("ERROR_TEXT"), chatId, 0);
        }
    }

    /**
     *Метод для формирования сообщения
     */
    public List<Object> listAppend(String text, Long chatId, Integer actionType) {
        return List.of(text, chatId, actionType);
    }

    /**
     *Метод, который возвращает,
     *прикрепляенные к пользователю
     *глобальные переменные
     */
    public User getUserGlobals(Long key) {
        return userData.computeIfAbsent(key, aLong -> new User());
    }

    /**
     *Метод, который прикрепляет к пользователю глобальные переменные
     */
    public void setUserGlobals(Long key, User value) {
        userData.put(key, value);
    }
}