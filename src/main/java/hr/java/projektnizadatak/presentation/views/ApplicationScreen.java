package hr.java.projektnizadatak.presentation.views;

public enum ApplicationScreen {
	// TODO: check titles and stuff
	Login("views/login-view.fxml", "Login", false),
	MainScreen("views/main-screen-view.fxml", "Projektni zadatak", true),
	;
	
	private final String fxmlPath;
	private final String title;
	private final boolean canResize;
	
	ApplicationScreen(String fxmlPath, String title, boolean canResize) {
		this.fxmlPath = fxmlPath;
		this.title = title;
		this.canResize = canResize;
	}

	public String getFxmlPath() {
		return fxmlPath;
	}

	public boolean canResize() {
		return canResize;
	}
}
