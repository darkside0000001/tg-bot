package org.example;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;

public class ConsoleBot {

<<<<<<< HEAD
    BotLogic blogic = new BotLogic();
    Scanner in = new Scanner(System.in);

    public ConsoleBot() throws SQLException, ClassNotFoundException {
        System.out.println("Приветствую в нашем магазине. Наберите '/help' для просмотра списка команд");
=======
    public ConsoleBot() {
        bot = new BotLogic();
        System.out.println("Приветствую в нашем магазине. Наберите /help для просмотра списка команд");
        Scanner in = new Scanner(System.in);
>>>>>>> afd8b0081886054ef8c111f978ef016827e7fd23
        while (true) {
            String line = in.nextLine();
            List<Object> Answer = blogic.parseMessage(line, 0, "cons");
            if ((Integer)Answer.get(2) == 0 || (Integer)Answer.get(2) == 1) {
                sendMessage((String)Answer.get(0));
            } else if ((Integer)Answer.get(2) == 3) {
                send_question_type();
            } else if ((Integer)Answer.get(2) == 4) {
                sendModelsProduct((String)Answer.get(0));
            }
        }
    }

    public void sendMessage(String textToSend) {
        System.out.println(textToSend);
    }

    private void send_question_type() throws SQLException, ClassNotFoundException {
        System.out.println("Что вы хотите посмотреть? (smartphone, notebook)");
        Globals.type = in.nextLine();
        System.out.println("Цель (gaming, study, work)");
        Globals.obj = in.nextLine();
        System.out.println("Цена (20000, 30000, 40000, 50000)");
        Globals.price = in.nextLine();
        Database.Conn();
        ArrayList<String> device = Database.ReadDB(Globals.type, Globals.obj, Globals.price);
        if (device.contains("Нет таких товаров")) {
            System.out.println("Извините, в данное время нет таких товаров");
        } else {
            for (int i = 0; i < device.size(); i++) {
                System.out.println("Вам подойдет " + device.get(i));
            }
        }
    }

    public void sendModelsProduct(String arg) throws SQLException, ClassNotFoundException {
        Database.Conn();
        ArrayList<String> models = Database.get_Models(arg);
        if (models.contains("Нет таких товаров")) {
            System.out.println("Извините, в данное время нет таких товаров");
        } else {
            for (int i = 0; i < models.size(); i++) {
                System.out.println(models.get(i));
            }
        }
    }

    public void parseMessage(String textMsg, long chatId) {
        if (textMsg.equals("/start"))
            System.out.println("Приветствую в нашем магазине. Наберите /help для просмотра списка команд");

        else if (textMsg.equals("/seeModels")) {
            System.out.println("Модели какого товара хотите посмотреть?");
        }
        else if (textMsg.equals("/help")) {
            System.out.println("/get - вывод списка товаров, /seeModels - вывод моделей товаров");
        }
        else {
            System.out.println("Сообщение не распознано");
        }
    }
}


