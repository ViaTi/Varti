package io.github.viati.varti;

import io.github.viati.varti.events.Event;
import io.github.viati.varti.events.EventDefiner;
import io.github.viati.varti.events.EventHandler;
import io.github.viati.varti.events.EventListener;
import org.junit.jupiter.api.Test;

public class TestVarti {

  @Test
  void humanifyTest() {
    String testResult = "pretty pretty text so cool wow";
    String testResult2 = Prettify.humanify(testResult);
    assert !testResult.equals(testResult2) : "Humanify returned an unmodified value.";
  }

  @Test
  void eventHandlerDual() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.add(new EventReception());
    eventHandler.add(new EventReceptionV2());
    int i = eventHandler.call(new EventTest());
    assert i == 2 : i + " call(s) were made 2 were expected.";
  }

  @Test
  void eventHandlerArg() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.add(new EventReception());
    int i = eventHandler.call(new EventTest());
    assert i == 1 : i + " call(s) were made 1 was expected.";
  }

  @Test
  void eventHandlerId() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.add(new EventReception());
    int i = eventHandler.call(new EventTest());
    assert i == 1 : i + " call(s) were made 1 was expected.";
  }

  @Test
  void eventHandlerEnable() {
    EventHandler eventHandler = new EventHandler();
    EventReception reception = new EventReception();
    eventHandler.add(reception);
    eventHandler.disable(reception);
    eventHandler.enable(reception);
    int i = eventHandler.call(new EventTest());
    assert i == 1 : "Event failed to re-enable, calls made: " + i + ".";
  }

  @Test
  void eventHandlerDisable() {
    EventHandler eventHandler = new EventHandler();
    EventReception reception = new EventReception();
    eventHandler.add(reception);
    eventHandler.disable(reception);
    int i = eventHandler.call(new EventTest());
    assert i == 0 : "Event failed to disable.";
  }

  @Test
  void eventHandlerRemove() {
    EventHandler eventHandler = new EventHandler();
    EventReception reception = new EventReception();
    eventHandler.add(reception);
    eventHandler.remove(reception);
    int i = eventHandler.call(new EventTest());
    assert i == 0 : "Event failed to be removed.";
  }

  static class EventTest extends Event {

    public EventTest() {
      super("test", true);
    }
  }

  @EventListener(id = "reception")
  public class EventReception {
    @EventDefiner
    public void testEvent(EventTest ignoredEventTest) {}
  }

  @EventListener(id = "reception")
  public class EventReceptionV2 {
    @EventDefiner(id = "test")
    public void testEvent() {}
  }
}
