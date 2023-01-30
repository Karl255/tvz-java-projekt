package hr.java.projektnizadatak.data;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface QueryResultMapper<T> {
	T map(ResultSet resultSet) throws SQLException;
}
