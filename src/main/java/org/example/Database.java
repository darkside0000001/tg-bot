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
     *Метод, который реализует добавление товара в корзину
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
    public static void cleanCart(long chatId) throws SQLException, ClassNotFoundException {
        Database.Conn();
        Database.deleteCart(chatId);
    }

    /**
     *Метод, который реализует добавление товара в корзину по id пользователя
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
        List<Object> answer = new ArrayList<>();
        ArrayList<String> device = Database.getCart(chatId);
        if (!device.contains("пусто")) {
            answer.addAll(device);
        }
        return answer;
    }

    /**
     *Вывод моделей товара по фильтру
     */
    public List<Object> giveDB(long chatId, boolean addFilter) throws SQLException, ClassNotFoundException {
        Database.Conn();
        List<Object> answer = new ArrayList<Object>();
        Globals global = Users.getUserGlobals(chatId);
        ArrayList<String> device = Database.ReadDB(global.type, global.obj, Integer.parseInt(global.priceFrom), Integer.parseInt(global.priceTo));

        if (addFilter) {
            Database.addFilter(chatId, global.type, global.obj, Integer.parseInt(global.priceFrom), Integer.parseInt(global.priceTo));
        }

        ArrayList<String> aa = new ArrayList<String>();

        if (device.contains("Нет таких товаров")) {
            aa.add("Нету");
        } else {
            for (int i = 0; i < device.size(); i++) {
                aa.add(device.get(i));
                global.cart = aa;
                answer.add(device.get(i));
            }
        }
        return answer;
    } 

    /**
     *Вывод товаров со скидками
     */
    public static ArrayList<String> giveDiscounts() throws ClassNotFoundException, SQLException{
        Database.Conn();
        PreparedStatement prepared = conn.prepareStatement("SELECT name, discount_size FROM products WHERE on_sale = 1;");
        ResultSet resSet = prepared.executeQuery();
        short iter = 0;
        ArrayList<String> products = new ArrayList<String>();
        while(resSet.next()){
            String name = resSet.getString("name");
            Integer discount = resSet.getInt("discount_size");
            products.add(name);
            products.add(discount.toString());
            iter += 1;
        }
        if(iter == 0){
            products.add("нету ничего");
            return products;
        }
        return products;
    }

    /**
     *Изменение цены на товар с учетом скидки
     */
    public static void addDiscount(String name, double discount) throws ClassNotFoundException, SQLException{
        Database.Conn();
        PreparedStatement prepared = conn.prepareStatement("SELECT price, object, type FROM products WHERE name = ?;");
        prepared.setString(1, name);
        ResultSet res = prepared.executeQuery();
        Integer price = 0;
        String obj = "aa";
        String type = "aa";
        while(res.next()){
            price = res.getInt("price");
            obj = res.getString("object");
            type = res.getString("type");
        }
        double a = discount/100;
        PreparedStatement st = conn.prepareStatement("DELETE FROM products WHERE name = ?;");
        st.setString(1, name);
        st.executeUpdate();
        PreparedStatement prep = conn.prepareStatement("INSERT INTO 'products' ('on_sale', 'discount_size','price', 'name', 'object', 'type', 'old_price') VALUES (?, ?, ?, ?, ?, ?, ?);");
        prep.setInt(1, 1);
        prep.setDouble(2, discount);
        prep.setDouble(3, price - price*(discount/100));
        prep.setString(4, name);
        prep.setString(5, obj);
        prep.setString(6, type);
        prep.setDouble(7, price);
        prep.executeUpdate();
    }

    /**
     *Удаление скидки и возвращение предыдущей цены
     */
    public static void deleteDiscount(String name) throws SQLException, ClassNotFoundException{
        Database.Conn();
        PreparedStatement prepared = conn.prepareStatement("SELECT old_price, object, type FROM products WHERE name = ?;");
        prepared.setString(1, name);
        ResultSet res = prepared.executeQuery();
        Integer price = 0;
        String obj = "aa";
        String type = "aa";
        while(res.next()){
            price = res.getInt("old_price");
            obj = res.getString("object");
            type = res.getString("type");
        }
        PreparedStatement st = conn.prepareStatement("DELETE FROM products WHERE name = ?;");
        st.setString(1, name);
        st.executeUpdate();
        PreparedStatement prep = conn.prepareStatement("INSERT INTO 'products' ('on_sale', 'discount_size','price', 'name', 'object', 'type') VALUES (?, ?, ?, ?, ?, ?);");
        prep.setInt(1, 0);
        prep.setDouble(2, 0);
        prep.setDouble(3, price);
        prep.setString(4, name);
        prep.setString(5, obj);
        prep.setString(6, type);
        prep.executeUpdate();
    }
}

