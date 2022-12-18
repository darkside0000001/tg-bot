package org.example.Notifier;

/**
 *Класс, реализующий отправку сообщений
 */
public interface SendNotice {
    /**
     *Метод, реализующий отправку сообщений
     */
    public String sendMessage(Long chatId, String text);
}
