package jsonmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Event {

  private String id;
  private String state;
  private Timestamp timestamp;
  private String type;
  private long duration;
  private boolean alert;
  private String host;

  public Event(String id, String state, Timestamp timestamp, String type, String host) {
    this.id = id;
    this.state = state;
    this.timestamp = timestamp;
    this.type = type;
    this.host = host;
  }

  public Event(String id, String type, String host, long duration, boolean alert) {
    this.id = id;
    this.type = type;
    this.host = host;
    this.duration = duration;
    this.alert = alert;
  }

  public Event(ResultSet rs) throws SQLException {
    this.id = rs.getString("ID");
    this.type = rs.getString("TYPE");
    this.host = rs.getString("HOST");
    this.duration = rs.getLong("DURATION");
    String alertVal = rs.getString("ALERT");
    this.alert = alertVal.equals("true");
  }

  public Event() {
  }

  public String getAlert() {
    if (this.alert) {
      return "true";
    } else {
      return "false";
    }
  }

  public void setAlert(boolean alert) {
    this.alert = alert;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getId() {
    return id;
  }

  public String getState() {
    return state;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public String getType() {
    return type;
  }

  public String getHost() {
    return host;
  }

  @Override
  public String toString() {
    return "Event{" +
        "id='" + id + '\'' +
        ", state='" + state + '\'' +
        ", timestamp=" + timestamp +
        ", type='" + type + '\'' +
        ", duration=" + duration +
        ", alert=" + alert +
        ", host='" + host + '\'' +
        '}';
  }
}
