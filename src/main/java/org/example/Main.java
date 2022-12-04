package org.example;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        Database db = new Database();
        db.ConnectToDB();
        // Database db = new Database();
        // db.addDiscount("Asus Rog 5", 50);
        // db.deleteDiscount("Asus Rog 5");
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
            try {
                ConsoleBot bot = new ConsoleBot();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
