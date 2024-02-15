package ru.dankoy.tcoubsinitiator.core.service.utils;

public class Utils {

  private Utils() {}

  public static void sleep(long millis) {

    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Interrupted while trying to get coubs", e);
    }
  }
}
