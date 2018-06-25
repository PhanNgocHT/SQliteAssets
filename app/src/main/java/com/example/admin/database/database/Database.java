package com.example.admin.database.database;

import java.util.ArrayList;

/**
 * Created by admin on 7/2/2017.
 */
// Generic programing
public interface Database<T> {
    boolean save(T obj);

    boolean upDate(T obj);

    boolean delete(T obj);

    ArrayList<T> getAllData();
}
