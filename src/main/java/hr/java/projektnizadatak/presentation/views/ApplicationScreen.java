package hr.java.projektnizadatak.presentation.views;

public enum ApplicationScreen {
	Login("views/login-view.fxml", "Login", false),
	MainScreen("views/main-screen-view.fxml", "Timetable", true),
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

	public String getTitle() {
		return title;
	}

	public boolean canResize() {
		return canResize;
	}
	
	@Override
	public String toString() {
		return title + "(" + fxmlPath + ", " + canResize + ")";
	}
}
