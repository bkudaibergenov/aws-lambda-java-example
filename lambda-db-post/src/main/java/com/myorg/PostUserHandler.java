package com.myorg;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Map;

public class PostUserHandler implements RequestHandler<Map<String, String>, String> {

    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        // Получаем параметры для подключения к базе данных из переменных окружения
        String dbHost = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        // JDBC URL для PostgreSQL
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {

            // Получаем данные из входного события Lambda (event)
            String name = event.get("name");
            String surname = event.get("surname");

            // SQL-запрос для добавления нового пользователя
            String insertSQL = "INSERT INTO users (name, surname) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.executeUpdate();

            return "User added successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding user: " + e.getMessage();
        }
    }
}
