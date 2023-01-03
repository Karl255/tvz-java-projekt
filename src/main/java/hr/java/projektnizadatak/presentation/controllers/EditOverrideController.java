package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EditOverrideController {
	@FXML
	private Label testLabel;
	
	@FXML
	private void initialize() {
		var selectedItem = Application.getOverrideManager().getItemBeingEdited();
		testLabel.setText("" + selectedItem.id());
	}
	
	@FXML
	private void close() {
		Application.setScreen(ApplicationScreen.MainScreen);
	}
}
