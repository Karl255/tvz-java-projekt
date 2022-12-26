package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.fxml.FXML;

public class GlobalMenuController {
	@FXML
	private void showMainScreen() {
		Application.setScreen(ApplicationScreen.MainScreen);
	}

	@FXML
	private void logout() {
		Application.getUserManager().logout();
		Application.setScreen(ApplicationScreen.Login);
	}
}
