package io.github.viati.varti;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public class Fs {
  private static final String separator = System.getProperty("path.separator");

  public static void write(String path, String value) {
    File file = new File(path);
    mkdir(file.getAbsolutePath());
    try {
      FileOutputStream fos = new FileOutputStream(file);
      DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
      outStream.writeBytes(value);
      outStream.close();

      FileInputStream fis = new FileInputStream(file);
      DataInputStream reader = new DataInputStream(fis);
      if (!Arrays.equals(reader.readAllBytes(), value.getBytes())) {
        System.out.println(
            "The content written to: " + file.getAbsolutePath() + " was invalid, please check it.");
        file.deleteOnExit();
      }
      reader.close();
    } catch (IOException e) {
      if (file.exists()) {
        file.deleteOnExit();
        System.out.println(
            "Write attempt of: " + file.getAbsolutePath() + " was invalid, will be deleted.");
      } else {
        System.out.println(
            "Write attempt of: " + file.getAbsolutePath() + " was invalid, wasn't written.");
      }
    }
  }

  public static String read(String path) {
    File file = new File(path);
    if (file.isDirectory()) {
      System.out.println(
          "The file at: "
              + file.getAbsolutePath()
              + " was attempted to be read and is a directory.");
      return "";
    }
    try {
      return new String(Files.readAllBytes(file.toPath()));
    } catch (IOException e) {
      return "";
    }
  }

  public static boolean exists(String path) {
    return new File(path).exists();
  }

  public static void mkdir(String path) {
    String newPath = path;
    String[] splitPath = newPath.split(separator);
    if (splitPath[splitPath.length - 1].contains(".")) {
      newPath = new File(newPath).getParent();
    }
    new File(newPath).mkdirs();
  }
}
