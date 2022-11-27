import org.example.BotLogic;
import org.example.Database;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class Tests {
    
    @Mock
    Database dbMock = mock(Database.class);
    BotLogic botLogic = new BotLogic(dbMock);
    

    /**
     *Тест на получения товаров из корзины
     */
    @Test
    public void giveCartTest() throws Exception {
        when(dbMock.giveCart(-1)).thenReturn(
                Arrays.asList("HP 15s-eq1332ur")
            );

            String answer = (String)botLogic.parseMessage("Посмотреть корзину",-1, "tele").get(0);
            assertEquals("У вас в корзине HP 15s-eq1332ur\n", answer);
        }


    /**
     *Тест на фильтр товаров
     */
    @Test
    public void priceFormTest() throws Exception {
            when(dbMock.giveDB(0, true)).thenReturn(
                    Arrays.asList("Asus Rog 5")
            );

            String answer = (String)botLogic.priceForm("40000", "60000", 0).get(0);
            assertEquals("Вам подойдет Asus Rog 5\n", answer);
        }

    /**
     *Тест на добавление товаров в корзину
     */
    @Test
    public void addCartTest() throws Exception {
            when(dbMock.giveCart(-1)).thenReturn(
                    Arrays.asList("HP 15s-eq1332ur", "Asus Rog 5")
            );
            String answer = botLogic.parseCart(-1);
            assertEquals("У вас в корзине HP 15s-eq1332ur\nУ вас в корзине Asus Rog 5\n", answer);
    }

    /**
     *Тест на очистку корзины
     */
    @Test
    public void cleanCartTest() throws Exception {
            dbMock.addCart(-1, "HP 15s-eq1332ur");
            dbMock.cleanCart(-1);
            //verify(dbMock, times(1)).cleanCart(-1);

            //String answer = bl.parseCart(-1);
            //assertEquals("Ваша корзина пуста", answer);
    }

    /**
     * Тест просмотра скидок
     */
    @Test
    public void sendDiscountsTest() throws Exception {

            when(dbMock.giveDiscounts()).thenReturn(
                new ArrayList<String>(Arrays.asList("HP 15s-eq1332ur", "40"))
            );
            String answer = (String) botLogic.parseMessage("Посмотреть скидки", -1, "tele").get(0);
            assertEquals("Сегодня продается HP 15s-eq1332ur с 40% скидкой!\n", answer);
        }
}
