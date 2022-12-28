package hr.java.projektnizadatak.presentation.util;

import hr.java.projektnizadatak.application.entities.Department;
import javafx.util.StringConverter;

public class DepartmentStringConverter extends StringConverter<Department> {
	@Override
	public String toString(Department department) {
		return department != null ? department.code() : "<select>";
	}

	@Override
	public Department fromString(String s) {
		// TODO: add message or use custom exception
		throw new UnsupportedOperationException("");
	}
}
