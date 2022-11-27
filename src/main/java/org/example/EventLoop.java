package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;


/**
     *отправка уведомлений телеграм
     */

public class EventLoop extends TelegramLongPollingBot {
    Database db = new Database();
    BotLogic bl = new BotLogic(db);
    public String BotToken = System.getenv("BOT_TOKEN");
    public EventLoop() throws ClassNotFoundException, SQLException {
        while (true) {
            ArrayList<Long> users = db.getAllIntervals();
            for (Long user : users) {
                sendDiscounts(user);
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
    public void onUpdateReceived(Update update) {
        // TODO Auto-generated method stub
        
    }

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
    private void sendDiscounts(long chatId) throws ClassNotFoundException, SQLException{
        ArrayList<String> products;
        products = db.giveDiscounts();
        if (products.contains("нету ничего")) {
            
            sendMessage(chatId, "Извините, в данное время нет скидок");
        } else {
            for (int i = 0; i < products.size() - 1; i += 2) {
                sendMessage(chatId, "Сегодня продается " + products.get(i) + " с " + products.get(i+1) + "% скидкой!");
            }
        }
    }
}
