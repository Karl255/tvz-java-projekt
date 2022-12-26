package hr.java.projektnizadatak.presentation;

import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
	private static Stage stage;

	public static void main(String[] args) {
		launch();
	}

	public static void setWindow(ApplicationScreen screen) {
		try {
			var fxmlPath = screen.getFxmlPath();
			var window = (Parent) FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxmlPath)));
			stage.setScene(new Scene(window));
			stage.show();
		} catch (IOException e) {
			// TODO: handle this better
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage stage) throws IOException {
		Application.stage = stage;

		FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(ApplicationScreen.MainScreen.getFxmlPath()));
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("Projektni zadatak");
		stage.setScene(scene);
		stage.show();
	}
}
