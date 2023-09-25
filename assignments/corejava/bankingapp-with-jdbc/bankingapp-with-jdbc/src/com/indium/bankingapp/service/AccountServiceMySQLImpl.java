package com.indium.bankingapp.service;

import com.indium.bankingapp.model.Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountServiceMySQLImpl implements AccountService<Account> {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankingapp";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "DBPassword";

    //Defining SQL queries here
    private static final String CREATE_ACCOUNT_SQL = "INSERT INTO accounts (id, name, balance, roi, account_type) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_ACCOUNTS_SQL = "SELECT * FROM accounts";
    private static final String GET_ACCOUNT_BY_ID_SQL = "SELECT * FROM accounts WHERE id = ?";
    private static final String UPDATE_ACCOUNT_SQL = "UPDATE accounts SET name = ?, balance = ?, roi = ?, account_type = ? WHERE id = ?";
    private static final String DELETE_ACCOUNT_SQL = "DELETE FROM accounts WHERE id = ?";

    @Override
    public boolean createAccount(Account account) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ACCOUNT_SQL)) {
            preparedStatement.setInt(1, account.getId());
            preparedStatement.setString(2, account.getName());
            preparedStatement.setDouble(3, account.getBalance());
            preparedStatement.setDouble(4, account.getRoi());
            preparedStatement.setString(5, account.getAccountType());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ACCOUNTS_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double balance = resultSet.getDouble("balance");
                double roi = resultSet.getDouble("roi");
                String accountType = resultSet.getString("account_type");

                Account account = new Account(id, name, balance, roi, accountType);
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    @Override
    public Account getAccount(int id) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                double balance = resultSet.getDouble("balance");
                double roi = resultSet.getDouble("roi");
                String accountType = resultSet.getString("account_type");

                return new Account(id, name, balance, roi, accountType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean updateAccount(int id, Account account) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT_SQL)) {
            preparedStatement.setString(1, account.getName());
            preparedStatement.setDouble(2, account.getBalance());
            preparedStatement.setDouble(3, account.getRoi());
            preparedStatement.setString(4, account.getAccountType());
            preparedStatement.setInt(5, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAccount(int id) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ACCOUNT_SQL)) {
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}