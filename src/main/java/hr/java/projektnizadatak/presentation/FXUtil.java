package hr.java.projektnizadatak.presentation;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.presentation.models.ScheduleItemModel;
import javafx.scene.control.Alert;

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
}
