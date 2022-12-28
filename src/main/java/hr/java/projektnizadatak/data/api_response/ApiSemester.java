package hr.java.projektnizadatak.data.api_response;

import com.google.gson.annotations.SerializedName;
import hr.java.projektnizadatak.application.entities.Semester;

public record ApiSemester(@SerializedName("SemesterNumber") String semester, @SerializedName("Department") String subdepartment) {
	public Semester toSemester() {
		return new Semester(subdepartment, Integer.parseInt(semester));
	}
}
