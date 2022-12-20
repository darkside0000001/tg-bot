package org.example.Notifier;

public class ConsoleNotice implements SendNotice {
    public void sendMessage(Long chatId, String text) {
        System.out.println(text);
    }
}
