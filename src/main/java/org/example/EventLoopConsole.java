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
import java.util.Scanner;

/**
 *Класс, реализующий отправку сообщений в консоли
 */
public class EventLoopConsole  {
    Database db = new Database();
    BotLogic bl = new BotLogic(db);
    public EventLoopConsole() throws Exception {
        while (true) {
            ArrayList<Long> users = db.getAllIntervals();
            for (Long user : users) {
                if (user == (long) 0) {
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

    /**
     *Метод, который отправляет товары со скидками
     */
    private void sendDiscounts(long chatId) throws Exception {
        System.out.println(bl.parseMessage("Посмотреть скидки", 0, "cons").get(0));
}
    }
