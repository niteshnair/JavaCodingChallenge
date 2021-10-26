package dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DbConnector {

  private static Logger logger = LogManager.getLogger(DbConnector.class);

  private String connectionString;

  public DbConnector(String dbPath, String databaseName) {
    this.connectionString = String.format("jdbc:hsqldb:file:%s/%s", dbPath, databaseName);
  }

  public Connection getHsqlDbConnection() throws ClassNotFoundException, SQLException {
    Class.forName("org.hsqldb.jdbc.JDBCDriver");
    logger.info("HSQLDB Connection created successfully");
    return DriverManager.getConnection(this.connectionString, "SA", "");
  }

}
