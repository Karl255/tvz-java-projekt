package hr.java.projektnizadatak.data;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

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

	public long createGeneric(String query, PreparedStatementModifier psm) {
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

	public List<Long> createManyGeneric(String query, Stream<PreparedStatementModifier> psmStream) {
		return runOnConnection(conn -> {
			conn.setAutoCommit(false);
			try {
				var ids = psmStream
					.map(psm -> {
						try (var ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
							if (psm != null) {
								psm.modify(ps);
							}

							ps.execute();
							
							var keys = ps.getGeneratedKeys();
							keys.next();
							return keys.getLong(1);
						} catch (SQLException e) {
							// propagating the SQLException through the stream lambda
							throw new RuntimeException(e);
						}
					}).toList();

				conn.commit();

				return ids;
			} catch (RuntimeException e) {
				if (e.getCause() instanceof SQLException sqlException) {
					// re-throw propagated SQLException
					throw sqlException;
				}

				throw e;
			}
		});
	}
	
	public <T> T selectSingleGeneric(String query, PreparedStatementModifier psm, QueryResultMapper<T> resultMapper) {
		return runOnConnection(conn -> {
			try (var ps = conn.prepareStatement(query)) {
				if (psm != null) {
					psm.modify(ps);
				}

				try (var results = ps.executeQuery()) {
					if (results.next()) {
						return resultMapper.map(results);
					}

					return null;
				}
			}
		});
	}

	public <T> List<T> selectGeneric(String query, PreparedStatementModifier psm, QueryResultMapper<T> resultMapper) {
		return runOnConnection(conn -> {
			try (var ps = conn.prepareStatement(query)) {
				if (psm != null) {
					psm.modify(ps);
				}

				var items = new ArrayList<T>();

				try (var results = ps.executeQuery()) {
					while (results.next()) {
						items.add(resultMapper.map(results));
					}
				}

				return items;
			}
		});
	}

	public int updateGeneric(String query, PreparedStatementModifier psm) {
		return runOnConnection(conn -> {
			try (var ps = conn.prepareStatement(query)) {
				if (psm != null) {
					psm.modify(ps);
				}

				return ps.executeUpdate();
			}
		});
	}

	public int updateManyGeneric(String query, Stream<PreparedStatementModifier> psmStream) {
		return runOnConnection(conn -> {
			conn.setAutoCommit(false);
			try {
				int updated = psmStream
					.mapToInt(psm -> {
						try (var ps = conn.prepareStatement(query)) {
							if (psm != null) {
								psm.modify(ps);
							}

							return ps.executeUpdate();
						} catch (SQLException e) {
							// propagating the SQLException through the stream lambda
							throw new RuntimeException(e);
						}
					}).sum();

				conn.commit();

				return updated;
			} catch (RuntimeException e) {
				if (e.getCause() instanceof SQLException sqlException) {
					// re-throw propagated SQLException
					throw sqlException;
				}

				throw e;
			}
		});
	}

	public void otherQueryGeneric(String query, PreparedStatementModifier psm) {
		runOnConnection(conn -> {
			try (var ps = conn.prepareStatement(query)) {
				if (psm != null) {
					psm.modify(ps);
				}

				ps.execute();

				return null;
			}
		});
	}

	public void otherQueryManyGeneric(String query, Stream<PreparedStatementModifier> psmStream) {
		runOnConnection(conn -> {
			conn.setAutoCommit(false);
			try {
				psmStream.forEach(psm -> {
					try (var ps = conn.prepareStatement(query)) {
						if (psm != null) {
							psm.modify(ps);
						}

						ps.execute();
					} catch (SQLException e) {
						// propagating the SQLException through the stream lambda
						throw new RuntimeException(e);
					}
				});

				conn.commit();
				
				return null;
			} catch (RuntimeException e) {
				if (e.getCause() instanceof SQLException sqlException) {
					// re-throw propagated SQLException
					throw sqlException;
				}

				throw e;
			}
		});
	}
}
