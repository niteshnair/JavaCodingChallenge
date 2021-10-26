package jsonmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import logreader.LogReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EventJsonParser {

  private static final Logger logger = LogManager.getLogger(LogReader.class);
  public String filePath;


  public EventJsonParser(String filePath) {
    this.filePath = filePath;
  }

  public ArrayList<Event> readTextFileToJsonObject() throws IOException, ParseException {
    ArrayList<Event> events = new ArrayList<Event>();
    try (FileInputStream ins = new FileInputStream(this.filePath); Scanner sc = new Scanner(ins,
        "UTF-8")) {
      while (sc.hasNextLine()) {
        String line = sc.nextLine();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(line);
        Event event = new Event((String) obj.get("id"), (String) obj.get("state"),
            new Timestamp((long) obj.get("timestamp")), (String) obj.get("type"),
            (String) obj.get("host"));
        events.add(event);
      }
      return events;
    }
  }

  public ArrayList<Event> readFileToJsonObject() throws IOException, ParseException {
    logger.info(String.format("Reading log file from: %s", this.filePath));
    try {
      ObjectMapper mapper = new ObjectMapper();
      ArrayList<Event> events;
      events = (ArrayList<Event>) Arrays
          .asList(mapper.readValue(Paths.get(this.filePath).toFile(), Event[].class));
      return events;
    } catch (MismatchedInputException ex) {
      return readTextFileToJsonObject();
    }
  }
}
