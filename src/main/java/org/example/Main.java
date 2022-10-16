package org.example;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        BotLogic l = new BotLogic();
        Scanner in = new Scanner(System.in);
        System.out.println("Введите Telegram для запуска бота в телеграме или Console для запуска бота в консоли");
        String modeSelection = in.nextLine();
        if (modeSelection.equals("Telegram")) {
            try {
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(new TelegramBot());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (modeSelection.equals("Console")) {
            ConsoleBot bot = new ConsoleBot();
        }
        System.out.println(l.parseMessage("/get"));
    }
}
