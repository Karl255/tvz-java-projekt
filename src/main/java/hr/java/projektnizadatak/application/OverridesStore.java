package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.OverrideData;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;

import java.util.List;

public interface OverridesStore {
	long createOriginal(ScheduleItem original, String forSubdepartment, int forSemester, String forUsername);
	List<Long> createReplacements(List<OverrideData> replacements, long forOriginalId);
	
	ScheduleOverride readOverride(long id);
	List<ScheduleOverride> readAllOverridesFor(String subdepartment, int semester);

	void updateOriginal(ScheduleItem original);
	void updateReplacements(List<OverrideData> replacements);

	void deleteReplacements(List<Long> replacementIds);

	void deleteOriginal(Long id);
}
