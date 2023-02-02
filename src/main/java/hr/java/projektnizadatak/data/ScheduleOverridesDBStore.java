package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.OverridesStore;
import hr.java.projektnizadatak.application.entities.ClassType;
import hr.java.projektnizadatak.application.entities.OverrideData;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.shared.Util;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
public class ScheduleOverridesDBStore implements OverridesStore {
	// create

	@Override
	public long createOriginal(ScheduleItem original, String forSubdepartment, int forSemester, String forUsername) throws DataStoreException {
		final String query = """
			INSERT INTO override_original(
				subdepartment,
				semester,
				username,
				courseName,
				className,
				professor,
				classType,
				classroom,
				note,
				group_,
				weekday,
				start_,
				end_
			) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
			""";

		return DataBaseAccess.getInstance().createGeneric(
			query,
			ps -> {
				ps.setString(1, forSubdepartment);
				ps.setInt(2, forSemester);
				ps.setString(3, forUsername);
				ps.setString(4, original.courseName());
				ps.setString(5, original.className());
				ps.setString(6, original.professor());
				ps.setString(7, original.classType().getName());
				ps.setString(8, original.classroom());
				ps.setString(9, original.note());
				ps.setString(10, original.group());
				ps.setInt(11, original.weekday().getValue());
				ps.setTime(12, Time.valueOf(original.start()));
				ps.setTime(13, Time.valueOf(original.end()));
			}
		);
	}

	@Override
	public List<Long> createReplacements(List<OverrideData> replacements, long forOriginalId) throws DataStoreException {
		final String query = """
			INSERT INTO override_replacement(
				identifier_id,
				professor,
				classType,
				classroom,
				note,
				group_,
				start_,
				end_
			) VALUES (?, ?, ?, ?, ?, ?, ?, ?);
			""";

		return DataBaseAccess.getInstance().createManyGeneric(
			query,
			replacements.stream().map(replacement -> ps -> {
				ps.setLong(1, forOriginalId);
				ps.setString(2, replacement.professor());
				ps.setString(3, replacement.classType().getName());
				ps.setString(4, replacement.classroom());
				ps.setString(5, replacement.note());
				ps.setString(6, replacement.group());
				ps.setTime(7, Time.valueOf(replacement.start()));
				ps.setTime(8, Time.valueOf(replacement.end()));
			})
		);
	}

	// read

	@Override
	public ScheduleOverride readOverride(long id) throws DataStoreException {
		var db = DataBaseAccess.getInstance();
		var original = db.selectSingleGeneric(
			"SELECT * FROM override_original WHERE id = ?;",
			ps -> ps.setLong(1, id),
			ScheduleOverridesDBStore::toOverrideOriginal);


		var replacements = db.selectGeneric(
			"SELECT * FROM override_replacement WHERE identifier_id = ?;",
			ps -> ps.setLong(1, id),
			ScheduleOverridesDBStore::toOverrideReplacement
		);

		return new ScheduleOverride(original, replacements);
	}

	@Override
	public List<ScheduleOverride> readAllUserOverrides(String username) throws DataStoreException {
		var db = DataBaseAccess.getInstance();
		var originals = db.selectGeneric(
			"SELECT * FROM override_original WHERE username = ?;",
			ps -> {
				ps.setString(1, username);
			},
			ScheduleOverridesDBStore::toOverrideOriginal);

		try {
			return originals.stream().map(original -> {
				try {
					return new ScheduleOverride(
						original,
						db.selectGeneric(
							"SELECT * FROM override_replacement WHERE identifier_id = ?;",
							ps -> ps.setLong(1, original.originalId()),
							ScheduleOverridesDBStore::toOverrideReplacement
						)
					);
				} catch (DataStoreException e) {
					throw new RuntimeException(e);
				}
			}).toList();
		} catch (RuntimeException e) {
			if (e.getCause() instanceof DataStoreException dataStoreException) {
				throw dataStoreException;
			}

			throw e;
		}
	}

	@Override
	public List<ScheduleOverride> readAllUserOverridesFor(String username, String subdepartment, int semester) throws DataStoreException {
		var db = DataBaseAccess.getInstance();
		var originals = db.selectGeneric(
			"SELECT * FROM override_original WHERE username = ? AND subdepartment = ? AND semester = ?;",
			ps -> {
				ps.setString(1, username);
				ps.setString(2, subdepartment);
				ps.setLong(3, semester);
			},
			ScheduleOverridesDBStore::toOverrideOriginal);

		try {
			return originals.stream().map(original -> {
				try {
					return new ScheduleOverride(
						original,
						db.selectGeneric(
							"SELECT * FROM override_replacement WHERE identifier_id = ?;",
							ps -> ps.setLong(1, original.originalId()),
							ScheduleOverridesDBStore::toOverrideReplacement
						)
					);
				} catch (DataStoreException e) {
					throw new RuntimeException(e);
				}
			}).toList();
		} catch (RuntimeException e) {
			if (e.getCause() instanceof DataStoreException dataStoreException) {
				throw dataStoreException;
			}

			throw e;
		}
	}

	// update

	public void updateOriginal(ScheduleItem origianl) throws DataStoreException {
		final String query = """
			UPDATE override_original SET
			courseName = ?,
			className = ?,
			professor = ?,
			classType = ?,
			classroom = ?,
			note = ?,
			group_ = ?,
			weekday = ?,
			start_ = ?,
			end_ = ?
			WHERE id = ?;
			""";

		DataBaseAccess.getInstance().updateGeneric(
			query,
			ps -> {
				ps.setString(1, origianl.courseName());
				ps.setString(2, origianl.className());
				ps.setString(3, origianl.professor());
				ps.setString(4, origianl.classType().getName());
				ps.setString(5, origianl.classroom());
				ps.setString(6, origianl.note());
				ps.setString(7, origianl.group());
				ps.setInt(8, origianl.weekday().getValue());
				ps.setTime(9, Time.valueOf(origianl.start()));
				ps.setTime(10, Time.valueOf(origianl.end()));
				ps.setLong(11, origianl.originalId());
			}
		);
	}

	@Override
	public void updateReplacements(List<OverrideData> replacements) throws DataStoreException {
		final String query = """
			UPDATE override_replacement
			SET
			professor = ?,
			classType = ?,
			classroom = ?,
			note = ?,
			group_ = ?,
			start_ = ?,
			end_ = ?
			WHERE id = ?;
			""";

		DataBaseAccess.getInstance().updateManyGeneric(
			query,
			replacements.stream().map(replacement -> ps -> {
				ps.setString(1, replacement.professor());
				ps.setString(2, replacement.classType().getName());
				ps.setString(3, replacement.classroom());
				ps.setString(4, replacement.note());
				ps.setString(5, replacement.group());
				ps.setTime(6, Time.valueOf(replacement.start()));
				ps.setTime(7, Time.valueOf(replacement.end()));
				ps.setLong(8, replacement.id());
			})
		);
	}

	// delete


	@Override
	public void deleteOriginal(Long id) throws DataStoreException {
		DataBaseAccess.getInstance().updateGeneric(
			"DELETE FROM override_original WHERE id = ?;",
			ps -> ps.setLong(1, id)
		);
	}

	@Override
	public void deleteReplacements(List<Long> replacementIds) throws DataStoreException {
		DataBaseAccess.getInstance().updateManyGeneric(
			"DELETE FROM override_replacement WHERE id = ?;",
			replacementIds.stream().map(id -> ps -> ps.setLong(1, id))
		);
	}

	@Override
	public void deleteForUser(String username) throws DataStoreException {
		var originalIds = DataBaseAccess.getInstance().selectGeneric(
			"SELECT id FROM override_original WHERE username = ?;",
			ps -> ps.setString(1, username),
			rs -> rs.getLong(1)
		);
		
		for (var id : originalIds) {
			DataBaseAccess.getInstance().updateGeneric(
				"DELETE FROM override_replacement WHERE identifier_id = ?;",
				ps -> ps.setLong(1, id)
			);
		}
		
		DataBaseAccess.getInstance().updateGeneric(
			"DELETE FROM override_original WHERE username = ?;",
			ps -> ps.setString(1, username)
		);
	}

	// util

	private static ScheduleItem toOverrideOriginal(ResultSet rs) throws SQLException {
		return new ScheduleItem(
			rs.getLong("id"),
			null,
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

	private static OverrideData toOverrideReplacement(ResultSet rs) throws SQLException {
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
