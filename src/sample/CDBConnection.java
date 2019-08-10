package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CDBConnection {

    public static Connection connect() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:classesData.db");
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS classes(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT,num INTEGER)");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}
