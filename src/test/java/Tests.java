import org.example.BotLogic;
import org.example.Database;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Tests {
    @Mock
    Database dbMock = mock(Database.class);
    BotLogic bl = new BotLogic(dbMock);

    /**
     Тест на получения товаров из корзины
     */
    @Test
    public void giveCart() throws Exception {
        when(dbMock.giveCart(-1)).thenReturn(
                Arrays.asList("HP 15s-eq1332ur")
        );

        String answer = (String)bl.parseMessage("Посмотреть корзину",-1, "tele").get(0);
        assertEquals("У вас в корзине HP 15s-eq1332ur\n", answer);
    }

    /**
     Тест на фильтр товаров
     */
    @Test
    public void priceForm() throws Exception {
        when(dbMock.giveDB(0, true)).thenReturn(
                Arrays.asList("Asus Rog 5")
        );

        String answer = (String)bl.priceForm("40000", "60000", 0).get(0);
        assertEquals("Вам подойдет Asus Rog 5\n", answer);
    }

    /**
     Тест на добавление товаров в корзину
     */
    @Test
    public void addCart() throws Exception {
        when(dbMock.giveCart(-1)).thenReturn(
                Arrays.asList("HP 15s-eq1332ur", "Asus Rog 5")
        );
        String answer = bl.parseCart(-1);
        assertEquals("У вас в корзине HP 15s-eq1332ur\nУ вас в корзине Asus Rog 5\n", answer);
    }

    /**
     Тест на очистку корзины
     */
    @Test
    public void cleanCart() throws Exception {
        dbMock.addCart(-1, "HP 15s-eq1332ur");
        dbMock.cleanCart(-1);
        String answer = bl.parseCart(-1);
        assertEquals("Ваша корзина пуста", answer);
    }
}