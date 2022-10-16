package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TelegramBot extends TelegramLongPollingBot {
    private String BotToken = "5519928315:AAG9QuA3vrMe_csK1PYedjCOioSKJsYeyFA";
    private String BotName = "Mbot";
    public TelegramBot() {
    }
    String HELP_TEXT = "Это онлайн магазин в котором есть опции простотра товаров и подборки товаров под себя";
    @Override
    public String getBotUsername() {
        return BotName;
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

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId);
                    break;

                case "Помощь":
                    sendSomething(chatId, HELP_TEXT);
                    break;

                case "Список товаров":
                    sendList(chatId);
                    break;

                case "Модели товаров":
                    sendModels(chatId);
                    break;
                case "Смартфоны":
                    sendModelsProduct(chatId, "Смартфоны");
                    break;
                case "Ноутбуки":
                    sendModelsProduct(chatId, "Ноутбуки");
                    break;

                default:
                    sendSomething(chatId, "Sorry, command was not recognized");

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
    public void sendModelsProduct(long chatId, String arg) {
        Data d = new Data();
        for (Object s: (ArrayList) d.products().get(arg)) {
            sendMessage(chatId, (String) s);
        }
    }

    private void startCommandReceived(long chatId) {
        String answer = "Приветствую в нашем магазине. Выберите опцию";
        sendMessage(chatId, answer);
    }

    private void sendList(long chatId){
        String answer = "Сегодня у нас в наличии смартфоны и ноутбуки";
        sendMessage(chatId, answer);
    }

    private void sendSomething(long chatId, String text){
        sendMessage(chatId, text);
    }

    private void sendModels(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Какие модели вы хотите посмотреть?");

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