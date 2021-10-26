package eventmanager;

import dbmanager.EventDBManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import jsonmanager.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventOperations {

  private ArrayList<Event> events;
  private static Logger logger = LogManager.getLogger(EventDBManager.class);

  public EventOperations(ArrayList<Event> events) {
    this.events = events;
  }

  public ArrayList<Event> getStartEvents() {
    ArrayList<Event> startEvents;
    startEvents = (ArrayList<Event>) events.stream()
        .filter(event -> event.getState().equalsIgnoreCase("STARTED"))
        .collect(Collectors.toList());
    return startEvents;
  }

  public ArrayList<Event> getFinishEvents() {
    ArrayList<Event> finishEvents;
    finishEvents = (ArrayList<Event>) events.stream()
        .filter(event -> event.getState().equalsIgnoreCase("FINISHED"))
        .collect(Collectors.toList());
    return finishEvents;
  }

  public ArrayList<Event> getLongEvents() {
    ArrayList<Event> startEvents = getStartEvents();
    ArrayList<Event> finishEvents = getFinishEvents();
    ArrayList<Event> longEvents = new ArrayList<Event>();
    for (Event event : startEvents) {
      Event finishEvent = (Event) finishEvents.stream()
          .filter(e -> e.getId().equals(event.getId()))
          .collect(toSingleton());
      long diff = finishEvent.getTimestamp().getTime() - event.getTimestamp().getTime();
      if (diff > 4) {
        longEvents.add(new Event(event.getId(), event.getType(), event.getHost(), diff, true));
      } else {
        longEvents.add(new Event(event.getId(), event.getType(), event.getHost(), diff, false));
      }
    }
    logger.info(String.format("Flagged events filtered %s", longEvents));
    return longEvents;
  }

  public static <T> Collector<T, ?, T> toSingleton() {
    return Collectors.collectingAndThen(
        Collectors.toList(),
        list -> {
          if (list.size() != 1) {
            throw new IllegalStateException();
          }
          return list.get(0);
        }
    );
  }
}
