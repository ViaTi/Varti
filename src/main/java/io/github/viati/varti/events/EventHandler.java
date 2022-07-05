package io.github.viati.varti.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/** The type Event handler. */
public class EventHandler {
  private final Map<String, ArrayList<EventWrapper>> eventMap = new HashMap<>();
  private final Map<Method, EventWrapper> methodWrappers = new HashMap<>();
  private final Map<Object, ArrayList<EventWrapper>> disabled = new HashMap<>();
  private final ConditionHandler conditionHandler;

  /** Instantiates a new Event handler. */
  public EventHandler() {
    this.conditionHandler = new ConditionHandler();
  }

  /**
   * Instantiates a new Event handler.
   *
   * @param conditionHandler the condition handler
   */
  public EventHandler(ConditionHandler conditionHandler) {
    this.conditionHandler = conditionHandler;
  }

  /**
   * Assigns an objects methods to events as-long as it is annotated with @EventListener.
   *
   * @param object an event listener.
   */
  public void add(Object object) {
    if (!conditionHandler.shouldAdd(object)
        || !object.getClass().isAnnotationPresent(io.github.viati.varti.events.EventListener.class))
      return;
    for (Method method : object.getClass().getDeclaredMethods()) {
      if (!method.isAnnotationPresent(EventDefiner.class)) continue;
      EventDefiner eventDefiner = method.getAnnotation(EventDefiner.class);
      if (eventDefiner.id().equals("none")) {
        Optional<Class<?>> param = Arrays.stream(method.getParameterTypes()).findFirst();
        if (param.isEmpty()) return;
        String name = param.get().getSimpleName();
        mkEventMap(name);
        methodWrappers.put(
            method,
            new EventWrapper(
                object,
                eventDefiner,
                method,
                Arrays.stream(method.getParameterTypes()).findFirst().isPresent()));
        methodWrappers.get(method).setId(name);
        eventMap.get(name).add(methodWrappers.get(method));
      } else {
        mkEventMap(eventDefiner.id());
        methodWrappers.put(
            method,
            new EventWrapper(
                object,
                eventDefiner,
                method,
                Arrays.stream(method.getParameterTypes()).findFirst().isPresent()));
        methodWrappers.get(method).setId(eventDefiner.id());
        eventMap.get(eventDefiner.id()).add(methodWrappers.get(method));
      }
    }
  }

  /**
   * Invoke methods with this event, will also pause them if needed.
   *
   * @param event the event
   * @return successful calls
   */
  public int call(Event event) {
    String className = event.getClass().getSimpleName();
    if (!eventMap.containsKey(className) && !eventMap.containsKey(event.getId())) return -1;

    int i = 0;

    if (eventMap.containsKey(className)) {
      mkEventMap(event.getId());
      eventMap.get(event.getId()).forEach(eventWrapper -> eventWrapper.setId(event.getId()));
      eventMap.get(event.getId()).addAll(eventMap.get(className));
      eventMap.remove(className);
    }

    for (EventWrapper wrapper : eventMap.get(event.getId())) {
      if (conditionHandler.toPause(wrapper) && wrapper.getEvent().canPause()) continue;
      try {
        if (wrapper.hasEventArg()) wrapper.getMethod().invoke(wrapper.getObject(), event);
        else wrapper.getMethod().invoke(wrapper.getObject());
        i++;
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      if (event.isCancelled() && wrapper.getEvent().breakOnCancel()) break;
    }

    return i;
  }

  /**
   * Disable an objects event methods.
   *
   * @param object the object
   */
  public synchronized void disable(Object object) {
    if (!object.getClass().isAnnotationPresent(io.github.viati.varti.events.EventListener.class)
        || disabled.containsKey(object)) return;
    disabled.put(object, new ArrayList<>());
    for (Method method : object.getClass().getDeclaredMethods()) {
      if (!method.isAnnotationPresent(EventDefiner.class)) continue;
      disabled.get(object).add(methodWrappers.get(method));
    }

    for (String wrappers : eventMap.keySet()) {
      eventMap.get(wrappers).removeIf(wrapper -> disabled.get(object).contains(wrapper));
    }
  }

  /**
   * Re-enable an objects event methods.
   *
   * @param object the object
   */
  public synchronized void enable(Object object) {
    if (!object.getClass().isAnnotationPresent(EventListener.class)
        || !disabled.containsKey(object)) return;
    for (EventWrapper wrapper : disabled.get(object)) {
      mkEventMap(wrapper.id());
      eventMap.get(wrapper.id()).add(wrapper);
    }
    disabled.remove(object);
  }

  public synchronized void remove(Object object) {
    disabled.remove(object);

    for (Method method : object.getClass().getDeclaredMethods()) {
      eventMap.get(methodWrappers.get(method).id()).remove(methodWrappers.get(method));
    }
  }

  private void mkEventMap(String event) {
    if (!eventMap.containsKey(event)) eventMap.put(event, new ArrayList<>());
  }
}
