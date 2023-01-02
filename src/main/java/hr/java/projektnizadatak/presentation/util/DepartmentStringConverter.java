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
		throw new UnsupportedOperationException("Cannot create Semester object from String");
	}
}
