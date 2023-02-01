package hr.java.projektnizadatak.presentation.views;

public enum ApplicationScreen {
	Login("Login", "views/login-view.fxml", false),
	Timetable("Timetable", "views/timetable-view.fxml", true),
	EditOverride("Edit override", "views/edit-override-view.fxml", true),
	AllOverrides("Overrides", "views/all-overrides-view.fxml", true),
	ChangeLog("Change log", "views/changes-browser-view.fxml", true)
	;
	
	private final String title;
	private final String fxmlPath;
	private final boolean canResize;
	
	ApplicationScreen(String title, String fxmlPath, boolean canResize) {
		this.title = title;
		this.fxmlPath = fxmlPath;
		this.canResize = canResize;
	}

	public String getTitle() {
		return title;
	}

	public String getFxmlPath() {
		return fxmlPath;
	}

	public boolean canResize() {
		return canResize;
	}
	
	@Override
	public String toString() {
		return title + "(" + fxmlPath + ", " + canResize + ")";
	}
}
