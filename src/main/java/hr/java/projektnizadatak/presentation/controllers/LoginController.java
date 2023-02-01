package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import hr.java.projektnizadatak.shared.exceptions.InvalidUsernameException;
import hr.java.projektnizadatak.shared.exceptions.UsernameTakenException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML
	private TextField usernameInput;

	@FXML
	private PasswordField passwordInput;

	@FXML
	private void login() {
		if (Application.getUserManager().tryLoginUser(usernameInput.getText(), passwordInput.getText())) {
			Application.switchToScreen(ApplicationScreen.Timetable);
		} else {
			FXUtil.showAlert(Alert.AlertType.ERROR, "Couldn't log in", "Invalid user or password!");
		}
	}

	@FXML
	private void createUser() {
		try {
			var user = Application.getUserManager().createUser(usernameInput.getText(), passwordInput.getText());
			
			String content = String.format("User %s successfully created!", user.username());
			FXUtil.showAlert(Alert.AlertType.INFORMATION, "User created", content);
		} catch (InvalidUsernameException e) {
			FXUtil.showAlert(Alert.AlertType.ERROR, "Invalid username", "Given username length is invalid or it contains invalid characters: " + usernameInput.getText());
		} catch (UsernameTakenException e) {
			FXUtil.showAlert(Alert.AlertType.ERROR, "Username taken", "This username is already taken: " + usernameInput.getText());
		}
	}
}
