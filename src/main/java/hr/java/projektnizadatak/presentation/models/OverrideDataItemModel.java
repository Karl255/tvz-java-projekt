package hr.java.projektnizadatak.presentation.models;


import hr.java.projektnizadatak.application.entities.ClassType;
import hr.java.projektnizadatak.application.entities.OverrideData;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalTime;
import java.util.Objects;

public class OverrideDataItemModel {
	private final Long dbId;
	private final SimpleStringProperty professor;
	private final SimpleObjectProperty<ClassType> classType;
	private final SimpleStringProperty classroom;
	private final SimpleStringProperty note;
	private final SimpleStringProperty group;
	private final SimpleObjectProperty<LocalTime> start;
	private final SimpleObjectProperty<LocalTime> end;

	public OverrideDataItemModel(OverrideData overrideData) {
		this.dbId = overrideData.id();
		this.professor = new SimpleStringProperty(overrideData.professor());
		this.classType = new SimpleObjectProperty<>(overrideData.classType());
		this.classroom = new SimpleStringProperty(overrideData.classroom());
		this.note = new SimpleStringProperty(overrideData.note());
		this.group = new SimpleStringProperty(overrideData.group());
		this.start = new SimpleObjectProperty<>(overrideData.start());
		this.end = new SimpleObjectProperty<>(overrideData.end());
	}

	public OverrideDataItemModel(OverrideDataItemModel overrideDataItemModel) {
		this.dbId = null;
		this.professor = new SimpleStringProperty(overrideDataItemModel.getProfessor());
		this.classType = new SimpleObjectProperty<>(overrideDataItemModel.getClassType());
		this.classroom = new SimpleStringProperty(overrideDataItemModel.getClassroom());
		this.note = new SimpleStringProperty(overrideDataItemModel.getNote());
		this.group = new SimpleStringProperty(overrideDataItemModel.getGroup());
		this.start = new SimpleObjectProperty<>(overrideDataItemModel.getStart());
		this.end = new SimpleObjectProperty<>(overrideDataItemModel.getEnd());
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
	
	public OverrideDataItemModel copy() {
		return new OverrideDataItemModel(this);
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
	
	public boolean equals(OverrideData other) {
		return Objects.equals(dbId, other.id())
			&& Objects.equals(professor.get(), other.professor())
			&& classType.get() == other.classType()
			&& Objects.equals(classroom.get(), other.classroom())
			&& Objects.equals(note.get(), other.note())
			&& Objects.equals(group.get(), other.group())
			&& Objects.equals(start.get(), other.start())
			&& Objects.equals(end.get(), other.end());
	}
}
