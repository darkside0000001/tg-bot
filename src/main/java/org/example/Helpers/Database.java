package org.example.Helpers;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *Класс, реализующий работу базы данных
 */
public class Database {
    private Connection conn;

    /**
     *Метод для подключения базы данных
     */
    public void ConnectToDB() {
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:sq1.db");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Database(){
        ConnectToDB();
    }

    /**
     *Метод, который реализует чтение из базы данных
     */
    public List<String> ReadDB(String type, String obj, Integer from, Integer to) throws SQLException{
        PreparedStatement prepared = conn.prepareStatement("SELECT * FROM products WHERE type = ? and object = ? and price >= ? and price <= ?;");
        prepared.setString(1, type);
        prepared.setString(2, obj);
        prepared.setInt(3, from);
        prepared.setInt(4, to);
        ResultSet resSet = prepared.executeQuery();
        short iter = 0;
        ArrayList<String> products = new ArrayList<>();
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
    public List<String> getModels(String type) throws SQLException{
        PreparedStatement prepared = conn.prepareStatement("SELECT * FROM products WHERE type = ?;");
        prepared.setString(1, type);
        ResultSet resSet = prepared.executeQuery();
        short iter = 0;
        List<String> products = new ArrayList<>();
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
    public List<String> giveCart(long id) throws ClassNotFoundException, SQLException{
        PreparedStatement prepared = conn.prepareStatement("SELECT * FROM cart WHERE id = ?;");
        prepared.setLong(1, id);
        ResultSet resSet = prepared.executeQuery();
        short iter = 0;
        List<String> cart = new ArrayList<>();
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
    public void addCart(long chatId, String product) throws SQLException {
        PreparedStatement prepared = conn.prepareStatement("SELECT * FROM cart WHERE id = ? and products = ?;");
        prepared.setLong(1, chatId);
        prepared.setString(2, product);
        ResultSet resSet = prepared.executeQuery();
        short iter = 0;
        List<String> cart = new ArrayList<>();
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
    public void deleteCart(long chatId) throws SQLException{
        PreparedStatement st = conn.prepareStatement("DELETE FROM cart WHERE id = ?;");
        st.setLong(1, chatId);
        st.executeUpdate();
    }

    /**
     *Метод, который реализует добавление фильтра
     */
    public void addFilter(long chatId, String type, String obj, Integer from, Integer to) throws SQLException{
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
    public Integer getRowCount(long chatId) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT COUNT(*) as count FROM `filters` WHERE `user_id` = ?");
        st.setLong(1, chatId);
        ResultSet res = st.executeQuery();
        int count = 0;
        while(res.next()){
            count = res.getInt("count");
        }
        return count;
    }

    /**
     *Метод, который реализует удаление фильтра
     */
    private void deleteLRU(long chatId, Integer count) throws SQLException{
        PreparedStatement st = conn.prepareStatement("DELETE FROM `filters` WHERE `id` IN (SELECT `id` FROM `filters` WHERE `user_id` = ? ORDER BY `id` ASC LIMIT ?)");
        st.setLong(1, chatId);
        st.setInt(2, count);
        st.executeUpdate();
    }

    /**
     *Метод, который реализует просмотр последних фильтров
     */
    public List<List<String>> showLatestFilters(long chatId) throws SQLException{
        PreparedStatement st = conn.prepareStatement("SELECT `type`, `object`, `price_from`, `price_to` FROM `filters` WHERE `user_id` = ? ORDER BY `id` DESC");
        st.setLong(1, chatId);
        ResultSet res = st.executeQuery();
        List<List<String>> list = new ArrayList<>();
        while(res.next()){
            List<String> line = new ArrayList<>();
            line.add(res.getString("type"));
            line.add(res.getString("object"));
            line.add(res.getString("price_from"));
            line.add(res.getString("price_to"));
            list.add(line);
        }
        return list;
    }


    /**
     *Вывод моделей товара по фильтру
     */
    public List<Object> giveDB(long chatId, boolean addFilter, User userData) throws SQLException {
        List<Object> answer = new ArrayList<>();
        
        List<String> device = ReadDB(userData.type, userData.obj, Integer.parseInt(userData.priceFrom), Integer.parseInt(userData.priceTo));

        if (addFilter) {
            addFilter(chatId, userData.type, userData.obj, Integer.parseInt(userData.priceFrom), Integer.parseInt(userData.priceTo));
        }

        List<String> aa = new ArrayList<>();

        if (device.contains("Нет таких товаров")) {
            aa.add("Нету");
        } else {
            for (String s : device) {
                aa.add(s);
                userData.cart = aa;
                answer.add(s);
            }
        }
        return answer;
    } 

    /**
     *Просмотр товаров со скидками
     */
    public List<String> giveDiscounts() throws SQLException{
        PreparedStatement prepared = conn.prepareStatement("SELECT name, discount_size FROM products WHERE on_sale = 1;");
        ResultSet resSet = prepared.executeQuery();
        short iter = 0;
        List<String> products = new ArrayList<>();
        while(resSet.next()){
            String name = resSet.getString("name");
            int discount = resSet.getInt("discount_size");
            products.add(name);
            products.add(String.valueOf(discount));
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
    public void addDiscount(String name, double discount) throws SQLException{
        PreparedStatement prepared = conn.prepareStatement("SELECT price, object, type FROM products WHERE name = ?;");
        prepared.setString(1, name);
        ResultSet res = prepared.executeQuery();
        int price = 0;
        String obj = "aa";
        String type = "aa";
        while(res.next()){
            price = res.getInt("price");
            obj = res.getString("object");
            type = res.getString("type");
        }
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
    public void deleteDiscount(String name) throws SQLException {
        PreparedStatement prepared = conn.prepareStatement("SELECT old_price, object, type FROM products WHERE name = ?;");
        prepared.setString(1, name);
        ResultSet res = prepared.executeQuery();
        int price = 0;
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

    /**
     *Добавление частоты уведомлений
     */
    public void addInterval(Long user_id, Integer interval) throws SQLException {
        PreparedStatement prepared = conn.prepareStatement("INSERT INTO 'subscriptions' ('user_id', 'interval') VALUES (?, ?);");
        prepared.setLong(1, user_id);
        prepared.setInt(2, interval);
        prepared.executeUpdate();
    }

    /**
     *Проверка, пришло ли время отправки сообщения
     */
    private Boolean readyToSend(Long user_id, Integer interval) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT `timestamp` FROM `last_send` WHERE user_id = ?;");
        st.setLong(1, user_id);
        ResultSet res = st.executeQuery();
        long old_timestamp = 0L;
        while(res.next()) {
            old_timestamp = res.getLong("timestamp");
        }
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        return old_timestamp + interval * 1000 < timestamp;
    }

    /**
     *Обновление таймера
     */
    private void updateTimestamp(Long user_id) throws SQLException {
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        PreparedStatement upd = conn.prepareStatement("REPLACE INTO `last_send`(`user_id`, `timestamp`) VALUES(?, ?);");
        upd.setLong(1, user_id);
        upd.setLong(2, timestamp);
        upd.executeUpdate();
    }

    /**
     *Получение всех id пользователей
     */
    public List<Long> getAllIUsers() throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT `user_id`, `interval` FROM `subscriptions`");
        ResultSet res = st.executeQuery();
        List<Long> users = new ArrayList<>();
        
        while(res.next()) {
            Long user_id = res.getLong("user_id");
            Integer interval = res.getInt("interval");
            if (readyToSend(user_id, interval)) {
                users.add(user_id);
                updateTimestamp(user_id);
            }
        }
        return users;
    }

    /**
     *Метод, который реализует отключение уведомлений о скидках
     */
    public void deleteSubs(Long user_id) throws SQLException{
        PreparedStatement st = conn.prepareStatement("DELETE FROM subscriptions WHERE user_id = ?;");
        st.setLong(1, user_id);
        st.executeUpdate();
        PreparedStatement pr = conn.prepareStatement("DELETE FROM last_send WHERE user_id = ?;");
        pr.setLong(1, user_id);
        pr.executeUpdate();
    }

    /**
     * Вывод всех продуктов
     */
    public List<String> ShowAllProducts() throws SQLException{
        PreparedStatement st = conn.prepareStatement("SELECT `name` FROM `products`");
        ResultSet res = st.executeQuery();
        List<String> products = new ArrayList<>();
        while(res.next()) {
            products.add(res.getString("name"));
        }

        return products;
    }

    /**
     * Просмотр отзывов на продукт
     */
    public String ShowReviews(String name) throws SQLException {
        PreparedStatement st = conn.prepareStatement("SELECT `reviews` FROM `products` WHERE `name` = ?");
        st.setString(1, name);
        ResultSet res = st.executeQuery();
        String review = "aa";
        while(res.next()) {
            review = res.getString("reviews");
        }
        if(review == null){
            review = "На этот продукт еще нет отзывов";
        }
        return review;
    }

    /**
     * Добавление отзыва
     */
    public void AddProductReview(String name, String review) throws SQLException{
        PreparedStatement prepared = conn.prepareStatement("SELECT price, object, type, on_sale, discount_size, old_price, reviews FROM products WHERE name = ?;");
        prepared.setString(1, name);
        ResultSet res = prepared.executeQuery();
        int price = 0;
        String obj = "aa";
        String type = "aa";
        int on_sale = 0;
        int discount_size = 0;
        int old_price = 0;
        String rev = "aa";
        while(res.next()){
            price = res.getInt("price");
            obj = res.getString("object");
            type = res.getString("type");
            on_sale = res.getInt("on_sale");
            discount_size = res.getInt("discount_size");
            old_price = res.getInt("old_price");
            rev = res.getString("reviews");
        }
        if(rev != null){
            review = rev + ", " + review;
        }
        PreparedStatement st = conn.prepareStatement("DELETE FROM products WHERE name = ?;");
        st.setString(1, name);
        st.executeUpdate();
        PreparedStatement prep = conn.prepareStatement("INSERT INTO 'products' ('on_sale', 'discount_size','price', 'name', 'object', 'type', 'old_price', 'reviews') VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
        prep.setInt(1, on_sale);
        prep.setDouble(2, discount_size);
        prep.setDouble(3, price);
        prep.setString(4, name);
        prep.setString(5, obj);
        prep.setString(6, type);
        prep.setDouble(7, old_price);
        prep.setString(8, review);
        prep.executeUpdate();
    }

}

