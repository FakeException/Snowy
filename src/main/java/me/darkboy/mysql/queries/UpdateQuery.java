package me.darkboy.mysql.queries;

import me.darkboy.mysql.utils.QueryUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class UpdateQuery {

   private final String table;
   private final LinkedHashMap<String, String> values = new LinkedHashMap<>();
   private final List<String> wheres = new ArrayList<>();

   public UpdateQuery(String table) {
      this.table = table;
   }

   public UpdateQuery set(String column, String value) {
      this.values.put(column, value);
      return this;
   }

   public UpdateQuery set(String column) {
      this.set(column, "?");
      return this;
   }

   public UpdateQuery where(String expression) {
      this.wheres.add(expression);
      return this;
   }

   public UpdateQuery and(String expression) {
      this.where(expression);
      return this;
   }

   public String build() {

      StringBuilder builder = new StringBuilder();
      builder.append("UPDATE ").append(this.table).append(" SET ");
      String seperator = "";

      for (Entry<String, String> stringStringEntry : this.values.entrySet()) {
         String column = stringStringEntry.getKey();
         String value = stringStringEntry.getValue();
         builder.append(seperator).append(column).append("=").append(value);
         seperator = ",";
      }

      if (this.wheres.size() > 0) {
         builder.append(" WHERE ").append(QueryUtils.separate(this.wheres, " AND "));
      }

      return builder.toString();
   }
}
    