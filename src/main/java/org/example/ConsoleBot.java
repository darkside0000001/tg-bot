package org.example;
import java.util.Scanner;

public class ConsoleBot {
    BotLogic bot;

    ConsoleBot() {
        bot = new BotLogic();
        Scanner in = new Scanner(System.in);
        while (true) {
            String line = in.nextLine();
            System.out.println(bot.parseMessage(line));
        }
    }
}

