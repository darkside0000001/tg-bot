package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите Telegram для запуска бота в телеграме или Console для запуска бота в консоли");
        String modeSelection = in.nextLine();
        if (modeSelection.equals("Telegram")) {
            try {
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(new Bot());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (modeSelection.equals("Console")) {
            Bot bot = new Bot();
            bot.Console();
        }
    }
}
