package hr.java.projektnizadatak.data.api_response;

import com.google.gson.annotations.SerializedName;
import hr.java.projektnizadatak.application.entities.Department;

public record ApiDepartment(@SerializedName("Code") String code, @SerializedName("Name") String name) {
	public Department toDepartment() {
		return new Department(code, name);
	}
}
