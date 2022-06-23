package de.warsteiner.jobs.utils.database.hikari;

public record HikariAuthentication (String host, int port, String database, String user, String password) {


}
