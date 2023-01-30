package hr.java.projektnizadatak.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementModifier {
	void modify(PreparedStatement ps) throws SQLException;
}
