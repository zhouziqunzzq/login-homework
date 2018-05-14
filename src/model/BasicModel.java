package model;

import util.DatabaseHelper;

import java.sql.Connection;
import java.util.Properties;

public class BasicModel {
    private Connection conn;
    private Properties properties;

    public BasicModel() {
        conn = null;
    }

    public BasicModel(Properties properties) {
        this.properties = properties;
        getConn();
    }

    /**
     * Before call getConn, be sure to set properties properly.
     *
     * @return DB connection.
     */
    public Connection getConn() {
        if (conn == null) {
            conn = DatabaseHelper.getConnFromConfig(properties);
        }
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
