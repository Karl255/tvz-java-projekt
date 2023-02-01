package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.UserRole;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class GlobalMenuController {
	@FXML private MenuItem showManageUsersMenuItem;
	
	@FXML
	private void initialize() {
		var user = Application.getUserManager().getLoggedInUser();
		
		if (user.role() == UserRole.ADMIN) {
			showManageUsersMenuItem.setDisable(false);
		}
	}
	
	@FXML
	private void showManageUsers() {
		Application.switchToScreen(ApplicationScreen.ManageUsers);
	}

	@FXML
	private void switchUser() {
		Application.getUserManager().logout();
		Application.switchToScreen(ApplicationScreen.Login);
	}

	@FXML
	private void logout() {
		Platform.exit();
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
