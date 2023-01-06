package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.fxml.FXML;

public class GlobalMenuController {
	@FXML
	private void logout() {
		Application.getUserManager().logout();
		Application.setScreen(ApplicationScreen.Login);
	}
	
	@FXML
	private void showTimetableScreen() {
		Application.setScreen(ApplicationScreen.Timetable);
	}
	
	@FXML
	private void showChangesScreen() {
		
	}
	
	@FXML
	private void showManageOverridesScreen() {
		
	}
}
