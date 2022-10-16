import org.example.BotLogic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    BotLogic lBot = new BotLogic();
    @Test
    public void testSee() {
        assertEquals("Модели какого товара хотите посмотреть?", lBot.parseMessage("/seeModels"));
    }
    @Test
    public void testStart() {
        assertEquals("Приветствую в нашем магазине. Наберите /help для просмотра списка команд", lBot.parseMessage("/start"));
    }
    @Test
    public void testHelp() {
        assertEquals("/get - вывод списка товаров, /seeModels - вывод моделей товаров", lBot.parseMessage("/help"));
    }

}