package hr.java.projektnizadatak.application;

import java.time.LocalDate;

public class Util {
	public static int getAcademicYear(LocalDate date) {
		return date.getMonthValue() >= 10
			? date.getYear()
			: date.getYear() - 1;
	}
}
