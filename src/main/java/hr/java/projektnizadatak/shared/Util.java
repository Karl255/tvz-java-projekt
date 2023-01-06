package hr.java.projektnizadatak.shared;

import java.time.LocalTime;

public class Util {
	public static double toHours(LocalTime time) {
		return time.getHour() + time.getMinute() / 60.0;
	}
	
	public static boolean isBetween(LocalTime t, LocalTime start, LocalTime end) {
		return t.compareTo(start) >= 0 && t.compareTo(end) < 0;
	}
}
