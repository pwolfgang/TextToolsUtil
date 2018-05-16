package edu.temple.cla.papolicy.wolfgang.texttools.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A simple data source for getting database connections.
 */
public class SimpleDataSource {

    /**
     * Initializes the data source.
     *
     * @param fileName the name of the property file that contains the database
     * driver, URL, username, and password Or is the Name of an ODBC datasource.
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public SimpleDataSource(String fileName)
            throws IOException, ClassNotFoundException {
        File file = new File(fileName);
        Properties props = new Properties();
        FileInputStream in = new FileInputStream(file);
        props.load(in);

        String driver = props.getProperty("jdbc.driver");
        url = props.getProperty("jdbc.url");
        username = props.getProperty("jdbc.username");
        password = props.getProperty("jdbc.password");
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }

        Class.forName(driver);
    }

    /**
     * Gets a connection to the database.
     *
     * @return the database connection
     * @throws java.sql.SQLException
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username.trim(), password.trim());
    }

    private final String url;
    private String username;
    private String password;
}
