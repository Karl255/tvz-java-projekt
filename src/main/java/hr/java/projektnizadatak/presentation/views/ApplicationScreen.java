package hr.java.projektnizadatak.presentation.views;

public enum ApplicationScreen {
	MainScreen("views/main-screen-view.fxml");
	
	private final String fxmlPath;
	
	ApplicationScreen(String fxmlPath) {
		this.fxmlPath = fxmlPath;
	}

	public String getFxmlPath() {
		return fxmlPath;
	}
}
