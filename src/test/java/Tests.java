import org.example.BotLogic;
import org.example.TelegramBot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    TelegramBot tgBot = new TelegramBot();

    BotLogic blogic = new BotLogic();

    // Вызываем команду /help в телеграмм боте
    @Test
    public void testTeleHelp() {
        Assertions.assertEquals("Это онлайн магазин в котором есть опции простотра товаров и подборки товаров под себя",
                blogic.parseMessage("/help", 0, "tele").get(0));
    }

    // Вызываем команду Список товаров в телеграмм боте
    @Test
    public void testTeleGet() {
        Assertions.assertEquals( "Сегодня у нас в наличии смартфоны и ноутбуки" ,
                blogic.parseMessage("Список товаров",0,"tele").get(0));
    }

    // Вызываем команду /help в консольном боте
    @Test
    public void testConsHelp() {
        Assertions.assertEquals("/get - вывод списка товаров, /seeModels - вывод моделей товаров, /pick - подобрать",
                blogic.parseMessage("/help", 0, "cons").get(0));
    }

    // Вызываем команду /start в консольном боте
    @Test
    public void testConsStart() {
        Assertions.assertEquals("Приветствую в нашем магазине. Наберите /help для просмотра списка команд",
                blogic.parseMessage("/start", 0, "cons").get(0));
    }

    // Вызываем команду с одинаковым выводом в консоли и телеграмм боте
    @Test
    public void testConsAndTele() {
        String answ_tele = (String) blogic.parseMessage("Список товаров",0,"tele").get(0);
        Assertions.assertEquals( answ_tele,
                blogic.parseMessage("/get",0,"cons").get(0));
    }
}