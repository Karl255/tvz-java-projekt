package hr.java.projektnizadatak.data;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
interface GenericQuery<T> {
	T run(Connection conn) throws SQLException;
}
