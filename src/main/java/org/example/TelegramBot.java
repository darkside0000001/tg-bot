package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Scanner;

public class TelegramBot extends TelegramLongPollingBot {
    BotLogic bot;
    TelegramBot() {
        bot = new BotLogic();
    }
    private String BotToken = System.getenv("BOT_TOKEN");
    private String BotName = System.getenv("BOT_NAME");
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
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message Mess = update.getMessage();
                String chatId = Mess.getChatId().toString();
                String response = bot.parseMessage(Mess.getText());
                SendMessage outMess = new SendMessage();
                outMess.setChatId(chatId);
                outMess.setText(response);
                execute(outMess);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}