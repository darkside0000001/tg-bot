package org.example.Notifier;

public class ConsoleNotice implements SendNotice {
    public String sendMessage(Long chatId, String text) {
        System.out.println(text);
        return text;
    }
}
