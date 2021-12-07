package me.darkboy.mysql.queries;

import me.darkboy.mysql.utils.QueryUtils;

import java.util.ArrayList;
import java.util.List;

public class DeleteQuery {

   private final String table;
   private final List<String> wheres = new ArrayList<>();

   public DeleteQuery(String table) {
      this.table = table;
   }

   public DeleteQuery where(String expression) {
      this.wheres.add(expression);
      return this;
   }

   public DeleteQuery and(String expression) {
      this.where(expression);
      return this;
   }

   public String build() {
      StringBuilder builder = new StringBuilder();
      builder.append("DELETE FROM ").append(this.table);
      if (this.wheres.size() > 0) {
         builder.append(" WHERE ").append(QueryUtils.separate(this.wheres, " AND "));
      }

      return builder.toString();
   }
}