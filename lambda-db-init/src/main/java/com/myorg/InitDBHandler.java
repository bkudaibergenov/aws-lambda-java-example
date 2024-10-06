package com.myorg;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;

public class InitDBHandler implements RequestHandler<Map<String, String>, String> {

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

            // SQL-запрос для создания таблицы, если она не существует
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id SERIAL PRIMARY KEY, "
                    + "name VARCHAR(100), "
                    + "surname VARCHAR(100))";

            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);

            return "Table 'users' created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating table: " + e.getMessage();
        }
    }
}
