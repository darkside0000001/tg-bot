package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;


import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

public class TelegramBot extends TelegramLongPollingBot {
    BotLogic blogic = new BotLogic();
    public String BotToken = "5519928315:AAG9QuA3vrMe_csK1PYedjCOioSKJsYeyFA";
    public TelegramBot() {
    }

    @Override
    public String getBotUsername() {
        return "MBot";
    }

    @Override
    public String getBotToken() {
        return BotToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            List<Object> Answer = blogic.parseMessage(messageText, chatId, "tele");
            if ((Integer) Answer.get(2) == 0) {
                sendMessage((Long)Answer.get(1), (String)Answer.get(0));
            } else if ((Integer) Answer.get(2) == 1) {
                sendModels((Long)Answer.get(1), (String)Answer.get(0));
            } else if ((Integer) Answer.get(2) == 2) {
                try {
                    sendModelsProduct((Long)Answer.get(1), (String)Answer.get(0));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if ((Integer) Answer.get(2) == 3) {
                send_question_type((Long)Answer.get(1));
            } else if ((Integer) Answer.get(2) == 4) {
                Globals.type = (String)Answer.get(0);
                ask_obj((Long)Answer.get(1));
            } else if ((Integer) Answer.get(2) == 5) {
                Globals.obj = (String)Answer.get(0);
                ask_price((Long)Answer.get(1));
            } else if ((Integer) Answer.get(2) == 6) {
                Globals.price = (String)Answer.get(0);
                try {
                    give_device((Long)Answer.get(1));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

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

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendModelsProduct(long chatId, String arg) throws SQLException, ClassNotFoundException {
        Database.Conn();
        ArrayList<String> models = Database.get_Models(arg);
        if (models.contains("Нет таких товаров")) {
            sendMessage(chatId, "Извините, в данное время нет таких товаров");
        } else {
            for (int i = 0; i < models.size(); i++) {
                sendMessage(chatId, models.get(i));
            }
        }
    }

    private void send_question_type(long chatId) {
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

    private void ask_obj(long chatId){
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

    private void ask_price(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Укажите цену которую вы готовы заплатить");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("20000");
        row.add("30000");
        keyboardRows.add(row);
                
        row = new KeyboardRow();
        row.add("40000");
        row.add("50000");

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
    private void give_device(long chatId) throws ClassNotFoundException, SQLException{
        Database.Conn();
        ArrayList<String> device = Database.ReadDB(Globals.type, Globals.obj, Globals.price);
        if(device.contains("Нет таких товаров")){
            sendMessage(chatId, "Извините, в данное время нет таких товаров");
        }
        else{
            for (int i = 0; i < device.size(); i++) {
                sendMessage(chatId, "Вам подойдет " + device.get(i));
            }
        }
    }

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
}