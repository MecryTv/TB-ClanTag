package de.mecrytv.tBClanTag.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.mecrytv.tBClanTag.TBClanTag;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private HikariDataSource dataSource;

    public DatabaseManager() {
        String host = TBClanTag.getInstance().getConfig().getString("mysql.host", "91.218.67.51");
        int port = TBClanTag.getInstance().getConfig().getInt("mysql.port", 3306);
        String database = TBClanTag.getInstance().getConfig().getString("mysql.database", "ClanTags");
        String username = TBClanTag.getInstance().getConfig().getString("mysql.user", "minecraft");
        String password = TBClanTag.getInstance().getConfig().getString("mysql.password", "MyMcPW05");

        HikariConfig mysqlConf = new HikariConfig();

        mysqlConf.setUsername(username);
        mysqlConf.setPassword(password);

        mysqlConf.setConnectionTimeout(2000);
        mysqlConf.setMaximumPoolSize(10);
        mysqlConf.setDriverClassName("com.mysql.cj.jdbc.Driver");

        String jdbcURL = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&serverTimezone=Europe/Berlin&useSSL=false";
        mysqlConf.setJdbcUrl(jdbcURL);

        dataSource = new HikariDataSource(mysqlConf);

        try {
            Connection connection = getConnection();
            closeConnection(connection);
        } catch (SQLException e) {
            throw new IllegalStateException("MySQL-Initialisierung fehlgeschlagen", e);
        }
    }


    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeConnection(Connection connection) {
        dataSource.evictConnection(connection);
    }

    public void shutDown() {
        dataSource.close();
    }

}
