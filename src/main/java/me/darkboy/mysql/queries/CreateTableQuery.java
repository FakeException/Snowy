package me.darkboy.mysql.queries;

import me.darkboy.mysql.utils.QueryUtils;

import java.util.ArrayList;
import java.util.List;

public class CreateTableQuery {
   private final String table;
   private boolean ifNotExists = false;
   private final List<String> columns = new ArrayList<>();
   private String primaryKey;

   public CreateTableQuery(String table) {
      this.table = table;
   }

   public CreateTableQuery ifNotExists() {
      this.ifNotExists = true;
      return this;
   }

   public CreateTableQuery column(String column, String settings) {
      this.columns.add(column + " " + settings);
      return this;
   }

   public CreateTableQuery primaryKey(String column) {
      this.primaryKey = column;
      return this;
   }

   public String build() {
      StringBuilder builder = new StringBuilder();
      builder.append("CREATE TABLE ");
      if (this.ifNotExists) {
         builder.append("IF NOT EXISTS ");
      }

      builder.append(this.table).append(" (").append(QueryUtils.separate(this.columns, ","));
      if (this.primaryKey != null) {
         builder.append(",PRIMARY KEY(");
         builder.append(this.primaryKey);
         builder.append(")");
      }

      builder.append(")");
      return builder.toString();
   }
}
    