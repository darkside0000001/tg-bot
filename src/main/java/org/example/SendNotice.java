package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *Класс, реализующий отправку сообщений
 */
public class SendNotice extends TelegramLongPollingBot {
    Database db = new Database();
    BotLogic bl = new BotLogic(db);
    public String BotToken = System.getenv("BOT_TOKEN");

    
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
        return BotToken;
    }

    public void sendTelegram(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void SendConsole(Object object){
        System.out.println(object);
    }

}
