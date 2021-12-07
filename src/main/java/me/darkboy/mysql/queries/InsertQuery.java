package me.darkboy.mysql.queries;

import me.darkboy.mysql.utils.QueryUtils;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class InsertQuery {
   private final String table;
   private final LinkedHashMap<String, String> values = new LinkedHashMap<>();
   private final LinkedHashMap<String, String> duplicateValues = new LinkedHashMap<>();
   private boolean onDuplicateKey = false;

   public InsertQuery(String table) {
      this.table = table;
   }

   public InsertQuery value(String column, String value) {
      this.values.put(column, value);
      return this;
   }

   public InsertQuery value(String column) {
      this.value(column, "?");
      return this;
   }

   public InsertQuery onDuplicateKeyUpdate() {
      this.onDuplicateKey = true;
      return this;
   }

   public InsertQuery set(String column, String value) {
      this.duplicateValues.put(column, value);
      return this;
   }

   public InsertQuery set(String column) {
      this.set(column, "VALUES(" + column + ")");
      return this;
   }

   public String build() {
      StringBuilder builder = new StringBuilder();
      builder.append("INSERT INTO ").append(this.table).append(" (").append(QueryUtils.separate(this.values.keySet(), ",")).append(")").append(" VALUES (").append(QueryUtils.separate(this.values.values(), ",")).append(")");
      if (this.onDuplicateKey) {
         builder.append(" ON DUPLICATE KEY UPDATE ");
         String separator = "";

         for (Entry<String, String> stringStringEntry : this.duplicateValues.entrySet()) {
            String column = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();
            builder.append(separator).append(column).append("=").append(value);
            separator = ",";
         }
      }

      return builder.toString();
   }
}