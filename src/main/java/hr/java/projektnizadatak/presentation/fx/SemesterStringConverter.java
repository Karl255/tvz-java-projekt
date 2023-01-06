package hr.java.projektnizadatak.presentation.fx;

import hr.java.projektnizadatak.application.entities.Semester;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SemesterStringConverter extends StringConverter<Semester> {
	private static final Logger logger = LoggerFactory.getLogger(Semester.class);

	@Override
	public String toString(Semester semester) {
		return semester != null
			? semester.semester() + " - " + semester.subdepartment()
			: "<select>";
	}

	@Override
	public Semester fromString(String s) {
		String m = "Cannot create Semester object from String";
		logger.error(m);

		throw new UnsupportedOperationException(m);
	}
}
