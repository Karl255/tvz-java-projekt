package hr.java.projektnizadatak.presentation.util;

import hr.java.projektnizadatak.application.entities.Department;
import hr.java.projektnizadatak.application.entities.Semester;
import javafx.util.StringConverter;

public class SemesterStringConverter extends StringConverter<Semester> {
	@Override
	public String toString(Semester semester) {
		return semester != null
			? semester.semester() + " - " + semester.subdepartment()
			: "<select>";
	}

	@Override
	public Semester fromString(String s) {
		// TODO: add message or use custom exception
		throw new UnsupportedOperationException("");
	}
}
