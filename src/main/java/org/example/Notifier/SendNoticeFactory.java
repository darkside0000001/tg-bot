package org.example.Notifier;

public class SendNoticeFactory {
    public SendNoticeFactory() {}

    public SendNotice create(NoticeType type) {
        if (type == NoticeType.CONSOLE) {
            return new ConsoleNotice();
        }
        if (type == NoticeType.TELEGRAM) {
            return new TelegramNotice();
        }
        if (type == NoticeType.TEST) {
            return new TestNotice();
        }
        return null;
    }
}
