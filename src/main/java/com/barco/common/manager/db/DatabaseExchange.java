package com.barco.common.manager.db;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author Nabeel Ahmed
 */
@Component
public class DatabaseExchange {

    private Logger logger = LogManager.getLogger(DatabaseExchange.class);

    // mysql,pg,sql server
    private String dataBaseType;
    private String driver; // driver for db base on mysql
    private String databaseServer; // ip or else db path
    private Long databasePort; // port of db
    private String databaseName; // db name
    private String databaseUser;
    private String databasePassword;

    public DatabaseExchange() {}

    public String getDataBaseType() {
        return dataBaseType;
    }
    public void setDataBaseType(String dataBaseType) {
        this.dataBaseType = dataBaseType;
    }

    public String getDriver() {
        return driver;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDatabaseServer() {
        return databaseServer;
    }
    public void setDatabaseServer(String databaseServer) {
        this.databaseServer = databaseServer;
    }

    public Long getDatabasePort() {
        return databasePort;
    }
    public void setDatabasePort(Long databasePort) {
        this.databasePort = databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }
    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }
    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
