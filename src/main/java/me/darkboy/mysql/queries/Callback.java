package me.darkboy.mysql.queries;

public interface Callback<V, T extends Throwable> {
   void call(V result, T thrown);
}