package me.darkboy.mysql.utils;

import java.util.Collection;

public class QueryUtils {

   public static String separate(Collection<String> collection, String separator) {

      StringBuilder builder = new StringBuilder();
      String sep = "";

      for (String item : collection) {
         builder.append(sep).append(item);
         sep = separator;
      }

      return builder.toString();
   }
}