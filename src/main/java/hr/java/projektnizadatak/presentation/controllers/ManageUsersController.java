package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.application.entities.UserRole;
import hr.java.projektnizadatak.presentation.models.ManageUsersModel;
import hr.java.projektnizadatak.presentation.models.UserItemModel;
import hr.java.projektnizadatak.shared.exceptions.NoAdminUserException;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;

public class ManageUsersController {
	private final ManageUsersModel model;

	@FXML private TableView<UserItemModel> userTableView;
	@FXML private TableColumn<UserItemModel, String> usernameColumn;
	@FXML private TableColumn<UserItemModel, UserRole> roleColumn;
	@FXML private TableColumn<UserItemModel, String> defaultDepartmentColumn;
	@FXML private TableColumn<UserItemModel, Semester> defaultSemesterColumn;
	
	@FXML private Button deleteButton;
	
	public ManageUsersController() {
		model = new ManageUsersModel();
	}
	
	@FXML
	private void initialize() {
		usernameColumn.setCellValueFactory(d -> d.getValue().usernameProperty());
		roleColumn.setCellValueFactory(d -> d.getValue().roleProperty());
		roleColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(UserRole.values()));
		defaultDepartmentColumn.setCellValueFactory(d -> d.getValue().defaultDepartmentCodeProperty());
		defaultSemesterColumn.setCellValueFactory(d -> d.getValue().defaultSemesterProperty());
		
		userTableView.setItems(model.getUserList());
		userTableView.getSelectionModel().selectedItemProperty().addListener(this::onSelectedRow);
		
		model.initialize();
	}
	
	private void onSelectedRow(Observable observable, UserItemModel oldValue, UserItemModel newValue) {
		model.setSelected(newValue);

		deleteButton.setDisable(newValue == null);
	}
	
	@FXML
	private void deleteSelectedUser() {
		var alert = new Alert(
			Alert.AlertType.CONFIRMATION,
			"Are you sure you want to delete this user?",
			ButtonType.YES, ButtonType.CANCEL
		);

		alert.setTitle("Confirm deletion");
		var clicked = alert.showAndWait();

		if (clicked.isPresent() && clicked.get().equals(ButtonType.YES)) {
			model.deleteSelected();
		}
		
	}
	
	@FXML
	private void save() {
		var alert = new Alert(
			Alert.AlertType.CONFIRMATION,
			"Are you sure you want to save your changes?",
			ButtonType.YES, ButtonType.CANCEL
		);
		
		alert.setTitle("Confirm save");
		var clicked = alert.showAndWait();
		
		if (clicked.isPresent() && clicked.get().equals(ButtonType.YES)) {
			try {
				model.saveChanges();
			} catch (NoAdminUserException e) {
				var errorAlert = new Alert(
					Alert.AlertType.ERROR,
					"There needs to be at least one admin user",
					ButtonType.OK
				);
				
				errorAlert.show();
			}
		}
	}
}
