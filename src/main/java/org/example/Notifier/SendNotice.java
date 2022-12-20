package org.example.Notifier;

/**
 *Класс, реализующий отправку сообщений
 */
public interface SendNotice {
    /**
     *Метод, реализующий отправку сообщений
     */
    public void sendMessage(Long chatId, String text);
}
