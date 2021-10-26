package logreader;

import dbmanager.DbConnector;
import dbmanager.EventDBManager;
import eventmanager.EventOperations;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import jsonmanager.Event;
import jsonmanager.EventJsonParser;

public class LogReader {

  private static final Logger logger = LogManager.getLogger(LogReader.class);
  private static String dbPath = "D:\\CodingChallenge\\JavaCodingChallenge\\src\\main\\resources\\HsqlFileDb";
  private static String dbName = "eventsdb";
  static Connection dbConn = null;

  public static void main(String[] args) throws SQLException {
    logger.info("Execution for Log Reader started");
    if (args.length > 0) {
      String logFilePath = args[0].trim();
      if (args[1].trim().length() > 0) {
        dbPath = args[1].trim();
        logger.info(String.format("Using Database Path from argument: %s", dbPath));
      } else {
        logger.info(String.format("Using Default Database Path: %s", dbPath));
      }
      if (args[2].trim().length() > 0) {
        dbName = args[2].trim();
        logger.info(String.format("Using Database Name from argument: %s", dbName));
      } else {
        logger.info(String.format("Using Default Database Name: %s", dbName));
      }
      EventJsonParser parser = new EventJsonParser(logFilePath);
      try {
        ArrayList<Event> events = parser.readFileToJsonObject();
        EventOperations eventOperations = new EventOperations(events);
        ArrayList<Event> longEvents = eventOperations.getLongEvents();
        dbConn = new DbConnector(dbPath, dbName).getHsqlDbConnection();
        EventDBManager eventDBManager = new EventDBManager(dbConn);
        eventDBManager.insertEventEntries(longEvents);
        ArrayList<Event> selectEvents = eventDBManager.getEventsFromTable();
        selectEvents.forEach(System.out::println);
      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        if (dbConn != null) {
          dbConn.close();
        }
      }
    } else {
      logger.info("Kindly provide log file path to parse as command line argument");
    }
  }
}
