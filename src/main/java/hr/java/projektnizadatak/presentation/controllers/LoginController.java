package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.User;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
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
		var user = getUserFromInputs();

		if (Application.getUserManager().tryLoginUser(user)) {
			Application.setScreen(ApplicationScreen.MainScreen);
		} else {
			FXUtil.showAlert(Alert.AlertType.ERROR, "Couldn't log in", "Invalid user or password!");
		}
	}

	@FXML
	private void createUser() {
		var user = getUserFromInputs();

		if (!user.isUsernameValid()) {
			FXUtil.showAlert(Alert.AlertType.ERROR, "Invalid username", "Given username length is invalid or it contains invalid characters!");
		} else if (Application.getUserManager().tryAddUser(user)) {
			FXUtil.showAlert(Alert.AlertType.INFORMATION, "User created", "User successfully created!");
		} else {
			FXUtil.showAlert(Alert.AlertType.ERROR, "Username taken", "This username is already taken!");
		}
	}

	private User getUserFromInputs() {
		return new User.UserBuilder(usernameInput.getText())
			.withPassword(passwordInput.getText())
			.build();
	}
}
