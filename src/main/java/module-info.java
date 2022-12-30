module hr.java.projektnizadatak {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.net.http;
	requires com.google.gson;

	exports hr.java.projektnizadatak.application;
	exports hr.java.projektnizadatak.application.entities;
	
	exports hr.java.projektnizadatak.presentation;
	opens hr.java.projektnizadatak.presentation to javafx.fxml;
	exports hr.java.projektnizadatak.presentation.controllers;
	opens hr.java.projektnizadatak.presentation.controllers to javafx.fxml;
	exports hr.java.projektnizadatak.presentation.models;
	exports hr.java.projektnizadatak.presentation.views;
	
	//exports hr.java.projektnizadatak.shared;
	exports hr.java.projektnizadatak.shared.exceptions;
	
	exports hr.java.projektnizadatak.data;
	exports hr.java.projektnizadatak.data.api_response;
	exports hr.java.projektnizadatak.presentation.util;
	opens hr.java.projektnizadatak.presentation.util to javafx.fxml;
	exports hr.java.projektnizadatak.shared;
}
