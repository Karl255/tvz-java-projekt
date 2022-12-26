module hr.java.projektnizadatak {
	requires javafx.controls;
	requires javafx.fxml;

	exports hr.java.projektnizadatak.presentation.views;
	exports hr.java.projektnizadatak.presentation.controllers;
	opens hr.java.projektnizadatak.presentation.controllers to javafx.fxml;
	exports hr.java.projektnizadatak.presentation;
	opens hr.java.projektnizadatak.presentation to javafx.fxml;
}
