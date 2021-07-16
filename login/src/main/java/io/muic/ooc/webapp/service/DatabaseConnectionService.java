package io.muic.ooc.webapp.service;


import com.zaxxer.hikari.HikariDataSource;
import io.muic.ooc.webapp.config.ConfigProperties;
import io.muic.ooc.webapp.config.ConfigurationLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnectionService {

    private final HikariDataSource ds;

    /**
     * Database connection pool using Hikari library
     * The secret and variables are loaded from disk
     * File config.properties isn't committed to git repo TODO: Shouldn't but I did anyway
     */
    public DatabaseConnectionService() {
        ds = new HikariDataSource();
        ds.setMaximumPoolSize(20);

        ConfigProperties configProperties = ConfigurationLoader.load();
        if (configProperties == null) {
            throw new RuntimeException("Unable to read config.properties");
        }
        ds.setDriverClassName(configProperties.getDatabaseDriverClassName());
        ds.setJdbcUrl(configProperties.getDatabaseConnectionUrl());
        ds.addDataSourceProperty("user", configProperties.getDatabaseUsername());
        ds.addDataSourceProperty("password", configProperties.getDatabasePassword());
        ds.setAutoCommit(false);

    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }



}
