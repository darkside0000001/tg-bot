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
	public static ResultSet resSet;

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
    public static ArrayList<String> ReadDB(String type, String obj, String price) throws ClassNotFoundException, SQLException{
        PreparedStatement prepared = conn.prepareStatement("SELECT * FROM products WHERE type = ? and object = ? and price = ?;");
        prepared.setString(1, type);
        prepared.setString(2, obj);
        prepared.setString(3, price);
        resSet = prepared.executeQuery();
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
    public static ArrayList<String> get_Models(String type) throws ClassNotFoundException, SQLException{
        PreparedStatement prepared = conn.prepareStatement("SELECT * FROM products WHERE type = ?;");
        prepared.setString(1, type);
        resSet = prepared.executeQuery();
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
    // public static void WriteDB() throws SQLException
    // {
    //        System.out.println("Тsaddad");
    //        statmt.execute("INSERT INTO 'products' ('name', 'object', 'type', 'price') VALUES ('HP 15s-eq1332ur', 'study', 'notebook', '30000'); ");
    //        statmt.execute("INSERT INTO 'products' ('name', 'object', 'type', 'price') VALUES ('Asus Rog 5', 'gaming', 'smartphone', '50000'); ");

    //        System.out.println("Таблица заполнена");
    // }
}
