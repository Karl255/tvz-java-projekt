package hr.java.projektnizadatak.presentation;

import javafx.scene.control.Alert;

public class FXUtil {
	public static void showAlert(Alert.AlertType alertType, String title, String body) {
		var alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(body);
		alert.show();
	}
}
