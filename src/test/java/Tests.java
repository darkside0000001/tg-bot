import org.example.BotLogic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


//import org.example.BotLogic;
//import org.example.ConsoleBot;
import org.example.TelegramBot;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    //TelegramBot tgBot = new TelegramBot();
    //ConsoleBot csBot = new ConsoleBot();
    BotLogic lBot = new BotLogic();
    //@Test
    //public void testButtonProductList() {
    //assertEquals("iPhone 13 \n Samsung Galaxy S20", lBot.parseMessage("Смартфоны"));
    //Update update = new Update();

    //assertEquals("Список товаров", tgBot.onUpdateReceived(update.setMessage("Список товаров")));
    //}
    @Test
    public void testSee() {
        assertEquals("Модели какого товара хотите посмотреть?", lBot.parseMessage("/seeModels"));
;
    }
//    @Test
//    public void testGet() {
//        assertEquals("Ноутбуки Смартфоны", lBot.parseMessage("/get"));
//    }
    @Test
    public void testStart() {
        assertEquals("Приветствую в нашем магазине. Наберите /help для просмотра списка команд", lBot.parseMessage("/start"));
    }
    @Test
    public void testHelp() {
        assertEquals("/get - вывод списка товаров, /seeModels - вывод моделей товаров", lBot.parseMessage("/help"));
    }
}