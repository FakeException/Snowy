package me.darkboy.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

   private HikariDataSource dataSource;
   private final String host;
   private final String port;
   private final String username;
   private final String password;
   private final String database;
   private final int connectionTimeout;
   private final int maximumPoolSize;

   public ConnectionManager(String host, String port, String username, String password, String database, int connectionTimeout, int maximumPoolSize) {
      this.host = host;
      this.port = port;
      this.username = username;
      this.password = password;
      this.database = database;
      this.connectionTimeout = connectionTimeout;
      this.maximumPoolSize = maximumPoolSize;
   }

   public ConnectionManager(String host, String port, String username, String password, String database) {
      this.host = host;
      this.port = port;
      this.username = username;
      this.password = password;
      this.database = database;
      this.connectionTimeout = 5000;
      this.maximumPoolSize = 10;
   }

   public Connection getConnection() {
      if (this.isClosed()) {
         throw new IllegalStateException("Connection is not open.");
      } else {
         try {
            return this.dataSource.getConnection();
         } catch (SQLException var2) {
            var2.printStackTrace();
            return null;
         }
      }
   }

   public boolean open() {
      try {
         HikariConfig config = new HikariConfig();
         config.setUsername(this.username);
         config.setPassword(this.password);
         config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s", this.host, this.port, this.database));
         config.setConnectionTimeout(this.connectionTimeout);
         config.setMaximumPoolSize(this.maximumPoolSize);
         this.dataSource = new HikariDataSource(config);
         return true;
      } catch (Exception var2) {
         var2.printStackTrace();
         return false;
      }
   }

   public void close() {
      if (this.isClosed()) {
         throw new IllegalStateException("Connection is not open.");
      } else {
         this.dataSource.close();
      }
   }

   public boolean isClosed() {
      return this.dataSource == null || this.dataSource.isClosed();
   }
}
    