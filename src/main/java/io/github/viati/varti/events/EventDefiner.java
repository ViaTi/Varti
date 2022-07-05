package io.github.viati.varti.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface EventDefiner {
  String id() default "none";

  boolean canPause() default true;

  boolean breakOnCancel() default false;

  int priority() default 5;
}
