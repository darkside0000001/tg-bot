package org.example.Notifier;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramNotice extends TelegramLongPollingBot implements SendNotice {
    public String botToken = System.getenv("BOT_TOKEN");

    
    @Override
    public void onUpdateReceived(Update update) {
        // TODO Auto-generated method stub
    }
    @Override
    public String getBotUsername() {
        // TODO Auto-generated method stub
        return "TestBot";
    }
    @Override
    public String getBotToken() {
        // TODO Auto-generated method stub
        return botToken;
    }
    
    TelegramNotice() { }

    @Override
    public String sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return text;
    }
}