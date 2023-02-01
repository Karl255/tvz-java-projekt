package hr.java.projektnizadatak.presentation;

import hr.java.projektnizadatak.application.ScheduleOverridesManager;
import hr.java.projektnizadatak.application.ScheduleSource;
import hr.java.projektnizadatak.application.UserManager;
import hr.java.projektnizadatak.data.ScheduleApiSource;
import hr.java.projektnizadatak.data.ScheduleOverridesDBStore;
import hr.java.projektnizadatak.data.UsersFileStore;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import hr.java.projektnizadatak.shared.exceptions.FxmlLoadingException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

@SuppressWarnings("FieldMayBeFinal")
public class Application extends javafx.application.Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	private static Stage stage;
	private static Stack<ApplicationScreen> screenStack;

	private static UserManager userManager = new UserManager(new UsersFileStore());
	private static ScheduleSource scheduleSource = new ScheduleApiSource();
	private static ScheduleOverridesManager scheduleOverridesManager = new ScheduleOverridesManager(new ScheduleOverridesDBStore());

	public static void main(String[] args) {
		launch();
	}

	public static void switchToScreen(ApplicationScreen screen) {
		screenStack.pop();
		screenStack.push(screen);
		setScreen(screenStack.peek());
	}
	
	public static void pushScreen(ApplicationScreen screen) {
		screenStack.push(screen);
		setScreen(screenStack.peek());
	}
	
	public static void popScreen() {
		screenStack.pop();
		
		if (screenStack.empty()) {
			screenStack.push(ApplicationScreen.Timetable);
		}
		
		setScreen(screenStack.peek());
	}
	
	private static void setScreen(ApplicationScreen screen) {
		try {
			var fxmlPath = screen.getFxmlPath();
			var window = (Parent) FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxmlPath)));
			var scene = new Scene(window);
			scene.getStylesheets().add("main.css");

			stage.setResizable(screen.canResize());
			stage.setScene(scene);
			stage.setTitle(screen.getTitle());
			stage.show();
		} catch (IOException e) {
			String m = "Loading FXML for screen: " + screen;
			logger.error(m);

			throw new FxmlLoadingException(m, e);
		}
	}

	public static UserManager getUserManager() {
		return userManager;
	}

	public static ScheduleSource getScheduleSource() {
		return scheduleSource;
	}

	public static ScheduleOverridesManager getOverrideManager() {
		return scheduleOverridesManager;
	}

	@Override
	public void start(Stage stage) {
		Application.stage = stage;
		Application.screenStack = new Stack<>();

		pushScreen(ApplicationScreen.Login);
	}
}
