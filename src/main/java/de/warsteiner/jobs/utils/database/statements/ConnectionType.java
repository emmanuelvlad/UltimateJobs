package de.warsteiner.jobs.utils.database.statements;

import java.sql.Connection;

public interface ConnectionType {

    Connection getConnection();
}
