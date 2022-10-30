package org.example;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

<<<<<<< HEAD
public class Main { 
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
=======
public class Main {
    public static void main(String args[]) {
        BotLogic l = new BotLogic();
>>>>>>> afd8b0081886054ef8c111f978ef016827e7fd23
        Scanner in = new Scanner(System.in);
        System.out.println("Введите Telegram для запуска бота в телеграме или Console для запуска бота в консоли");
        String modeSelection = in.nextLine();
        if (modeSelection.equals("Telegram") || modeSelection.equals("1")) {
             try {
                 TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                 telegramBotsApi.registerBot(new TelegramBot());
             } catch (TelegramApiException e) {
                 e.printStackTrace();
             }
        } else if (modeSelection.equals("Console") || modeSelection.equals("2")) {
             ConsoleBot bot = new ConsoleBot();
        }
        System.out.println(l.parseMessage("/get"));
    }
}
