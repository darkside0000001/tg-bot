import org.example.Helpers.BotLogic;
import org.example.Helpers.Database;
import org.example.Helpers.EventLoop;
import org.example.Helpers.User;
import org.example.Notifier.NoticeType;
import org.example.Notifier.TestNotice;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                List.of("HP 15s-eq1332ur")
            );

            String answer = (String)botLogic.parseMessage("Посмотреть корзину",-1, "tele", "").get(0);
            assertEquals("У вас в корзине HP 15s-eq1332ur\n", answer);
        }

    /**
     *Тест на подбор товара
     */
    @Test
    public void parseDBTest() throws Exception {
            User userData = botLogic.getUserGlobals(Long.valueOf(-1));
            when(dbMock.giveDB(-1, true, userData)).thenReturn(
                    List.of("Asus Rog 5")
            );
            String answer = botLogic.parseDB( -1, true);
            assertEquals("Вам подойдет Asus Rog 5\n", answer);
        }

    /**
     *Тест на добавление товаров в корзину
     */
    @Test
    public void addCartTest() throws Exception {
        when(dbMock.giveCart(-1)).thenReturn(
                List.of("HP 15s-eq1332ur", "Asus Rog 5")
        );
        String answer = botLogic.parseCart(-1);
        assertEquals("У вас в корзине HP 15s-eq1332ur\nУ вас в корзине Asus Rog 5\n", answer);
    }

    /**
     *Тест на очистку корзины
     */
    @Test
    public void cleanCartTest() throws Exception {
        botLogic.cleanCart(-1);
        verify(dbMock).deleteCart(-1);
    }

    /**
     * Тест на просмотр скидок
     */
    @Test
    public void sendDiscountsTest() throws Exception {
        when(dbMock.giveDiscounts()).thenReturn(
                List.of("HP 15s-eq1332ur", "40")
            );
        String answer = (String) botLogic.parseMessage("Посмотреть скидки", -1, "tele", "").get(0);
        assertEquals("Сегодня продается HP 15s-eq1332ur с 40% скидкой!\n", answer);
    }

    /**
     * Тест на просмотр отзывов
     */
    @Test
    public void CheckReviewsTest() throws Exception {
        when(dbMock.ShowReviews("Test")).thenReturn("Товар хороший");
        String answer = (String) botLogic.parseMessage("Посмотреть отзывы", -1, "tele", "Test").get(0);
        assertEquals("Товар хороший", answer);
    }

    @Test
    public void NoticeTest() throws Exception {
        TestNotice testNotice = new TestNotice();
        String answer = testNotice.sendMessage(-1L, "Сегодня продается Asus Rog 5 с 60% скидкой!\n");
        assertEquals("Сегодня продается Asus Rog 5 с 60% скидкой!\n", answer);
    }
}
