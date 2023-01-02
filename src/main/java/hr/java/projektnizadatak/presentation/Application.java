package hr.java.projektnizadatak.presentation;

import hr.java.projektnizadatak.application.UserManager;
import hr.java.projektnizadatak.data.UsersFileStore;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
	private static Stage stage;

	private static final UserManager userManager = new UserManager(new UsersFileStore());

	public static void main(String[] args) {
		launch();
	}

	public static void setScreen(ApplicationScreen screen) {
		try {
			var fxmlPath = screen.getFxmlPath();
			var window = (Parent) FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxmlPath)));
			stage.setResizable(screen.canResize());
			stage.setScene(new Scene(window));
			stage.setTitle(screen.getTitle());
			stage.show();
		} catch (IOException e) {
			// TODO: handle this better
			e.printStackTrace();
		}
	}

	public static UserManager getUserManager() {
		return userManager;
	}

	@Override
	public void start(Stage stage) {
		Application.stage = stage;

		stage.setTitle("Projektni zadatak");
		setScreen(ApplicationScreen.Login);
	}
}
