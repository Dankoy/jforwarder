package ru.dankoy.coubtagssearcher.core.service;

public class Utils {

    public static void sleep(long millis) {

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while trying to get search coub.com", e);
        }
    }
}
