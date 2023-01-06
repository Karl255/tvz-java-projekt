package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.application.entities.ClassType;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleItemModel {
	private final SimpleLongProperty id;
	private final SimpleStringProperty courseName;
	private final SimpleStringProperty className;
	private final SimpleStringProperty professor;
	private final SimpleObjectProperty<ClassType> classType;
	private final SimpleStringProperty classroom;
	private final SimpleStringProperty note;
	private final SimpleStringProperty group;
	private final SimpleObjectProperty<LocalDate> date;
	private final SimpleObjectProperty<LocalTime> start;
	private final SimpleObjectProperty<LocalTime> end;
	private final SimpleBooleanProperty isOriginal;
	
	public ScheduleItemModel(ScheduleItem item) {
		this.id = new SimpleLongProperty(item.id());
		this.courseName = new SimpleStringProperty(item.courseName());
		this.className = new SimpleStringProperty(item.className());
		this.professor = new SimpleStringProperty(item.professor());
		this.classType = new SimpleObjectProperty<>(item.classType());
		this.classroom = new SimpleStringProperty(item.classroom());
		this.note = new SimpleStringProperty(item.note());
		this.group = new SimpleStringProperty(item.group());
		this.date = new SimpleObjectProperty<>(item.date());
		this.start = new SimpleObjectProperty<>(item.start());
		this.end = new SimpleObjectProperty<>(item.end());
		this.isOriginal = new SimpleBooleanProperty(item.isOriginal());
	}
	
	public ScheduleItem toScheduleItem() {
		return new ScheduleItem(
			id.get(),
			courseName.get(),
			className.get(),
			professor.get(),
			classType.get(),
			classroom.get(),
			note.get(), 
			group.get(),
			date.get(),
			start.get(),
			end.get(),
			isOriginal.get()
		);
	}

	public long getId() {
		return id.get();
	}

	public SimpleLongProperty idProperty() {
		return id;
	}

	public void setId(long id) {
		this.id.set(id);
	}

	public String getCourseName() {
		return courseName.get();
	}

	public SimpleStringProperty courseNameProperty() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName.set(courseName);
	}

	public String getClassName() {
		return className.get();
	}

	public SimpleStringProperty classNameProperty() {
		return className;
	}

	public void setClassName(String className) {
		this.className.set(className);
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

	public LocalDate getDate() {
		return date.get();
	}

	public SimpleObjectProperty<LocalDate> dateProperty() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date.set(date);
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

	public boolean isIsOriginal() {
		return isOriginal.get();
	}

	public SimpleBooleanProperty isOriginalProperty() {
		return isOriginal;
	}

	public void setIsOriginal(boolean isOriginal) {
		this.isOriginal.set(isOriginal);
	}
}
