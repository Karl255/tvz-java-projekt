package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.OverridesStore;
import hr.java.projektnizadatak.application.entities.ClassType;
import hr.java.projektnizadatak.application.entities.OverrideData;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.shared.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.List;

public class ScheduleOverridesDBStore implements OverridesStore {
	@Override
	public void createSigle(ScheduleOverride scheduleOverride) {
		// TODO
		throw new RuntimeException();
	}

	@Override
	public ScheduleOverride readOverride(long id) {
		var db = DataBaseAccess.getInstance();
		var original = db.selectGeneric(
			"SELECT * FROM override_original WHERE id = ?;",
			ps -> {
				ps.setLong(1, id);
			},
			ScheduleOverridesDBStore::mapOverrideOriginal)
			.get(0);


		var replacements = db.selectGeneric(
			"SELECT * FROM override_replacement WHERE identifier_id = ?;",
			ps -> ps.setLong(1, id),
			ScheduleOverridesDBStore::mapOverrideReplacement
		);

		return new ScheduleOverride(original, replacements);
	}

	@Override
	public List<ScheduleOverride> readAllOverridesFor(String subdepartment, int semester) {
		var db = DataBaseAccess.getInstance();
		var originals = db.selectGeneric(
			"SELECT * FROM override_original WHERE subdepartment = ? AND semester = ?;",
			ps -> {
				ps.setString(1, subdepartment);
				ps.setLong(2, semester);
			},
			ScheduleOverridesDBStore::mapOverrideOriginal);

		return originals.stream()
			.map(original ->
				new ScheduleOverride(
					original,
					db.selectGeneric(
						"SELECT * FROM override_replacement WHERE identifier_id = ?;",
						ps -> ps.setLong(1, original.dbId()),
						ScheduleOverridesDBStore::mapOverrideReplacement
					)
				)
			).toList();
	}


	@Override
	public void updateSingle(ScheduleOverride oldScheduleOverride, ScheduleOverride newScheduleOverride) {
		// TODO
	}

	@Override
	public void deleteSingle(ScheduleOverride scheduleOverride) {
		// TODO
	}

	private static ScheduleItem mapOverrideOriginal(ResultSet rs) throws SQLException {
		return new ScheduleItem(
			rs.getLong("id"),
			rs.getString("courseName"),
			rs.getString("className"),
			rs.getString("professor"),
			ClassType.parse(rs.getString("classType")),
			rs.getString("classroom"),
			rs.getString("note"),
			rs.getString("group_"),
			DayOfWeek.of(rs.getInt("weekday")),
			rs.getTime("start_").toLocalTime(),
			rs.getTime("end_").toLocalTime(),
			true
		);
	}

	private static OverrideData mapOverrideReplacement(ResultSet rs) throws SQLException {
		return new OverrideData(
			rs.getLong("id"),
			rs.getString("professor"),
			Util.nullCoalesc(rs.getString("classType"), ClassType::parse),
			rs.getString("classroom"),
			rs.getString("note"),
			rs.getString("group_"),
			Util.nullCoalesc(rs.getTime("start_"), Time::toLocalTime),
			Util.nullCoalesc(rs.getTime("end_"), Time::toLocalTime)
		);
	}
}
