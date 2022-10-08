import org.example.Bot;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.junit.jupiter.api.Assertions.*;
public class Tests {
    Bot bot = new Bot();
    @Test
    public void getToken() {
        assertEquals(System.getenv("BOT_TOKEN"), bot.getBotToken());
    }
    @Test
    public void getName() {
        assertEquals(System.getenv("BOT_NAME"), bot.getBotUsername());
    }
    @Test
    public void testHelp() {
        assertEquals("Бот может копировать сообщения", bot.parseMessage("/help"));
    }
    @Test
    public void testCopy() {
        assertEquals("Привет мир", bot.parseMessage("Привет мир"));
    }
}