package hr.java.projektnizadatak.presentation.models;


import hr.java.projektnizadatak.application.entities.ClassType;
import hr.java.projektnizadatak.application.entities.OverrideData;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalTime;

public class OverrideDataModel {
	private final Long dbId;
	private final SimpleStringProperty professor;
	private final SimpleObjectProperty<ClassType> classType;
	private final SimpleStringProperty classroom;
	private final SimpleStringProperty note;
	private final SimpleStringProperty group;
	private final SimpleObjectProperty<LocalTime> start;
	private final SimpleObjectProperty<LocalTime> end;

	public OverrideDataModel(OverrideData overrideData) {
		this.dbId = overrideData.id();
		this.professor = new SimpleStringProperty(overrideData.professor());
		this.classType = new SimpleObjectProperty<>(overrideData.classType());
		this.classroom = new SimpleStringProperty(overrideData.classroom());
		this.note = new SimpleStringProperty(overrideData.note());
		this.group = new SimpleStringProperty(overrideData.group());
		this.start = new SimpleObjectProperty<>(overrideData.start());
		this.end = new SimpleObjectProperty<>(overrideData.end());
	}

	public OverrideDataModel(OverrideDataModel overrideDataModel) {
		this.dbId = overrideDataModel.getDbId();
		this.professor = new SimpleStringProperty(overrideDataModel.getProfessor());
		this.classType = new SimpleObjectProperty<>(overrideDataModel.getClassType());
		this.classroom = new SimpleStringProperty(overrideDataModel.getClassroom());
		this.note = new SimpleStringProperty(overrideDataModel.getNote());
		this.group = new SimpleStringProperty(overrideDataModel.getGroup());
		this.start = new SimpleObjectProperty<>(overrideDataModel.getStart());
		this.end = new SimpleObjectProperty<>(overrideDataModel.getEnd());
	}

	public OverrideData toOverrideData() {
		return new OverrideData(
			dbId,
			professor.get(),
			classType.get(),
			classroom.get(),
			note.get(),
			group.get(),
			start.get(),
			end.get()
		);
	}
	
	public OverrideDataModel copy() {
		return new OverrideDataModel(this);
	}
	
	public Long getDbId() {
		return dbId;
	}

	public String getProfessor() {
		return professor.get();
	}

	public SimpleStringProperty professorProperty() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor.set(professor);
	}

	public ClassType getClassType() {
		return classType.get();
	}

	public SimpleObjectProperty<ClassType> classTypeProperty() {
		return classType;
	}

	public void setClassType(ClassType classType) {
		this.classType.set(classType);
	}

	public String getClassroom() {
		return classroom.get();
	}

	public SimpleStringProperty classroomProperty() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom.set(classroom);
	}

	public String getNote() {
		return note.get();
	}

	public SimpleStringProperty noteProperty() {
		return note;
	}

	public void setNote(String note) {
		this.note.set(note);
	}

	public String getGroup() {
		return group.get();
	}

	public SimpleStringProperty groupProperty() {
		return group;
	}

	public void setGroup(String group) {
		this.group.set(group);
	}

	public LocalTime getStart() {
		return start.get();
	}

	public SimpleObjectProperty<LocalTime> startProperty() {
		return start;
	}

	public void setStart(LocalTime start) {
		this.start.set(start);
	}

	public LocalTime getEnd() {
		return end.get();
	}

	public SimpleObjectProperty<LocalTime> endProperty() {
		return end;
	}

	public void setEnd(LocalTime end) {
		this.end.set(end);
	}
}
