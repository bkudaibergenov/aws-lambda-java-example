package kz.bkudaibergenov.aws.examples.lambda;

import kz.bkudaibergenov.aws.sidecar.lambda.annotation.LambdaFunction;
import kz.bkudaibergenov.aws.sidecar.lambda.annotation.QueryParam;
import kz.bkudaibergenov.aws.sidecar.lambda.handler.LambdaFunctionHandler;
import kz.bkudaibergenov.aws.sidecar.lambda.model.ResponseEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class LambdaPostgresql implements LambdaFunctionHandler {

    private final String jdbcUrl = "jdbc:postgresql://infrastructurestack-postgresrdsinstanced2aa3e22-l5wy6opxfgu1.chsusa4a8rbq.eu-central-1.rds.amazonaws.com:5432/mydatabase";
    private final String username = "lambda_user";
    private final String password = "3d38e9577ef44836bc447c4c19ae514f";

    @LambdaFunction()
    public ResponseEntity<?> execute(@QueryParam("name") String name, @QueryParam("age") Integer age) {

        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);

            String createTableSQL = "CREATE TABLE IF NOT EXISTS test_table (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "age INT)";
            stmt = conn.createStatement();
            stmt.execute(createTableSQL);

            String insertSQL = "INSERT INTO test_table (name, age) VALUES (?, ?)";
            pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return ResponseEntity.builder()
                .statusCode(200)
                .build();
    }
}
