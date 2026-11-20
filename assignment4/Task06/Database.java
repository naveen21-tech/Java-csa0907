import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** JDBC connection factory. Configure DB_URL, DB_USER and DB_PASSWORD environment variables. */
public final class Database {
    private Database() {}
    public static Connection getConnection() throws SQLException {
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/campus_events");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }
}
