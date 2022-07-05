package io.github.viati.varti.events;

public class Event {
  private static String id;
  private final boolean cancellable;
  private boolean cancel = false;

  public Event(String id) {
    this(id, false);
  }

  public Event(String id, boolean cancellable) {
    this.id = id;
    this.cancellable = cancellable;
  }

  public String getId() {
    return id;
  }

  public boolean cancel() {
    if (cancellable) {
      cancel = !cancel;
      return cancel;
    }
    return false;
  }

  public boolean isCancelled() {
    return cancellable && cancel;
  }

  public boolean isCancellable() {
    return cancellable;
  }
}
