package org.example;

public class BotLogic {
    public String parseMessage(String textMsg) {
        String response;
        if (textMsg.equals("/help"))
            response = "Бот может копировать сообщения";
        else {
            response = textMsg;
        }
        return response;
    }
}
