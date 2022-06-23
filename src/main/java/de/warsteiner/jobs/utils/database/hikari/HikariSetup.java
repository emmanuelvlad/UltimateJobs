package de.warsteiner.jobs.utils.database.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.database.statements.ConnectionType;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class HikariSetup implements ConnectionType {

    private HikariDataSource dataSource;

    public HikariSetup init(SQLTypes types, HikariAuthentication authentication, int timeOut, int poolSize) {
        this.dataSource = new HikariDataSource(getDataProperties(types, authentication, timeOut, poolSize));
        UltimateJobs.getPlugin().getSQLStatementAPI().setType(this);
        return this;
    }

    public HikariConfig getDataProperties(SQLTypes types, HikariAuthentication authentication, int timeOut, int poolSize) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(types.getDriverName());
        config.setUsername(authentication.user());
        config.setPassword(authentication.password());
        config.setJdbcUrl(generateURL(types.getDriverURL(), authentication));
        config.setConnectionTimeout(timeOut);
        config.setMaximumPoolSize(poolSize);
        return config;
    }

    public String generateURL(String jdurl, HikariAuthentication authentication) {
        String url = jdurl.replace("{host}", authentication.host());
        url = url.replace("{port}", String.valueOf(authentication.port()));
        url = url.replace("{database}", authentication.database());
        return url;
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public void close(){
        if(dataSource == null) return;
        if(isClosed()) return;
        dataSource.close();
    }

    public boolean isClosed(){
        return dataSource.isClosed();
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
