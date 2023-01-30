package hr.java.projektnizadatak.data;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class DataBaseAccess {
	private static final String DB_PROPERTIES_PATH = "/db.properties";
	private static DataBaseAccess instance = null;

	private final String dbUrl;
	private final String dbUsername;
	private final String dbPassword;

	private DataBaseAccess() {
		try (var stream = DataBaseAccess.class.getResourceAsStream(DB_PROPERTIES_PATH)) {
			var properties = new Properties();
			properties.load(stream);

			dbUrl = properties.getProperty("url");
			dbUsername = properties.getProperty("username");
			dbPassword = properties.getProperty("password");
		} catch (IOException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	public static DataBaseAccess getInstance() {
		if (instance == null) {
			instance = new DataBaseAccess();
		}
		
		return instance;
	}

	public <T> T runOnConnection(GenericQuery<T> query) {
		try (var conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
			return query.run(conn);
		} catch (SQLException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	public <T> List<T> selectGeneric(String query, PreparedStatementModifier psm, QueryResultMapper<T> resultMapper) {
		return runOnConnection(conn -> {
			try (var ps = conn.prepareStatement(query)) {
				if (psm != null) {
					psm.modify(ps);
				}

				ps.execute();
				var items = new ArrayList<T>();

				try (var results = ps.getResultSet()) {
					while (results.next()) {
						items.add(resultMapper.map(results));
					}
				}

				return items;
			}
		});
	}

	public long insertGeneric(String query, PreparedStatementModifier psm) {
		return runOnConnection(conn -> {
			try (var ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				if (psm != null) {
					psm.modify(ps);
				}

				ps.execute();
				
				var keys = ps.getGeneratedKeys();
				keys.next();
				return keys.getLong(1);
			}
		});
	}
}
