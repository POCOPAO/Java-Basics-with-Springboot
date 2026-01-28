package com.bpi.training.jdbc_maven;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DBConnection {


    private static final Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);

        // Replace these with your actual database details
        private static String url = "jdbc:postgresql://localhost:5432/training_db";
        private static String username = "pdnaranjo";
        private static String password = "postgres";
        

        static {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                LOGGER.error("PostgreSQL JDBC driver not found on classpath.", e);
            }
            

try {
            java.util.logging.LogManager.getLogManager().reset();
            org.slf4j.bridge.SLF4JBridgeHandler.removeHandlersForRootLogger();
            org.slf4j.bridge.SLF4JBridgeHandler.install();
        } catch (Throwable t) {
            LOGGER.warn("Failed to install JUL bridge (continuing): {}", t.toString());
        }
    }

        
        
        

public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
        
    

