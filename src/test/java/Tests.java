import org.example.BotLogic;
import org.example.Database;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *Класс, реализующий тестирование бота
 */
public class Tests {
    @Mock
    Database dbMock = mock(Database.class);
    BotLogic blMock = new BotLogic(dbMock);

    Database db = new Database();
    BotLogic bl = new BotLogic(db);

    /**
     Тест на получения товаров из корзины
     */
    @Test
    public void giveCart() throws Exception {
        when(dbMock.giveCart(0)).thenReturn(
                Arrays.asList("У вас в корзине HP 15s-eq1332ur", 0, 6)
        );

        String answer = (String)blMock.parseMessage("Посмотреть корзину",0, "tele").get(0);
        assertEquals("У вас в корзине HP 15s-eq1332ur", answer);
    }

    /**
     Тест на фильтр товаров
     */
    @Test
    public void priceForm() throws Exception {
        when(dbMock.giveDB(0, true)).thenReturn(
                Arrays.asList("Вам подойдет Asus Rog 5", 0, 6)
        );

        String answer = (String)blMock.priceForm("40000", "60000", 0).get(0);
        assertEquals("Вам подойдет Asus Rog 5", answer);
    }

    /**
     Тест на добавление товаров в корзину
     */
    @Test
    public void addCart() throws Exception {
        db.addCart(-1, "HP 15s-eq1332ur");
        String answer = (String)bl.parseMessage("Посмотреть корзину", -1, "tele").get(0);
        assertEquals("У вас в корзине HP 15s-eq1332ur\n", answer);
    }

    /**
     Тест на очистку корзины
     */
    @Test
    public void cleanCart() throws Exception {
        bl.parseMessage("Очистить корзину", -1, "tele");
        String answer = (String)bl.parseMessage("Посмотреть корзину", -1, "tele").get(0);
        assertEquals("Ваша корзина пуста ", answer);
    }
}