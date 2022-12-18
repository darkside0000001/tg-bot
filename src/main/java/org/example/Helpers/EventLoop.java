package org.example.Helpers;
import org.example.Notifier.NoticeType;
import org.example.Notifier.SendNotice;
import org.example.Notifier.SendNoticeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.*;

/**
 *Класс, реализующий отправку сообщений
 */
public class EventLoop {
    Database db = new Database();
    BotLogic logic = new BotLogic(db);
    SendNotice notifier = null;

    /**
     *Реализация отправки сообщений о скидках пользователю
     */
    public EventLoop(NoticeType type) throws Exception {
        notifier = new SendNoticeFactory().create(type);
        while (true) {
            List<Long> users = db.getAllIUsers();
            for (Long user : users) {
                notifier.sendMessage(user, (String) logic.parseMessage("Посмотреть скидки", 0, "tele", "").get(0));

            }
            try {
                sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}


