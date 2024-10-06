package com.myorg;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetUserHandler implements RequestHandler<Map<String, String>, List<Map<String, String>>> {

    @Override
    public List<Map<String, String>> handleRequest(Map<String, String> event, Context context) {
        // Получаем параметры для подключения к базе данных из переменных окружения
        String dbHost = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        // JDBC URL для PostgreSQL
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);

        List<Map<String, String>> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {

            // SQL-запрос для получения всех пользователей
            String query = "SELECT id, name, surname FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Проходим по результатам и добавляем каждого пользователя в список
            while (resultSet.next()) {
                users.add(Map.of(
                        "id", String.valueOf(resultSet.getInt("id")),
                        "name", resultSet.getString("name"),
                        "surname", resultSet.getString("surname")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }
}
