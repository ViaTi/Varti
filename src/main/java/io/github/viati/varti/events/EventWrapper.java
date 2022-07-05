package io.github.viati.varti.events;

import java.lang.reflect.Method;

public class EventWrapper {
  private final Object object;
  private final EventDefiner event;
  private final Method method;
  private final boolean hasEventArg;
  private String id;

  public EventWrapper(Object object, EventDefiner event, Method method) {
    this.object = object;
    this.event = event;
    this.method = method;
    this.hasEventArg = true;
  }

  public EventWrapper(Object object, EventDefiner event, Method method, boolean hasEventArg) {
    this.object = object;
    this.event = event;
    this.method = method;
    this.hasEventArg = hasEventArg;
  }

  public String id() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean hasEventArg() {
    return hasEventArg;
  }

  public Object getObject() {
    return object;
  }

  public EventDefiner getEvent() {
    return event;
  }

  public Method getMethod() {
    return method;
  }
}
