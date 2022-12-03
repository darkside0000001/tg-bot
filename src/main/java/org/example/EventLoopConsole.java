package org.example;
import java.util.List;
import static java.lang.Thread.*;

/**
 *Класс, реализующий отправку сообщений в консоли
 */
public class EventLoopConsole  {
    Database db = new Database();
    BotLogic bl = new BotLogic(db);
    public EventLoopConsole() throws Exception {
        while (true) {
            List<Long> users = db.getAllIntervals();
            for (Long user : users) {
                if (user == (long) 0) {
                sendDiscounts(user);
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
     *Метод, который отправляет товары со скидками
     */
    private void sendDiscounts(long chatId) throws Exception {
        System.out.println(bl.parseMessage("Посмотреть скидки", 0, "cons").get(0));
}
    }
