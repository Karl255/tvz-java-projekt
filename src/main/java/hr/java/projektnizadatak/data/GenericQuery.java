package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.shared.exceptions.DataStoreException;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
interface GenericQuery<T> {
	T run(Connection conn) throws DataStoreException, SQLException;
}
