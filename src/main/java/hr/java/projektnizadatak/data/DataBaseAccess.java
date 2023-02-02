package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.shared.exceptions.DataStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

class DataBaseAccess {
	private static final Logger logger = LoggerFactory.getLogger(DataBaseAccess.class);
	private static final String DB_PROPERTIES_PATH = "/db.properties";
	private static DataBaseAccess instance = null;

	private final String dbUrl;
	private final String dbUsername;
	private final String dbPassword;

	private DataBaseAccess() throws DataStoreException {
		try (var stream = DataBaseAccess.class.getResourceAsStream(DB_PROPERTIES_PATH)) {
			var properties = new Properties();
			properties.load(stream);

			dbUrl = properties.getProperty("url");
			dbUsername = properties.getProperty("username");
			dbPassword = properties.getProperty("password");
		} catch (IOException e) {
			String m = "Failed to read database configuration file";
			logger.error(m);

			throw new DataStoreException(m, e);
		}
	}

	public static DataBaseAccess getInstance() throws DataStoreException {
		if (instance == null) {
			instance = new DataBaseAccess();
		}

		return instance;
	}

	public <T> T runOnConnection(GenericQuery<T> query) throws DataStoreException {
		try (var conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
			return query.run(conn);
		} catch (SQLException e) {
			String m = "Failed to connect to database";
			logger.error(m);

			throw new DataStoreException(m, e);
		}
	}

	public long createGeneric(String query, PreparedStatementModifier psm) throws DataStoreException {
		return runOnConnection(conn -> {
			try (var ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				if (psm != null) {
					psm.modify(ps);
				}

				ps.execute();

				var keys = ps.getGeneratedKeys();
				keys.next();
				return keys.getLong(1);
			} catch (SQLException e) {
				String m = "Database error occured while trying to execute query";
				logger.error(m);
				
				throw new DataStoreException(m, e);
			}
		});
	}

	public List<Long> createManyGeneric(String query, Stream<PreparedStatementModifier> psmStream) throws DataStoreException {
		return runOnConnection(conn -> {
			conn.setAutoCommit(false);
			try {
				var ids = psmStream.map(psm -> {
					try (var ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
						if (psm != null) {
							psm.modify(ps);
						}

						ps.execute();

						var keys = ps.getGeneratedKeys();
						keys.next();
						return keys.getLong(1);
					} catch (SQLException e) {
						String m = "Database error occured while trying to execute query";
						logger.error(m);

						// propagating the DataStoreException through the stream lambda
						throw new RuntimeException(new DataStoreException(m, e));
					}
				}).toList();

				conn.commit();

				return ids;
			} catch (SQLException e){
				String m = "Database error occured while trying to commit changes";
				logger.error(m);
				
				throw new DataStoreException(m, e);
			} catch (RuntimeException e) {
				if (e.getCause() instanceof DataStoreException dataStoreException) {
					// re-throw propagated DataStoreException as cause
					throw dataStoreException;
				}

				throw e;
			}
		});
	}

	public <T> T selectSingleGeneric(String query, PreparedStatementModifier psm, QueryResultMapper<T> resultMapper) throws DataStoreException {
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
			} catch (SQLException e) {
				String m = "Database error occured while trying to execute query";
				logger.error(m);
				
				throw new DataStoreException(e);
			}
		});
	}

	public <T> List<T> selectGeneric(String query, PreparedStatementModifier psm, QueryResultMapper<T> resultMapper) throws DataStoreException {
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
			} catch (SQLException e) {
				String m = "Database error occured while trying to execute query";
				logger.error(m);

				throw new DataStoreException(m, e);
			}
		});
	}

	public int updateGeneric(String query, PreparedStatementModifier psm) throws DataStoreException {
		return runOnConnection(conn -> {
			try (var ps = conn.prepareStatement(query)) {
				if (psm != null) {
					psm.modify(ps);
				}

				return ps.executeUpdate();
			} catch (SQLException e) {
				String m = "Database error occured while trying to execute query";
				logger.error(m);

				throw new DataStoreException(m, e);
			}
		});
	}

	public int updateManyGeneric(String query, Stream<PreparedStatementModifier> psmStream) throws DataStoreException {
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
							String m = "Database error occured while trying to execute query";
							logger.error(m);

							// propagating the DataStoreException through the stream lambda
							throw new RuntimeException(new DataStoreException(m, e));
						}
					}).sum();

				conn.commit();

				return updated;
			} catch (SQLException e){
				String m = "Database error occured while trying to commit changes";
				logger.error(m);

				throw new DataStoreException(m, e);
			} catch (RuntimeException e) {
				if (e.getCause() instanceof DataStoreException dataStoreException) {
					// re-throw propagated DataStoreException as cause
					throw dataStoreException;
				}

				throw e;
			}
		});
	}

	public void otherQueryGeneric(String query, PreparedStatementModifier psm) throws DataStoreException {
		runOnConnection(conn -> {
			try (var ps = conn.prepareStatement(query)) {
				if (psm != null) {
					psm.modify(ps);
				}

				ps.execute();

				return null;
			} catch (SQLException e) {
				String m = "Database error occured while trying to execute query";
				logger.error(m);

				throw new DataStoreException(m, e);
			}
		});
	}

	public void otherQueryManyGeneric(String query, Stream<PreparedStatementModifier> psmStream) throws DataStoreException {
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
						String m = "Database error occured while trying to execute query";
						logger.error(m);

						// propagating the DataStoreException through the stream lambda
						throw new RuntimeException(new DataStoreException(m, e));
					}
				});

				conn.commit();

				return null;
			} catch (SQLException e){
				String m = "Database error occured while trying to commit changes";
				logger.error(m);

				throw new DataStoreException(m, e);
			} catch (RuntimeException e) {
				if (e.getCause() instanceof DataStoreException dataStoreException) {
					// re-throw propagated DataStoreException as cause
					throw dataStoreException;
				}

				throw e;
			}
		});
	}
}
