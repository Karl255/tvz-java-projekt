package hr.java.projektnizadatak.presentation;

import hr.java.projektnizadatak.application.ScheduleSource;
import hr.java.projektnizadatak.application.UserManager;
import hr.java.projektnizadatak.data.ScheduleApiSource;
import hr.java.projektnizadatak.data.UsersFileStore;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import hr.java.projektnizadatak.shared.exceptions.FxmlLoadingException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class Application extends javafx.application.Application {
	private static Stage stage;

	private static UserManager userManager = new UserManager(new UsersFileStore());
	private static ScheduleSource scheduleSource = new ScheduleApiSource();

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
			throw new FxmlLoadingException("Loading FXML for screen: " + screen, e);
		}
	}

	public static UserManager getUserManager() {
		return userManager;
	}

	public static ScheduleSource getScheduleSource() {
		return scheduleSource;
	}

	@Override
	public void start(Stage stage) {
		Application.stage = stage;

		stage.setTitle("Projektni zadatak");
		setScreen(ApplicationScreen.Login);
	}
}
