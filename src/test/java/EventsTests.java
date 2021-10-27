import dbmanager.DbConnector;
import dbmanager.EventDBManager;
import eventmanager.EventOperations;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import jsonmanager.Event;
import jsonmanager.EventJsonParser;
import logreader.LogReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventsTests {

  private static Connection dbConn = null;
  private static String dbPath = "D:\\CodingChallenge\\JavaCodingChallenge\\src\\main\\resources\\HsqlFileDb";
  private static String dbName = "eventsdb";
  private static final Logger logger = LogManager.getLogger(LogReader.class);

  @BeforeClass
  public static void setUp() {
    try {
      dbConn = new DbConnector(dbPath, dbName).getHsqlDbConnection();
    } catch (SQLException | ClassNotFoundException se) {
      logger.error(se.getStackTrace());
    }
  }

  @AfterClass
  public static void tearDown() {
    try {
      if (dbConn != null) {
        dbConn.close();
      }
    } catch (SQLException se) {
      logger.error(se.getStackTrace());
    }
  }

  @Test
  public void readLogFile() {
    try {
      EventJsonParser parser = new EventJsonParser(
          "D:\\CodingChallenge\\JavaCodingChallenge\\src\\main\\resources\\logfile.txt");
      ArrayList<Event> events = parser.readFileToJsonObject();
      assert (events.size() > 0);
    } catch (IOException | ParseException ae) {
      logger.error(ae.getStackTrace());
    }
  }

  @Test
  public void findLongestEvent() {
    try {
      EventJsonParser parser = new EventJsonParser(
          "D:\\CodingChallenge\\JavaCodingChallenge\\src\\main\\resources\\logfile.txt");
      ArrayList<Event> events = parser.readFileToJsonObject();
      EventOperations eventOperations = new EventOperations(events);
      ArrayList<Event> longEvents = eventOperations.getLongEvents();
      assert (longEvents.size() > 0);
    } catch (IOException | ParseException ae) {
      logger.error(ae.getStackTrace());
    }
  }

  @Test
  public void insertDataIntoHsqlDb() {
    try {
      EventJsonParser parser = new EventJsonParser(
          "D:\\CodingChallenge\\JavaCodingChallenge\\src\\main\\resources\\logfile.txt");
      ArrayList<Event> events = parser.readFileToJsonObject();
      EventOperations eventOperations = new EventOperations(events);
      ArrayList<Event> longEvents = eventOperations.getLongEvents();
      EventDBManager eventDBManager = new EventDBManager(dbConn);
      eventDBManager.insertEventEntries(longEvents);
      assert events.size() > 0;
    } catch (IOException | ParseException | SQLException | ClassNotFoundException ae) {
      logger.error(ae.getStackTrace());
    }
  }

  @Test
  public void selectDataIntoHsqlDb() {
    try {
      EventDBManager eventDBManager = new EventDBManager(dbConn);
      ArrayList<Event> selectEvents = eventDBManager.getEventsFromTable();
      assert selectEvents.size() > 0;
    } catch (SQLException | ClassNotFoundException ae) {
      logger.error(ae.getStackTrace());
    }
  }
}
