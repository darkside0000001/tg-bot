package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

/**
 *Класс, реализующий отправку сообщений в телеграме
 */
public class EventLoopTelegram extends TelegramLongPollingBot {
    Database db = new Database();
    BotLogic bl = new BotLogic(db);
    public String BotToken = System.getenv("BOT_TOKEN");
    public EventLoopTelegram() throws Exception {
        while (true) {
            ArrayList<Long> users = db.getAllIntervals();
            for (Long user : users) {
                if (user != (long) 0) {
                    sendDiscounts(user);
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    /**
     *Получение имени бота
     */
    public String getBotUsername() {
        return "MBot";
    }

    @Override
    /**
     *Получение токена бота
     */
    public String getBotToken() {
        return BotToken;
    }

    @Override
    /**
     *Метод, который реализует получение и отправку сообщений пользователю
     */
    public void onUpdateReceived(Update update) {
        // TODO Auto-generated method stub
        
    }

    /**
     *Метод, который реализует отправку сообщений пользователю
     */
    private void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *Метод, который отправляет товары со скидками
     */
    private void sendDiscounts(long chatId) throws Exception {
        sendMessage(chatId, (String) bl.parseMessage("Посмотреть скидки", 0, "tele").get(0));
    }
}

