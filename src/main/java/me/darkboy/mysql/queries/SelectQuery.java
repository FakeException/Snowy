package me.darkboy.mysql.queries;

import me.darkboy.mysql.utils.QueryUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectQuery {

   private final String table;
   private final List<String> columns = new ArrayList<>();
   private final List<String> wheres = new ArrayList<>();
   private String orderBy;
   private boolean orderByAscending = false;
   private int limitOffset = 0;
   private int limitRowCount = 0;

   public SelectQuery(String table) {
      this.table = table;
   }

   public SelectQuery column(String column) {
      this.columns.add(column);
      return this;
   }

   public SelectQuery where(String expression) {
      this.wheres.add(expression);
      return this;
   }

   public SelectQuery and(String expression) {
      this.where(expression);
      return this;
   }

   public SelectQuery orderBy(String column, boolean ascending) {
      this.orderBy = column;
      this.orderByAscending = ascending;
      return this;
   }

   public SelectQuery limit(int offset, int rowCount) {
      this.limitOffset = offset;
      this.limitRowCount = rowCount;
      return this;
   }

   public SelectQuery limit(int rowCount) {
      this.limitOffset = 0;
      this.limitRowCount = rowCount;
      return this;
   }

   public String build() {
      StringBuilder builder = new StringBuilder();
      builder.append("SELECT ").append(QueryUtils.separate(this.columns, ",")).append(" FROM ").append(this.table);
      if (this.wheres.size() > 0) {
         builder.append(" WHERE ").append(QueryUtils.separate(this.wheres, " AND "));
      }

      if (this.orderBy != null) {
         builder.append(" ORDER BY ").append(this.orderBy).append(this.orderByAscending ? " ASC" : " DESC");
      }

      if (this.limitRowCount > 0) {
         builder.append(" LIMIT ").append(this.limitOffset).append(",").append(this.limitRowCount);
      }

      return builder.toString();
   }
}