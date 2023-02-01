package hr.java.projektnizadatak.application.entities;

public sealed interface Recordable permits ScheduleOverride, User {
	String displayShort();
	
	String displayFull();
	
	// TODO: diffing?
}
