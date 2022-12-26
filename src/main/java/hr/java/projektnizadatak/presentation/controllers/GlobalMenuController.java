package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.fxml.FXML;

public class GlobalMenuController {
	@FXML
	public void showMainScreen() {
		Application.setWindow(ApplicationScreen.MainScreen);
	}
}
