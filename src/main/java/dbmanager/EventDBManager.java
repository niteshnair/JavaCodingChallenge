package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import jsonmanager.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventDBManager {

  private final Connection dbConn;
  private static Logger logger = LogManager.getLogger(EventDBManager.class);

  public EventDBManager(Connection dbConn) throws SQLException, ClassNotFoundException {
    this.dbConn = dbConn;
    createEventsTable();
  }

  private void createEventsTable() throws SQLException {
    String createEventTableQuery =
        "CREATE TABLE IF NOT EXISTS EVENTS (ID VARCHAR(50), TYPE VARCHAR(50),"
            + " HOST VARCHAR(50), DURATION INTEGER, ALERT VARCHAR(20))";
    this.dbConn.createStatement().executeUpdate(createEventTableQuery);
    logger.info("EVENTS TABLE created if not exists in db");
  }

  public void insertEventEntries(ArrayList<Event> events) throws SQLException {
    this.dbConn.setAutoCommit(false);
    int cnt = 0;
    String insertQuery = "INSERT INTO EVENTS (id, type, host, duration, alert) VALUES(?,?,?,?,?) ";
    PreparedStatement insertStatement = this.dbConn.prepareStatement(insertQuery);
    for (Event event : events) {
      insertStatement.setString(1, event.getId());
      insertStatement.setString(2, event.getType());
      insertStatement.setString(3, event.getHost());
      insertStatement.setLong(4, event.getDuration());
      insertStatement.setString(5, event.getAlert());
      insertStatement.addBatch();
      cnt++;
      if (cnt % 100 == 0) {
        insertStatement.executeBatch();
        logger.info("Batch insert statements executed");
        cnt = 0;
      }
    }
    insertStatement.executeBatch();
    dbConn.commit();
  }

  public ArrayList<Event> getEventsFromTable() throws SQLException {
    String selectQuery = "SELECT * FROM EVENTS";
    ArrayList<Event> selectEvents = new ArrayList<Event>();
    Statement selectStatement = this.dbConn.createStatement();
    ResultSet rs = selectStatement.executeQuery(selectQuery);
    while (rs.next()) {
      selectEvents.add(new Event(rs));
    }
    selectStatement.close();
    logger.info(String.format("Total Number of Records in EVENTS table: %d", selectEvents.size()));
    logger.info(String.format("Data read from EVENTS table using query:\n %s", selectQuery));
    return selectEvents;
  }
}
