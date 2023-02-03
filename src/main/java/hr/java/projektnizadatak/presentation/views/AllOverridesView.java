package hr.java.projektnizadatak.presentation.views;

import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.controllers.AllOverridesController;
import hr.java.projektnizadatak.presentation.ApplicationScreen;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AllOverridesView {
	private final AllOverridesController controller;

	@FXML private TableView<ScheduleOverride> originalsTableView;
	@FXML private TableColumn<ScheduleOverride, String> courseNameColumn;
	@FXML private TableColumn<ScheduleOverride, String> classNameColumn;
	@FXML private TableColumn<ScheduleOverride, String> classTypeColumn;
	@FXML private TableColumn<ScheduleOverride, String> weekdayColumn;
	@FXML private TableColumn<ScheduleOverride, String> timeColumn;
	@FXML private TableColumn<ScheduleOverride, String> groupColumn;
	@FXML private TableColumn<ScheduleOverride, String> noteColumn;

	@FXML private TextField filterTextInput;
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private Label originalItemLabel;
	@FXML private VBox replacementsVBox;

	public AllOverridesView() {
		controller = new AllOverridesController();
	}

	@FXML
	private void initialize() {
		courseNameColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().courseName()));
		classNameColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().className()));
		classTypeColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().classType().getName()));
		weekdayColumn.setCellValueFactory(d -> new SimpleStringProperty(
			FXUtil.weekdayName(d.getValue().original().weekday())
		));
		timeColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().getTimestampFull()));
		groupColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().group()));
		noteColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().note()));

		originalsTableView.setItems(controller.getOverridesList());
		originalsTableView.getSelectionModel().selectedItemProperty().addListener(this::onSelect);

		try {
			controller.initialize();
		} catch (DataStoreException e) {
			FXUtil.showDataStoreExceptionAlert(e);
		}
	}
	
	@FXML
	private void setFilter() {
		controller.setFilter(filterTextInput.getText());
	}

	private void onSelect(Observable observable, ScheduleOverride oldValue, ScheduleOverride newValue) {
		controller.setSelected(newValue);
		
		editButton.setDisable(newValue == null);
		deleteButton.setDisable(newValue == null);
		
		if (newValue != null) {
			originalItemLabel.setText(FXUtil.scheduleItemToString(newValue.original()));
			
			replacementsVBox.getChildren().setAll(newValue.replacements().stream()
				.map(r -> new Text(FXUtil.overrideDataToOnelineString(r)))
				.toList()
			);
		} else {
			originalItemLabel.setText("");
			replacementsVBox.getChildren().clear();
		}
	}
	
	@FXML
	private void editSelected() {
		var selected = controller.getSelected();
		
		if (selected != null) {
			Application.getOverrideManager().setItemBeingEdited(selected.original(), null, null);
			Application.pushScreen(ApplicationScreen.EditOverride);
		}
	}

	@FXML
	private void deleteSelected() {
		var alert = new Alert(
			Alert.AlertType.CONFIRMATION,
			"Are you sure you want to delete this override?",
			ButtonType.YES, ButtonType.CANCEL
		);
		
		alert.setTitle("Confirm deletion");
		
		var clicked = alert.showAndWait();
		
		if (clicked.isPresent() && clicked.get().equals(ButtonType.YES)) {
			try {
				controller.deleteSelected();
			} catch (DataStoreException e) {
				FXUtil.showDataStoreExceptionAlert(e);
			}
		}
	}
}
