package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.OverrideData;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;

import java.util.List;

public interface OverridesStore {
	long createOriginal(ScheduleItem original, String forSubdepartment, int forSemester, String forUsername) throws DataStoreException;
	List<Long> createReplacements(List<OverrideData> replacements, long forOriginalId) throws DataStoreException;
	
	ScheduleOverride readOverride(long id) throws DataStoreException;
	List<ScheduleOverride> readAllUserOverrides(String username) throws DataStoreException;
	List<ScheduleOverride> readAllUserOverridesFor(String username, String subdepartment, int semester) throws DataStoreException;

	void updateOriginal(ScheduleItem original) throws DataStoreException;
	void updateReplacements(List<OverrideData> replacements) throws DataStoreException;

	void deleteReplacements(List<Long> replacementIds) throws DataStoreException;

	void deleteOriginal(Long id) throws DataStoreException;
}
