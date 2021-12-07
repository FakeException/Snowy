package me.darkboy.mysql.queries;

import me.darkboy.mysql.MySQL;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {

   private final MySQL mysql;
   private final Connection connection;
   private final PreparedStatement statement;

   public Query(MySQL mysql, String sql) throws SQLException {
      this.mysql = mysql;
      this.connection = mysql.getConnectionManager().getConnection();
      this.statement = this.connection.prepareStatement(sql);
   }

   public void setParameter(int index, Object value) throws SQLException {
      this.statement.setObject(index, value);
   }

   public void addBatch() throws SQLException {
      if (this.connection.getAutoCommit()) {
         this.connection.setAutoCommit(false);
      }

      this.statement.addBatch();
   }

   public int executeUpdate() throws SQLException {
      int var1;
      try {
         var1 = this.statement.executeUpdate();
      } finally {
         if (this.statement != null) {
            this.statement.close();
         }

         if (this.connection != null) {
            this.connection.close();
         }

      }

      return var1;
   }

   public ResultSet executeQuery() throws SQLException {
      CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();

      try {
         ResultSet resultSet = this.statement.executeQuery();
         Throwable var3 = null;

         try {
            rowSet.populate(resultSet);
         } catch (Throwable var19) {
            var3 = var19;
            throw var19;
         } finally {
            if (resultSet != null) {
               if (var3 != null) {
                  try {
                     resultSet.close();
                  } catch (Throwable var18) {
                     var3.addSuppressed(var18);
                  }
               } else {
                  resultSet.close();
               }
            }

         }
      } finally {
         if (this.statement != null) {
            this.statement.close();
         }

         if (this.connection != null) {
            this.connection.close();
         }

      }

      return rowSet;
   }

   public int[] executeBatch() throws SQLException {
      int[] var1;
      try {
         var1 = this.statement.executeBatch();
      } finally {
         if (this.statement != null) {
            this.statement.close();
         }

         if (this.connection != null) {
            this.connection.commit();
            this.connection.close();
         }

      }

      return var1;
   }

   public void executeUpdateAsync(final Callback<Integer, SQLException> callback) {
      this.mysql.getThreadPool().submit(() -> {
         try {
            int rowsChanged = Query.this.executeUpdate();
            if (callback != null) {
               callback.call(rowsChanged, null);
            }
         } catch (SQLException var2) {
            if (callback != null) {
               callback.call(0, var2);
            } else {
               var2.printStackTrace();
            }
         }

      });
   }

   public void executeUpdateAsync() {
      this.executeUpdateAsync(null);
   }

   public void executeQueryAsync(final Callback<ResultSet, SQLException> callback) {
      this.mysql.getThreadPool().submit(() -> {
         try {
            ResultSet rs = this.executeQuery();
            callback.call(rs, null);
         } catch (SQLException var3) {
            callback.call(null, var3);
         }

      });
   }

   public void executeQueryAsync() {
      this.executeQueryAsync(null);
   }

   public void executeBatchAsync() {
      this.executeBatchAsync(null);
   }

   public void executeBatchAsync(final Callback<int[], SQLException> callback) {
      this.mysql.getThreadPool().submit(() -> {
         try {
            int[] rowsChanged = this.executeBatch();
            if (callback != null) {
               callback.call(rowsChanged, null);
            }
         } catch (SQLException var3) {
            if (callback != null) {
               callback.call(null, var3);
            }
         }

      });
   }

   public void rollback() throws SQLException {
      if (this.connection != null) {
         this.connection.rollback();
      }

   }
}