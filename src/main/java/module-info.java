module hr.java.projektnizadatak {
	requires javafx.controls;
	requires javafx.fxml;


	opens hr.java.projektnizadatak to javafx.fxml;
	exports hr.java.projektnizadatak;
	exports hr.java.projektnizadatak.presentation.controllers;
	opens hr.java.projektnizadatak.presentation.controllers to javafx.fxml;
}
