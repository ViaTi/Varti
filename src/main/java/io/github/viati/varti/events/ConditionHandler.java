package io.github.viati.varti.events;

public class ConditionHandler {
  public boolean toPause(EventWrapper wrapper) {
    return false;
  }

  public boolean shouldAdd(Object object) {
    return true;
  }
}
