package hr.java.projektnizadatak.presentation.fx;

import hr.java.projektnizadatak.application.entities.Department;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepartmentStringConverter extends StringConverter<Department> {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentStringConverter.class);

	@Override
	public String toString(Department department) {
		return department != null ? department.code() : "<select>";
	}

	@Override
	public Department fromString(String s) {
		String m = "Cannot create Semester object from String";
		logger.error(m);

		throw new UnsupportedOperationException(m);
	}
}
