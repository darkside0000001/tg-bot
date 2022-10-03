import org.example.Bot;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Tests {
    Bot bot = new Bot();
    @Test
    public void getToken() {
        Assert.assertEquals(System.getenv("BOT_TOKEN"), bot.getBotToken());
    }
    @Test
    public void getName() {
        Assert.assertEquals(System.getenv("BOT_NAME"), bot.getBotUsername());
    }
    @Test
    public void testHelp() {
        Assert.assertEquals("Бот может копировать сообщения", bot.parseMessage("/help"));
    }
    @Test
    public void testCopy() {
        Assert.assertEquals("Привет мир", bot.parseMessage("Привет мир"));
    }
}