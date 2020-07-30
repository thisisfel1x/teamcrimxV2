package de.fel1x.teamcrimx.crimxapi.utils;

public class TimeUtils {

    public static final long SECS = 1000;
    public static final long MINS = SECS * 60;
    public static final long HOURS = MINS * 60;
    public static final long DAYS = HOURS * 24;

    public static long[] splitTime(long millis) {
        long[] result = new long[5];
        long rest = millis;
        result[0] = rest / DAYS;
        rest = rest - result[0] * DAYS;
        result[1] = rest / HOURS;
        rest = rest - result[1] * HOURS;
        result[2] = rest / MINS;
        rest = rest - result[2] * MINS;
        result[3] = rest / SECS;
        rest = rest - result[3] * SECS;
        result[4] = rest;
        return result;
    }

    public static String getFormattedTime(long millis) {
        long[] split = splitTime(millis);
        if (split[0] == 0 && split[1] != 0) {
            return "" + (split[1] == 1 ? split[1] + " Stunde " : split[1] + " Stunden ")
                    + (split[2] == 1 ? split[2] + " Minute " : split[2] + " Minuten ")
                    + (split[3] == 1 ? split[3] + " Sekunde" : split[3] + " Sekunden");
        }
        if (split[0] == 0 && split[2] != 0) {
            return "" + (split[2] == 1 ? split[2] + " Minute " : split[2] + " Minuten ")
                    + (split[3] == 1 ? split[3] + " Sekunde" : split[3] + " Sekunden");
        }
        if (split[0] == 0 && split[3] != 0) {
            return "" + (split[3] == 1 ? split[3] + " Sekunde" : split[3] + " Sekunden");
        }
        return "" + (split[0] == 1 ? split[0] + " Tag " : split[0] + " Tage ")
                + (split[1] == 1 ? split[1] + " Stunde " : split[1] + " Stunden ")
                + (split[2] == 1 ? split[2] + " Minute " : split[2] + " Minuten ")
                + (split[3] == 1 ? split[3] + " Sekunde" : split[3] + " Sekunden");

    }

}
