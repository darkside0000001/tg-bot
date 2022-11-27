package org.example;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *Класс для реализации бота в телеграме
 */
public class ConsoleBot {
    Database db = new Database();
    BotLogic blogic = new BotLogic(db);
    Scanner in = new Scanner(System.in);


    public ConsoleBot() throws Exception {
        System.out.println("Приветствую в нашем магазине. Наберите '/help' для просмотра списка команд");
        while (true) {
            ExecutorService executor = Executors.newFixedThreadPool(10);
            executor.submit(() -> {
                try {
                    new EventLoopConsole();
                } catch (ClassNotFoundException | SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
            String line = in.nextLine();
            List<Object> Answer = blogic.parseMessage(line, 0, "cons");
            if ((Integer)Answer.get(2) == 0 || (Integer)Answer.get(2) == 1) {
                sendMessage((String)Answer.get(0));
            } else if ((Integer)Answer.get(2) == 3) {
                sendMessage(sendQuestionType());
            } else if ((Integer)Answer.get(2) == 4) {
                sendModelsProduct((String)Answer.get(0));
            } else if ((Integer)Answer.get(2) == 7) {
                //sendCartConsole();
                System.out.println("Выберете опцию - Посмотреть корзину, Очистить корзину");
                String answer = in.nextLine();
                if(answer.equals("Посмотреть корзину")){
                    sendMessage((String)db.giveCart(0).get(0));
                } else if(answer.equals("Очистить корзину")) {
                    db.cleanCart(0);
                    System.out.println("Корзина очищена");
                }
            } else if ((Integer)Answer.get(2) == 12) {
                sendFilters();
            } else if ((Integer)Answer.get(2) == 13) {
                System.out.println("Выберите опцию - Посмотреть скидки, Подключить уведомления о скидках");
                String answer = in.nextLine();
                if (answer.equals("Посмотреть скидки")) {
                    sendDiscounts(0);
                } else if (answer.equals("Подключить уведомления о скидках")) {
                    System.out.println("Выберете частоту уведомлений - 30 секунд, 1 чаc, 1 день, Отписаться");
                } else if (answer.equals("30 секунд")) {
                    System.out.println("Включены уведомления на 30 секунд");
                    db.addInterval(Long.valueOf(0), 30);
                } else if (answer.equals("1 час")) {
                    System.out.println("Включены уведомления на 1 час");
                    db.addInterval(Long.valueOf(0), 3600);
                } else if (answer.equals("1 день")) {
                    db.addInterval(Long.valueOf(0), 86400);
                    System.out.println("Включены уведомления на 1 день");
                } else if (answer.equals("Отписаться")) {
                    db.deleteSubs(0L);
                    System.out.println("Уведомления отключены");
                }
            }
        }
    }

    /**
     *Метод, который реализует отправку сообщений пользователю
     */
    public void sendMessage(String textToSend) {
        System.out.println(textToSend);
    }

    /**
     *Метод, который реализует вывод предыдущих фильтров
     */
    public void sendFilters() throws SQLException, ClassNotFoundException{
        System.out.println(db.showLatestFilters(0));
    }

    /**
     *Метод, который реализует подбор товаров
     */
    private String sendQuestionType() throws Exception {
        Globals global = Users.getUserGlobals((long) 0);
        System.out.println("Что вы хотите посмотреть? (smartphone, notebook)");
        global.type = in.nextLine();
        System.out.println("Цель (gaming, study, work)");
        global.obj = in.nextLine();
        System.out.println("Цена (меньше 20000, от 20000 до 40000, от 40000 до 60000, больше 60000)");
        System.out.println("Выберете - 1, 2, 3 или 4");
        String answer = in.nextLine();
        if(answer.equals("1")){
            global.priceFrom = "0";
            global.priceTo = "20000";
        } else if(answer.equals("2")){
            global.priceFrom = "20000";
            global.priceTo = "40000";
        } else if(answer.equals("3")){
            global.priceFrom = "40000";
            global.priceTo = "60000";
        } else if(answer.equals("4")){
            global.priceFrom = "60000";
            global.priceTo = "600000";
        }
        String product = blogic.parseDB(0, true);
        if (!Objects.equals(product, "Извините, в данное время нет таких товаров")) {
            sendMessage(product + "\n Добавить товар в корзину? Да / Нет");
            String line = in.nextLine();
            if (line.equals("Да")) {
                db.addToCart(0);
                return "Товар добавлен";
            } else {
                return "Товар не был добавлен в корзину";
            }
        }

        return "Нет такого товара";
    }

    /**
     *Метод, который реализует просмотр актуальных моделей товаров
     */
    public void sendModelsProduct(String arg) throws SQLException, ClassNotFoundException {
        ArrayList<String> models = db.getModels(arg);
        if (models.contains("Нет таких товаров")) {
            System.out.println("Извините, в данное время нет таких товаров");
        } else {
            for (int i = 0; i < models.size(); i++) {
                System.out.println(models.get(i));
            }
        }
    }

    /**
     *Метод, который отправляет товары со скидками
     */
    private void sendDiscounts(long chatId) throws ClassNotFoundException, SQLException{
        ArrayList<String> products;
        products = db.giveDiscounts();
        if (products.contains("нету ничего")) {
            sendMessage( "Извините, в данное время нет скидок");
        } else {
            for (int i = 0; i < products.size() - 1; i += 2) {
                sendMessage( "Сегодня продается " + products.get(i) + " с " + products.get(i+1) + "% скидкой!");
            }
        }
    }
}








