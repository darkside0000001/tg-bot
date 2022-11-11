package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *Класс, реализующий работу базы данных
 */
public class Database {
    private final String name = null;
    public static Connection conn;
    public static Statement statmt;

    /**
     *Метод для подключения базы данных
     */
    public static void Conn() throws ClassNotFoundException, SQLException{
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:sq1.db");
            statmt = conn.createStatement();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     *Метод, который реализует чтение из базы данных
     */
    public static ArrayList<String> ReadDB(String type, String obj, Integer from, Integer to) throws ClassNotFoundException, SQLException{
        PreparedStatement prepared = conn.prepareStatement("SELECT * FROM products WHERE type = ? and object = ? and price >= ? and price <= ?;");
        prepared.setString(1, type);
        prepared.setString(2, obj);
        prepared.setInt(3, from);
        prepared.setInt(4, to);
        ResultSet resSet = prepared.executeQuery();
        short iter = 0;
        ArrayList<String> products = new ArrayList<String>();
        while(resSet.next()){
            String name = resSet.getString("name");
            products.add(name);
            iter += 1;
        }
        if(iter == 0){
            products.add("Нет таких товаров");
            return products;
        }
        return products;
    }

    /**
     *Метод, который реализует просмотр моделей
     */
    public static ArrayList<String> getModels(String type) throws ClassNotFoundException, SQLException{
        PreparedStatement prepared = conn.prepareStatement("SELECT * FROM products WHERE type = ?;");
        prepared.setString(1, type);
        ResultSet resSet = prepared.executeQuery();
        short iter = 0;
        ArrayList<String> products = new ArrayList<String>();
        while(resSet.next()){
            String name = resSet.getString("name");
            products.add(name);
            iter += 1;
        }
        if(iter == 0){
            products.add("Нет таких товаров");
            return products;
        }
        return products;
    }

    /**
     *Метод, который реализует просмотр корзины
     */
    public static ArrayList<String> getCart(long id) throws ClassNotFoundException, SQLException{
        PreparedStatement prepared = conn.prepareStatement("SELECT * FROM cart WHERE id = ?;");
        prepared.setLong(1, id);
        ResultSet resSet = prepared.executeQuery();
        short iter = 0;
        ArrayList<String> cart = new ArrayList<String>();
        while(resSet.next()){
            String name = resSet.getString("products");
            cart.add(name);
            iter += 1;
        }
        if(iter == 0){
            cart.add("пусто");
            return cart;
        }
        return cart;
    }

    /**
     *Метод, который добавление товара в корзину
     */
    public static void addCart(long chatId, String product) throws SQLException, ClassNotFoundException {
        Database.Conn();
        PreparedStatement prepared = conn.prepareStatement("SELECT * FROM cart WHERE id = ? and products = ?;");
        prepared.setLong(1, chatId);
        prepared.setString(2, product);
        ResultSet resSet = prepared.executeQuery();
        short iter = 0;
        ArrayList<String> cart = new ArrayList<String>();
        while(resSet.next()){
            String name = resSet.getString("products");
            cart.add(name);
            iter += 1;
        }
        if(iter == 0){
            PreparedStatement prep = conn.prepareStatement("INSERT INTO 'cart' ('id', 'products') VALUES (?, ?); ");
            prep.setLong(1, chatId);
            prep.setString(2, product);
            prep.executeUpdate();
        }
    }

    /**
     *Метод, который реализует удаление корзины
     */
    public static void deleteCart(long chatId) throws SQLException{
        PreparedStatement st = conn.prepareStatement("DELETE FROM cart WHERE id = ?;");
        st.setLong(1, chatId);
        st.executeUpdate();
    }

    /**
     *Метод, который реализует добавление фильтра
     */
    public static void addFilter(long chatId, String type, String obj, Integer from, Integer to) throws SQLException{
        final Integer limit = 5;
        Integer rows = getRowCount(chatId);
        if (rows >= limit) {
            deleteLRU(chatId, rows - (limit - 1));
        }

        PreparedStatement prepared = conn.prepareStatement("INSERT INTO 'filters' ('user_id', 'type','object','price_from', 'price_to') VALUES (?, ?, ?, ?, ?); ");
        prepared.setLong(1, chatId);
        prepared.setString(2, type);
        prepared.setString(3, obj);
        prepared.setInt(4, from);
        prepared.setInt(5, to);
        prepared.executeUpdate();
    }

    /**
     *Количество фильтров
     */
    public static Integer getRowCount(long chatId) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT COUNT(*) as count FROM `filters` WHERE `user_id` = ?");
        st.setLong(1, chatId);
        ResultSet res = st.executeQuery();
        Integer count = 0;
        while(res.next()){
            count = res.getInt("count");
        }
        return count;
    }

    /**
     *Метод, который реализует удаление фильтра
     */
    private static void deleteLRU(long chatId, Integer count) throws SQLException{
        PreparedStatement st = conn.prepareStatement("DELETE FROM `filters` WHERE `id` IN (SELECT `id` FROM `filters` WHERE `user_id` = ? ORDER BY `id` ASC LIMIT ?)");
        st.setLong(1, chatId);
        st.setInt(2, count);
        st.executeUpdate();
    }

    /**
     *Метод, который реализует просмотр последних фильтров
     */
    public static ArrayList<ArrayList<String>> showLatestFilters(long chatId) throws SQLException{
        PreparedStatement st = conn.prepareStatement("SELECT `type`, `object`, `price_from`, `price_to` FROM `filters` WHERE `user_id` = ? ORDER BY `id` DESC");
        st.setLong(1, chatId);
        ResultSet res = st.executeQuery();
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        while(res.next()){
            ArrayList<String> line = new ArrayList<String>();
            line.add(res.getString("type"));
            line.add(res.getString("object"));
            line.add(res.getString("price_from"));
            line.add(res.getString("price_to"));
            list.add(line);
        }
        return list;
    }

    /**
     *Метод, который реализует очистку корзины
     */
    public static List<Object> cleanCart(long chatId) throws SQLException, ClassNotFoundException {
        Database.Conn();
        Database.deleteCart(chatId);
        return listAppend("Корзина очищена", chatId, 0);
    }

    /**
     *Метод, который реализует добавление товара в корзину
     */
    void addToCart(long chatId) throws ClassNotFoundException, SQLException{
        Database.Conn();
        Globals global = Users.getUserGlobals(chatId);
        ArrayList<String> products = global.cart;
        Database.addCart(chatId, products.get(0));
    }

    /**
     *Метод, который реализует просмотр корзины
     */
    public List<Object> giveCart(long chatId) throws ClassNotFoundException, SQLException{
        Database.Conn();
        ArrayList<String> device = Database.getCart(chatId);
        if(device.contains("пусто")){
            return listAppend("Ваша корзина пуста ", chatId, 0);
        }
        else{
            StringBuilder answer = new StringBuilder();
            for (int i = 0; i < device.size(); i++) {
                answer.append("У вас в корзине ").append(device.get(i)).append("\n");
            }
            return listAppend(answer.toString(), chatId, 0);
        }
    }

    public List<Object> giveDB(long chatId, boolean addFilter) throws SQLException, ClassNotFoundException {
        Database.Conn();
        Globals global = Users.getUserGlobals(chatId);
        ArrayList<String> device = Database.ReadDB(global.type, global.obj, Integer.parseInt(global.priceFrom), Integer.parseInt(global.priceTo));
        if (addFilter) {
            Database.addFilter(chatId, global.type, global.obj, Integer.parseInt(global.priceFrom), Integer.parseInt(global.priceTo));
        }
        ArrayList<String> aa = new ArrayList<String>();
        if(device.contains("Нет таких товаров")){
            aa.add("Нету");
            return listAppend("Извините, в данное время нет таких товаров", chatId, 0);
        }
        else{
            StringBuilder answer = new StringBuilder();
            for (int i = 0; i < device.size(); i++) {
                aa.add(device.get(i));
                global.cart = aa;
                answer.append("Вам подойдет ").append(device.get(i)).append("\n");
            }
            return listAppend(answer.toString(), chatId, 6);
        }
    }

    public static List<Object> listAppend(String text, Long chatId, Integer actionType) {
        List<Object> params = Arrays.asList(text, chatId, actionType);

        return params;
    }
}

