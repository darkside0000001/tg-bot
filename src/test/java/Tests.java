import org.example.BotLogic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    BotLogic bot = new BotLogic();
    @Test
    public void testHelp() {
        assertEquals("Бот может копировать сообщения", bot.parseMessage("/help"));
    }
    @Test
    public void testCopy() {
        assertEquals("Привет мир", bot.parseMessage("Привет мир"));
    }
}