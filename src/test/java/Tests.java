import org.example.BotLogic;
import org.example.Database;
import org.example.Globals;
import org.example.TelegramBot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    BotLogic blogic = new BotLogic();

    /*
    *Вызываем команду /help в телеграмм боте
     */
    @Test
    public void testTelegramHelp() {
        Assertions.assertEquals("Это онлайн магазин в котором есть опции простотра товаров и подборки товаров под себя",
                blogic.parseMessage("/help", 0, "tele").get(0));
    }

    /*
    *Тест команды "Список товаров" в телеграм боте
     */
    @Test
    public void testTelegramGet() {
        Assertions.assertEquals( "Сегодня у нас в наличии смартфоны и ноутбуки" ,
                blogic.parseMessage("Список товаров",0,"tele").get(0));
    }

    /*
    *Вызываем команду /help в консольном боте
     */
    @Test
    public void testConsHelp() {
        Assertions.assertEquals("/get - вывод списка товаров, /seeModels - вывод моделей товаров, /pick - подобрать",
                blogic.parseMessage("/help", 0, "cons").get(0));
    }

    /*
     *Вызываем команду /start в консольном боте
     */
    @Test
    public void testConsStart() {
        Assertions.assertEquals("Приветствую в нашем магазине. Наберите /help для просмотра списка команд",
                blogic.parseMessage("/start", 0, "cons").get(0));
    }

    /*
    *Вызываем команду с одинаковым выводом в консоли и телеграмм боте
     */
    @Test
    public void testConsAndTelegram() {
        String answTelegram = (String) blogic.parseMessage("Список товаров",0,"tele").get(0);
        Assertions.assertEquals( answTelegram,
                blogic.parseMessage("/get",0,"cons").get(0));
    }

    /*
    Тест команд для подбора товаров в телеграмм боте
     */
    @Test
    public void testTelegramPick() throws SQLException, ClassNotFoundException {
        Globals.type = (String) blogic.parseMessage("смартфоны",0,"tele").get(0);
        Globals.obj = (String) blogic.parseMessage("Игры",0,"tele").get(0);
        Globals.price = (String) blogic.parseMessage("50000",0,"tele").get(0);
        Database.Conn();
        ArrayList<String> device = Database.ReadDB(Globals.type, Globals.obj, Globals.price);
        Assertions.assertEquals( true,
                blogic.parseMessage("Подобрать товар",0,"tele").get(0).equals("Что вас интересует?") && device.get(0).equals("Asus Rog 5"));
    }

    /*
    Тест команд для подбора товаров в консольном боте
     */
    @Test
    public void testConsolePick() throws SQLException, ClassNotFoundException {
        Globals.type = (String) blogic.parseMessage("ноутбуки",0,"cons").get(0);
        Globals.obj = (String) blogic.parseMessage("Учеба",0,"cons").get(0);
        Globals.price = (String) blogic.parseMessage("30000",0,"cons").get(0);
        Database.Conn();
        ArrayList<String> device = Database.ReadDB(Globals.type, Globals.obj, Globals.price);
        Assertions.assertEquals( true,
                blogic.parseMessage("/pick",0,"cons").get(0).equals("Что вас интересует?") && device.get(0).equals("HP 15s-eq1332ur"));
    }
}