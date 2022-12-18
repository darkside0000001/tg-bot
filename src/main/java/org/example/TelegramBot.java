package org.example;
import org.example.Helpers.BotLogic;
import org.example.Helpers.Database;
import org.example.Helpers.EventLoop;
import org.example.Helpers.User;
import org.example.Notifier.NoticeType;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;


import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *Класс для реализации бота в телеграме
 */
public class TelegramBot extends TelegramLongPollingBot {
    Database db = new Database();
    BotLogic logic = new BotLogic(db);
    public String BotToken = System.getenv("BOT_TOKEN");

    public TelegramBot() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            try {
                new EventLoop(NoticeType.TELEGRAM);
            } catch (ClassNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    /**
     *Получение имени бота
     */
    @Override
    public String getBotUsername() {
        return "TestBot";
    }

    /**
     *Получение токена бота
     */
    @Override
    public String getBotToken() {
        return BotToken;
    }


    /**
     *Метод, который реализует получение и отправку сообщений пользователю
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            User userData = logic.getUserGlobals(chatId);


            List<Object> Answer = null;
            try {
                Answer = logic.parseMessage(messageText, chatId, "tele", "");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if ((Integer) Answer.get(2) == 0) {
                sendMessage(chatId, (String)Answer.get(0));
            } else if ((Integer) Answer.get(2) == 1) {
                sendModels(chatId, (String)Answer.get(0));
            } else if ((Integer) Answer.get(2) == 2) {
                try {
                    sendModelsProduct(chatId, (String)Answer.get(0));
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if ((Integer) Answer.get(2) == 3) {
                sendQuestionType(chatId);
            } else if ((Integer) Answer.get(2) == 4) {
                userData.type = (String)Answer.get(0);
                askObj(chatId);
            } else if ((Integer) Answer.get(2) == 5) {
                userData.obj = (String)Answer.get(0);
                askPrice(chatId);
            } else if ((Integer) Answer.get(2) == 6) {
                try {
                    String answer = logic.parseDB(chatId, true);
                    sendMessage(chatId, answer);
                    if (!Objects.equals(answer, "Извините, в данное время нет таких товаров")) {
                        sendCart(chatId);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if((Integer) Answer.get(2) == 7){
                giveCartOptions(chatId);
            } else if((Integer) Answer.get(2) == 8){
                try {
                    //addToCart(chatId);
                    logic.addToCart(chatId);
                } catch (ClassNotFoundException | SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if((Integer) Answer.get(2) == 9){
                sendMessage(chatId, "Окей");
            } 
            else if((Integer) Answer.get(2) == 10){
                try {
                    //giveCart(chatId);
                    db.giveCart(chatId);
                } catch (ClassNotFoundException | SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if((Integer) Answer.get(2) == 13){
                sendDiscountsOptions(chatId);
            } else if((Integer) Answer.get(2) == 15){
                sendSubscriptionType(chatId);
            } else if((Integer) Answer.get(2) == 16){
                sendReviewsOptions(chatId);
            } else if((Integer) Answer.get(2) == 17){
                try {
                    List<String> list = db.ShowAllProducts();

                    SendMessage message = new SendMessage();
                    message.setText("Выберите продукт:");
                    message.setChatId(chatId);
        
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    for (String line : list) {
                        List<InlineKeyboardButton> rowInline = new ArrayList<>();

                        InlineKeyboardButton button = new InlineKeyboardButton();
                        button.setText(String.format(line));
                        button.setCallbackData(String.format("fast_products|%s", line));

                        rowInline.add(button);
                        rowsInline.add(rowInline);
                    }
                    markupInline.setKeyboard(rowsInline);
                    message.setReplyMarkup(markupInline);

                    try {
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if((Integer) Answer.get(2) == 18){
                try {
                    List<String> list = db.ShowAllProducts();

                    SendMessage message = new SendMessage();
                    message.setText("Выберите продукт:");
                    message.setChatId(chatId);
        
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    for (String line : list) {
                        List<InlineKeyboardButton> rowInline = new ArrayList<>();

                        InlineKeyboardButton button = new InlineKeyboardButton();
                        button.setText(String.format(line));
                        button.setCallbackData(String.format("add_review|%s", line));

                        rowInline.add(button);
                        rowsInline.add(rowInline);
                    }
                    markupInline.setKeyboard(rowsInline);
                    message.setReplyMarkup(markupInline);

                    try {
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if((Integer) Answer.get(2) == 14){
                //sendDiscounts(chatId);
                try {
                    sendMessage(chatId,(String) logic.parseMessage("Посмотреть скидки", chatId, "cons", "").get(0));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if((Integer) Answer.get(2) == 11){
                try {
                    logic.cleanCart(chatId);
                    //deleteCart(chatId);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }else if((Integer) Answer.get(2) == 12){
                try {
                    List<List<String>> list = db.showLatestFilters(chatId);

                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    if (db.getRowCount(chatId) == 0) {
                        message.setText("Вы не делали запросов");
                    } else {
                        message.setText("Ваши предыдущие фильтры:");
                    }

                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    for (List<String> line : list) {
                        String type = line.get(0);
                        String object = line.get(1);
                        String priceFrom = line.get(2);
                        String priceTo = line.get(3);
                        List<InlineKeyboardButton> rowInline = new ArrayList<>();

                        InlineKeyboardButton button = new InlineKeyboardButton();
                        button.setText(String.format("Тип = %s, Объект = %s, Цена = %s-%s\n", type, object, priceFrom, priceTo));
                        button.setCallbackData(String.format("fast_filtering|%s|%s|%s|%s", type, object, priceFrom, priceTo));

                        rowInline.add(button);
                        rowsInline.add(rowInline);
                    }
                    markupInline.setKeyboard(rowsInline);
                    message.setReplyMarkup(markupInline);

                    try {
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        if (update.hasCallbackQuery()) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String call_data = update.getCallbackQuery().getData();
            String[] tokens = call_data.split("\\|");
            User userData = logic.getUserGlobals(chatId);

            if (tokens[0].equals("fast_filtering")) {
                userData.type = tokens[1];
                userData.obj = tokens[2];
                userData.priceFrom = tokens[3];
                userData.priceTo = tokens[4];
                try {
                    giveDevice(chatId, false);
                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (tokens[0].equals("fast_products")) {
                String line = tokens[1];
                try {
                    sendReviews(chatId, line);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (tokens[0].equals("add_review")) {
                String line = tokens[1];
                AddReview(chatId, line);
            }
        }
    }

    /**
     *Метод, который реализует отправку сообщений пользователю
     */
    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Список товаров");
        row.add("Модели товаров");
        row.add("Подобрать товар");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("Помощь");
        row.add("Корзина");
        row.add("Мои фильтры");

        keyboardRows.add(row);
        row = new KeyboardRow();
        
        row.add("Скидки");
        row.add("Отзывы");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *Метод, который реализует просмотр актуальных моделей товаров
     */
    public void sendModelsProduct(long chatId, String arg) throws SQLException, ClassNotFoundException {
        List<String> models = db.getModels(arg);
        if (models.contains("Нет таких товаров")) {
            sendMessage(chatId, "Извините, в данное время нет таких товаров");
        } else {
            for (String model : models) {
                sendMessage(chatId, model);
            }
        }
    }

    /**
     *Метод, который реализует подбор товаров
     */
    private void sendQuestionType(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Что вас интересует?");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("смартфоны");
        row.add("ноутбуки");

        keyboardRows.add(row);

        row = new KeyboardRow();

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *Метод выбора категории товара
     */
    private void askObj(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Для каких целей?");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Игры");
        row.add("Учеба");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Работа");

        keyboardRows.add(row);

        row = new KeyboardRow();

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *Метод диапазона выбора цены
     */
    private void askPrice(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Укажите цену которую вы готовы заплатить");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("меньше 20000");
        row.add("от 20000 до 40000");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("от 40000 до 60000");
        row.add("больше 60000");

        keyboardRows.add(row);

        row = new KeyboardRow();

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *Вывод моделей товара по фильтру
     */
    private void giveDevice(long chatId, boolean addFilter) throws ClassNotFoundException, SQLException{
        User userData = logic.getUserGlobals(chatId);
        List<String> device = db.ReadDB(userData.type, userData.obj, Integer.parseInt(userData.priceFrom), Integer.parseInt(userData.priceTo));
        if (addFilter) {
            db.addFilter(chatId, userData.type, userData.obj, Integer.parseInt(userData.priceFrom), Integer.parseInt(userData.priceTo));
        }
        ArrayList<String> aa = new ArrayList<>();
        if(device.contains("Нет таких товаров")){
            sendMessage(chatId, "Извините, в данное время нет таких товаров");
            aa.add("Нету");
        }
        else{
            for (String s : device) {
                sendMessage(chatId, "Вам подойдет " + s);
                aa.add(s);
            }
        }
        if(aa.contains("Нету")){

        }
        else{
            userData.cart = aa;
            sendCart(chatId);
        }
    }

    /**
     *Метод, который реализует добавление товара в корзину
     */
    private void sendCart(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Хотите добавить модели в корзину?");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Добавить");
        row.add("Не добавлять");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *Метод, который реализует просмотр товаров
     */
    private void sendModels(long chatId, String text){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Смартфоны");
        row.add("Ноутбуки");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("Помощь");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *Метод, который реализует выбор опций корзины
     */
    private void giveCartOptions(long chatId){
        SendMessage message = new SendMessage();
        String text = "Выберете опцию";
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Посмотреть корзину");
        row.add("Очистить корзину");

        keyboardRows.add(row);

        row = new KeyboardRow();
        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *Метод, который реализует опции скидок
     */
    private void sendDiscountsOptions(long chatId){
        SendMessage message = new SendMessage();
        String text = "Выберете опцию";
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Посмотреть скидки");

        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Включить уведомления о скидках");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Таймеры
     */
    private void sendSubscriptionType(long chatId){
        SendMessage message = new SendMessage();
        String text = "Выберете частоту уведомлений";
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("30 секунд");
        row.add("1 час");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("1 день");
        row.add("Отписаться");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Меню отзывов
     */

    private void sendReviewsOptions(long chatId){
        SendMessage message = new SendMessage();
        String text = "Выберите опцию";
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Посмотреть отзывы");
        row.add("Написать отзыв");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Просмотр отзывов
     */
    private void sendReviews(long chatId, String name) throws SQLException{
        String review = db.ShowReviews(name);
        String[] reviews = review.split("\\, ");
        int iter = 1;
        if(review.equals("На этот продукт еще нет отзывов")){
            sendMessage(chatId, "На этот продукт еще нет отзывов");
            return;
        }
        for(String i : reviews){
            sendMessage(chatId, "Отзыв " + Integer.toString(iter) + ": " + i);
            iter += 1;
        }
    }

    /**
     * Добавление отзыва
     */
    private void AddReview(long chatId, String line){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Напишите отзыв");
        User userData = logic.getUserGlobals(chatId);
        userData.need_review = 1;
        userData.product_to_review = line;
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}