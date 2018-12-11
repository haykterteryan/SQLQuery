package com.paypal.desk;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {

    private static final Connection connection = getConnection();

    private static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/paypal",
                    "root",
                    ""
            );

            System.out.println("Connection successful");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static int createUser(String firstName, String lastName) {
        String sql = "insert into users (first_name, last_name) values (?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,firstName);
            preparedStatement.setString(2,lastName);
            preparedStatement.executeUpdate();

            String idSql = "select max(id) from users";
            Statement idStatement = connection.createStatement();
            ResultSet resultSet = idStatement.executeQuery(idSql);

            resultSet.next();

            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Updates the user balance in database
     * Sets balance = balance + amount
     *
     * @param userId id of the user in users table
     * @param amount double value of the amount to insert
     */
    static void cashFlow(int userId, double amount) throws SQLException {

        if(check(userId, -amount)) {
            String sql = "UPDATE users SET balance = balance+? where id =?";
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);

            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, userId);

            preparedStatement.executeUpdate();
            System.out.println("Cash transfer successful");
        }else
            {
                System.out.println("User not found or transfer amount is invalid");

        }
    }

    /**
     * Emulates a transaction between 2 users
     * Takes money from one account and adds to another account
     *
     * @param userFrom source user id
     * @param userTo   target user id
     * @param amount   transaction amount
     */
    static void transaction(int userFrom, int userTo, double amount) {
        try {
            connection.setAutoCommit(false);

            if(!check(userFrom, amount)) throw new SQLException("Invalid User ID");

            String query1 = "UPDATE users SET balance = balance-? where id =?";
            PreparedStatement preparedStatement1 =
                    connection.prepareStatement(query1);

            preparedStatement1.setDouble(1, amount);
            preparedStatement1.setInt(2, userFrom);

            preparedStatement1.executeUpdate();

            if(!check(userTo,0)) throw new SQLException("Invalid User ID");

            String query2 = "UPDATE users SET balance = balance+? where id =?";
            PreparedStatement preparedStatement2 =
                    connection.prepareStatement(query2);

            preparedStatement2.setDouble(1, amount);
            preparedStatement2.setInt(2, userTo);

            preparedStatement2.executeUpdate();
            connection.commit();
            System.out.println("Transaction successful");
        }
        catch (SQLException  e){
            try {
                System.out.println("Transaction Faild:Incorect User id or balance");
                connection.rollback();
            } catch ( SQLException e1 ) {
                e1.printStackTrace();
            }
        }


    }



    private static boolean check(int userid,double amount) throws SQLException {
        String query = "Select balance from users where id =?";

        PreparedStatement preparedstaement = connection.prepareStatement(query);
        preparedstaement.setInt(1,userid);
        preparedstaement.execute();

        ResultSet result = preparedstaement.executeQuery();
        if(result.next() && amount < result.getDouble("balance")) {
            return true;
        }
        else{
            return false;
        }
    }

    static List<User> listUsers() {
        String sql = "select * from users";

        try {
            Statement statement = connection.createStatement();
            System.out.println("conceted");
            ResultSet resultSet = statement.executeQuery(sql);

            List<User> userList = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                double balance = resultSet.getDouble("balance");

                userList.add(new User(
                        id, firstName, lastName, balance
                ));
            }
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
