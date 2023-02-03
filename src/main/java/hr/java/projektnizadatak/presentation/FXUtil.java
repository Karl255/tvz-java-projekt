package hr.java.projektnizadatak.presentation;

import hr.java.projektnizadatak.application.entities.OverrideData;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.presentation.models.ScheduleItemModel;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;
import hr.java.projektnizadatak.shared.exceptions.NetworkErrorException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.DayOfWeek;

public class FXUtil {
	public static void showAlert(Alert.AlertType alertType, String title, String body) {
		var alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(body);
		alert.show();
	}

	public static String scheduleItemToString(ScheduleItemModel model) {
		return scheduleItemToString(model.toScheduleItem());
	}
	
	public static String scheduleItemToString(ScheduleItem item) {
		var sb = new StringBuilder()
			.append(item.getTimestampShort()).append('\n')
			.append(item.className()).append('\n')
			.append(item.classType()).append('\n')
			.append(item.classroom()).append('\n')
			.append(item.professor()).append('\n');

		if (item.group() != null && !item.group().isBlank()) {
			sb.append("Grupa: ").append(item.group()).append('\n');
		}

		if (item.note() != null && !item.note().isBlank()) {
			sb.append("Napomena: ").append(item.note()).append('\n');
		}
		
		return sb.toString();
	}
	
	public static String overrideDataToOnelineString(OverrideData overrideData) {
		return new StringBuilder()
			.append(overrideData.getTimestampFull()).append("; ")
			.append(overrideData.classType().getName()).append("; ")
			.append(overrideData.classroom())
			.toString();
	}
	
	public static String weekdayName(DayOfWeek dow) {
		return switch (dow) {
			case MONDAY -> "Monday";
			case TUESDAY -> "Tuesday";
			case WEDNESDAY -> "Wednesday";
			case THURSDAY -> "Thursday";
			case FRIDAY -> "Friday";
			case SATURDAY -> "Saturday";
			case SUNDAY -> "Sunday";
		};
	}
	
	public static void showDataStoreExceptionAlert(DataStoreException e) {
		var alert = new Alert(
			Alert.AlertType.ERROR,
			e.getMessage(),
			ButtonType.OK
		);
		
		alert.setTitle("Data access error");
		alert.setHeaderText("An error occured while trying to read or write data. Details are below.");
		alert.showAndWait();
	}
	
	public static void showNetworkErrorAlert(NetworkErrorException e) {
		var alert = new Alert(
			Alert.AlertType.ERROR,
			e.getMessage(),
			ButtonType.OK
		);
		
		alert.setTitle("Network error");
		alert.setHeaderText("An error occured while trying to connect to the Internet. Please check your Internet connection.");
		alert.showAndWait();
	}
}
