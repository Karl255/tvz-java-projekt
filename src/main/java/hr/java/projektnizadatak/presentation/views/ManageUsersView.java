package hr.java.projektnizadatak.presentation.views;

import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.application.entities.UserRole;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.controllers.ManageUsersController;
import hr.java.projektnizadatak.presentation.models.UserModel;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;
import hr.java.projektnizadatak.shared.exceptions.NoAdminUserException;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;

public class ManageUsersView {
	private final ManageUsersController controller;

	@FXML private TableView<UserModel> userTableView;
	@FXML private TableColumn<UserModel, String> usernameColumn;
	@FXML private TableColumn<UserModel, UserRole> roleColumn;
	@FXML private TableColumn<UserModel, String> defaultDepartmentColumn;
	@FXML private TableColumn<UserModel, Semester> defaultSemesterColumn;
	
	@FXML private Button deleteButton;
	
	public ManageUsersView() {
		controller = new ManageUsersController();
	}
	
	@FXML
	private void initialize() {
		usernameColumn.setCellValueFactory(d -> d.getValue().usernameProperty());
		roleColumn.setCellValueFactory(d -> d.getValue().roleProperty());
		roleColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(UserRole.values()));
		defaultDepartmentColumn.setCellValueFactory(d -> d.getValue().defaultDepartmentCodeProperty());
		defaultSemesterColumn.setCellValueFactory(d -> d.getValue().defaultSemesterProperty());
		
		userTableView.setItems(controller.getUserList());
		userTableView.getSelectionModel().selectedItemProperty().addListener(this::onSelectedRow);
		
		controller.initialize();
	}
	
	private void onSelectedRow(Observable observable, UserModel oldValue, UserModel newValue) {
		controller.setSelected(newValue);

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
			controller.deleteSelected();
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
				controller.saveChanges();
			} catch (NoAdminUserException e) {
				var errorAlert = new Alert(
					Alert.AlertType.ERROR,
					"There needs to be at least one admin user",
					ButtonType.OK
				);
				
				errorAlert.show();
			} catch (DataStoreException e) {
				FXUtil.showDataStoreExceptionAlert(e);
			}
		}
	}
}
