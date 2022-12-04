package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.List;

import static java.lang.Thread.*;

/**
 *Класс, реализующий отправку сообщений
 */
public class EventLoop extends TelegramLongPollingBot {
    Database db = new Database();
    BotLogic bl = new BotLogic(db);
    public String BotToken = System.getenv("BOT_TOKEN");

    /**
     *Реализация отправки сообщений о скидках пользователю
     */
    public EventLoop(String type) throws Exception {
        while (true) {
            List<Long> users = db.getAllIUsers();
            for (Long user : users) {
                if (user != 0L && type.equals("tele")) {
                    sendMessage(user, (String) bl.parseMessage("Посмотреть скидки", 0, "tele").get(0));
                    //sendDiscounts(user);
                }
                else if (user == 0L && type.equals("cons")) {
                    System.out.println(bl.parseMessage("Посмотреть скидки", 0, "cons").get(0));
                }
            }
            try {
                sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *Получение имени бота
     */
    @Override
    public String getBotUsername() {
        return "MBot";
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
        // TODO Auto-generated method stub

    }

    /**
     *Метод, который реализует отправку сообщений пользователю в телеграме
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

}


