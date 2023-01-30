package hr.java.projektnizadatak.shared;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.function.Function;
import java.util.function.Supplier;

public class Util {
	public static double toHours(LocalTime time) {
		return time.getHour() + time.getMinute() / 60.0;
	}
	
	public static boolean isBetween(LocalTime t, LocalTime start, LocalTime end) {
		return t.compareTo(start) >= 0 && t.compareTo(end) < 0;
	}
	
	public static <T> T unlessNull(T value, T def) {
		return value != null
			? value
			: def;
	}
	
	public static <T> T unlessNullGet(T value, Supplier<T> def) {
		return value != null
			? value
			: def.get();
	}
	
	public static <T, R> R nullCoalesc(T value, Function<T, R> mapper) {
		return value != null ? mapper.apply(value) : null;
	}

	public static String dayOfWeekToShortString(DayOfWeek dow) {
		return switch (dow) {
			case MONDAY -> "Pon";
			case TUESDAY -> "Uto";
			case WEDNESDAY -> "Sri";
			case THURSDAY -> "Čet";
			case FRIDAY -> "Pet";
			case SATURDAY -> "Sub";
			case SUNDAY -> "Ned";
		};
	}
}
