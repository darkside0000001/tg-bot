package org.example.Notifier;

public class TestNotice implements SendNotice{
    @Override
    public String sendMessage(Long chatId, String text) {
        return text;
    }
}
