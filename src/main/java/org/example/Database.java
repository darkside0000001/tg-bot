package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Database {
    private static final String name = null;
    public static Connection conn;
    public static Statement statmt;

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
    // public static void WriteDB() throws SQLException
    // {
    //        System.out.println("Тsaddad");
    //        statmt.execute("INSERT INTO 'products' ('name', 'object', 'type', 'price') VALUES ('HP 15s-eq1332ur', 'study', 'notebook', '30000'); ");
    //        statmt.execute("INSERT INTO 'products' ('name', 'object', 'type', 'price') VALUES ('Asus Rog 5', 'gaming', 'smartphone', '50000'); ");

    //        System.out.println("Таблица заполнена");
    // }
    public static void addCart(long chatId, String product) throws SQLException{
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

    public static void deleteCart(long chatId) throws SQLException{
        PreparedStatement st = conn.prepareStatement("DELETE FROM cart WHERE id = ?;");
        st.setLong(1, chatId);
        st.executeUpdate();
    }

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

    private static void deleteLRU(long chatId, Integer count) throws SQLException{
        PreparedStatement st = conn.prepareStatement("DELETE FROM `filters` WHERE `id` IN (SELECT `id` FROM `filters` WHERE `user_id` = ? ORDER BY `id` ASC LIMIT ?)");
        st.setLong(1, chatId);
        st.setInt(2, count);
        st.executeUpdate();
    }

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
}
