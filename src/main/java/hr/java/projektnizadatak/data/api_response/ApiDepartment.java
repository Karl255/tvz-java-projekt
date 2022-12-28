package hr.java.projektnizadatak.data.api_response;

import com.google.gson.annotations.SerializedName;

public record ApiDepartment(@SerializedName("Code") String code, @SerializedName("Name") String name) {
}
