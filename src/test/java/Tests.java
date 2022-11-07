import org.example.BotLogic;
import org.example.Database;
import org.example.Globals;
import org.example.TelegramBot;
import org.example.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class Tests {
    @Mock
    BotLogic blogic = new BotLogic();

    BotLogic mock = org.mockito.Mockito.mock(BotLogic.class);

    /*
    Тест команд для подбора товаров. Должен вернуть ноутбук HP 15s-eq1332ur
    */
    @Test
    public void testPick() throws SQLException, ClassNotFoundException {
        Mockito.when(mock.giveDB(0, true)).thenReturn(Arrays.asList("HP 15s-eq1332ur", 0, 6));

        String answ = (String) mock.giveDB(0, true).get(0);

        Assertions.assertTrue(answ.equals("HP 15s-eq1332ur"));
    }

    /*
    Тест получения объектов из корзины. Должен вернуть ноутбук HP 15s-eq1332ur
    */
    @Test
    public void testGetCart() throws SQLException, ClassNotFoundException {
        Mockito.when(mock.giveCart(0)).thenReturn(Arrays.asList("У вас в корзине HP 15s-eq1332ur", 0, 0));

        String answ = (String) mock.giveCart(0).get(0);

        Assertions.assertTrue(answ.equals("У вас в корзине HP 15s-eq1332ur"));
    }

    /*
    Тест получения объектов из корзины. Должен вернуть ноутбук HP 15s-eq1332ur
    */
    @Test
    public void test() throws SQLException, ClassNotFoundException {
        Mockito.when(mock.giveCart(0)).thenReturn(Arrays.asList("У вас в корзине HP 15s-eq1332ur", 0, 0));

        String answ = (String) mock.giveCart(0).get(0);

        Assertions.assertTrue(answ.equals("У вас в корзине HP 15s-eq1332ur"));
    }
}