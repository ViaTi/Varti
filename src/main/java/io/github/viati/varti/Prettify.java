package io.github.viati.varti;

import java.util.Locale;

public class Prettify {
  public static String humanify(String s) {
    if (s == null || s.equals("")) return "";
    String newString = s;
    String firstChar = String.valueOf(newString.charAt(0));
    String lastChar = String.valueOf(newString.charAt(newString.length() - 1));

    if (canLowercase(firstChar)) {
      newString = newString.replaceFirst(firstChar, firstChar.toUpperCase(Locale.ROOT));
    }

    if (newString.contains("_")) {
      newString = newString.replaceAll("_", " ");
    }

    if (newString.split(" ").length >= 2 && canLowercase(lastChar)) {
      newString += ".";
    }

    return newString;
  }

  public static boolean canLowercase(String s) {
    return !s.toLowerCase().equals(s.toUpperCase());
  }

  public static boolean canLowercase(char s) {
    return !String.valueOf(s).toLowerCase().equals(String.valueOf(s).toUpperCase());
  }
}
