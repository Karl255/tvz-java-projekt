package hr.java.projektnizadatak;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
	private static Stage stage;
	
	@Override
	public void start(Stage stage) throws IOException {
		Application.stage = stage;
		
		FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/main-screen-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("Projektni zadatak");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
	
	public static void setWindow(String fxmlPath) {
		try {
			var window = (Parent) FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxmlPath)));
			stage.setScene(new Scene(window));
			stage.show();
		} catch (IOException e) {
			// TODO: handle this better
			e.printStackTrace();
		}
	}
}
