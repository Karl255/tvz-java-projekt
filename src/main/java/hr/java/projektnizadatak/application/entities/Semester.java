package hr.java.projektnizadatak.application.entities;

import java.io.Serializable;

public record Semester(String subdepartment, int semester) implements Serializable {
	public String toString() {
		return semester + "-" + subdepartment;
	}
}
