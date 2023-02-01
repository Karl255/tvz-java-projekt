package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.models.AllOverridesModel;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AllOverridesController {
	private final AllOverridesModel model;

	@FXML private TableView<ScheduleOverride> originalsTableView;
	@FXML private TableColumn<ScheduleOverride, String> courseNameColumn;
	@FXML private TableColumn<ScheduleOverride, String> classNameColumn;
	@FXML private TableColumn<ScheduleOverride, String> classTypeColumn;
	@FXML private TableColumn<ScheduleOverride, Integer> weekdayColumn;
	@FXML private TableColumn<ScheduleOverride, String> timeColumn;
	@FXML private TableColumn<ScheduleOverride, String> groupColumn;
	@FXML private TableColumn<ScheduleOverride, String> noteColumn;

	@FXML private Button editButton;
	@FXML private Label originalItemLabel;
	@FXML private VBox replacementsVBox;

	public AllOverridesController() {
		model = new AllOverridesModel();
	}

	@FXML
	private void initialize() {
		courseNameColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().courseName()));
		classNameColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().className()));
		classTypeColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().classType().getName()));
		weekdayColumn.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().original().weekday().getValue()).asObject());
		timeColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().getTimestampFull()));
		groupColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().group()));
		noteColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().original().note()));

		originalsTableView.setItems(model.getOverridesList());
		originalsTableView.getSelectionModel().selectedItemProperty().addListener(this::onSelect);

		model.initialize();
	}

	private void onSelect(Observable observable, ScheduleOverride oldValue, ScheduleOverride newValue) {
		model.setSelected(newValue);
		
		if (newValue != null) {
			editButton.setDisable(false);
			originalItemLabel.setText(FXUtil.scheduleItemToString(newValue.original()));
			
			replacementsVBox.getChildren().setAll(newValue.replacements().stream()
				.map(r -> new Text(FXUtil.overrideDataToOnelineString(r)))
				.toList()
			);
		} else {
			editButton.setDisable(true);
			originalItemLabel.setText("");
			replacementsVBox.getChildren().clear();
		}
	}
	
	@FXML
	private void editSelected() {
		var selected = model.getSelected();
		
		if (selected != null) {
			Application.getOverrideManager().setItemBeingEdited(selected.original(), null, null);
			Application.pushScreen(ApplicationScreen.EditOverride);
		}
	}
}
