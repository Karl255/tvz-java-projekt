package hr.java.projektnizadatak.application.entities;

public record Semester(String subdepartment, int semester) {
	public String toString() {
		return semester + "-" + subdepartment;
	}
}
