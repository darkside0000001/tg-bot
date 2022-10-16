package org.example;
import java.util.Scanner;

public class ConsoleBot {
    BotLogic bot;

    public ConsoleBot() {
        bot = new BotLogic();
        System.out.println("Приветствую в нашем магазине. Наберите /help для просмотра списка команд");
        Scanner in = new Scanner(System.in);
        while (true) {
            String line = in.nextLine();
            System.out.println(bot.parseMessage(line));
        }
    }
}


