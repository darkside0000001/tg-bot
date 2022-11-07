package org.example;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main { 
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        /*try {
            Database.Conn();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
                e.printStackTrace();
        }*/
        Scanner in = new Scanner(System.in);
        System.out.println("Введите Telegram для запуска бота в телеграме или Console для запуска бота в консоли");
        String modeSelection = in.nextLine();
        if (modeSelection.equals("Telegram") || modeSelection.equals("1")) {
            try {
                Database.Conn();
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(new TelegramBot());
            } catch (TelegramApiException e) {
                  e.printStackTrace();
            }
        } else if (modeSelection.equals("Console") || modeSelection.equals("2")) {
              ConsoleBot bot = new ConsoleBot();
        }
    }
}
