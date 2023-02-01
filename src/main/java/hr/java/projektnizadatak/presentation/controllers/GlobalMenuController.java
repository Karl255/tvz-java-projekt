package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.fxml.FXML;

public class GlobalMenuController {
	@FXML
	private void logout() {
		Application.getUserManager().logout();
		Application.switchToScreen(ApplicationScreen.Login);
	}
	
	@FXML
	private void showTimetableScreen() {
		Application.switchToScreen(ApplicationScreen.Timetable);
	}

	@FXML
	private void showManageOverridesScreen() {
		Application.switchToScreen(ApplicationScreen.AllOverrides);
	}
	
	@FXML
	private void showChangeLogScreen() {
		Application.switchToScreen(ApplicationScreen.ChangeLog);
	}
}
