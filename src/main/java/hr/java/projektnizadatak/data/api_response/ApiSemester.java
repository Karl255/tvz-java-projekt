package hr.java.projektnizadatak.data.api_response;

import com.google.gson.annotations.SerializedName;

public record ApiSemester(@SerializedName("SemesterNumber") String semester, @SerializedName("Department") String subdepartment) {
}
