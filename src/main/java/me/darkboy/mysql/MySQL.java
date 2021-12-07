package me.darkboy.mysql;

import me.darkboy.Snowy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySQL {

   private ConnectionManager connectionManager;
   private final ExecutorService threadPool;

   public MySQL() {
      this.threadPool = Snowy.getExecutorService();
   }

   public MySQL(int maxThreads) {
      this.threadPool = Executors.newFixedThreadPool(maxThreads);
   }

   public ConnectionManager getConnectionManager() {
      return this.connectionManager;
   }

   public ExecutorService getThreadPool() {
      return this.threadPool;
   }

   public boolean connect(String host, String port, String username, String password, String database) {
      this.connectionManager = new ConnectionManager(host, port, username, password, database);
      return this.connectionManager.open();
   }

   public void disconnect() {
      if (this.connectionManager != null) {
         this.connectionManager.close();
      }

      if (this.threadPool != null) {
         this.threadPool.shutdown();
      }

   }
}