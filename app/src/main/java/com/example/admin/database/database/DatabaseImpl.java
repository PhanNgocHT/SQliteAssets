package com.example.admin.database.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by admin on 7/2/2017.
 */

public class DatabaseImpl implements Database<Story>{
    private static final String DATABASE_NAME="story.db";
    private static final int DATABASE_VERSION=1;

    private static final String TABLE_NAME_STORY="story";
    private static final String COLUMN_STORY_ID="id";
    private static final String COLUMN_STORY_TITLE="title";
    private static final String COLUMN_STORY_CONTENT="content";
    //Doi tuong giup thuc thi cac cau lenh
    private SQLiteDatabase sqLiteDatabase;
     //Giup tao bang, tao database
    private DatabaseHelper databaseHelper;

    public DatabaseImpl(Context context) {
           databaseHelper=new DatabaseHelper(context);

    }

    private void initializeDatabase(Context context) {
        //Source: assets folder
        //destination:/data/data/packageName
        try {


            //Open output stream

            String path = Environment.getDataDirectory()
                    .getAbsolutePath()+"/data/com.example.admin.database/ql_sinhvien.sqlite";
            File file=new File(path);
            if (file.exists()) {
                return;
            }

            FileOutputStream output=new FileOutputStream(file);

            AssetManager assets = context.getAssets();
            InputStream input=assets.open("ql_sinhvien.sqlite");

            //Copy database file
            //TODO
            //Close
            input.close();
            output.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openDatabase() {
        if (sqLiteDatabase==null||!sqLiteDatabase.isOpen()) {
            //Cach 1
            sqLiteDatabase = databaseHelper.getWritableDatabase();
            String path = Environment.getDataDirectory()
                    .getAbsolutePath()+"/data/com.example.admin.database/ql_sinhvien.sqlite";
            //Cach 2
            sqLiteDatabase=SQLiteDatabase.openDatabase(
                    path,
                    null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
    }

    private void closeDatabase() {
         if (sqLiteDatabase!=null&&sqLiteDatabase.isOpen())  {
             sqLiteDatabase.close();
         }
    }

    public void saveStory(Story story) {

    }

    @Override
    public boolean save(Story story) {
        //UUID giup sinh ra chuoi ngau nhien
        openDatabase();

        ContentValues values=new ContentValues();
        values.put(COLUMN_STORY_ID, story.getId());
        values.put(COLUMN_STORY_TITLE, story.getTitle());
        values.put(COLUMN_STORY_CONTENT, story.getContent());

        long id=sqLiteDatabase.insert(TABLE_NAME_STORY, null, values);


        closeDatabase();
        return id!=-1;

    }

    @Override
    public boolean upDate(Story story) {
        openDatabase();

         ContentValues values=new ContentValues();
         values.put(COLUMN_STORY_TITLE, story.getTitle());
         values.put(COLUMN_STORY_CONTENT, story.getContent());
         long id=sqLiteDatabase.update(TABLE_NAME_STORY,
                 values,
                 "id=?",
                 new String[]{story.getId()});

         closeDatabase();
         return id!=-1;
    }

    @Override
    public boolean delete(Story story) {
        openDatabase();

       long id= sqLiteDatabase.delete(TABLE_NAME_STORY,
                "id=?",
                new String[]{story.getId()});

        closeDatabase();
        return id!=-1;
    }

    @Override
    public ArrayList<Story> getAllData() {
        openDatabase();
        String sqlGetAllData="SELECT * FROM "+TABLE_NAME_STORY+" ORDER BY "+COLUMN_STORY_TITLE+" ASC";

        ArrayList<Story> stories=new ArrayList<>();
        Cursor cursor= sqLiteDatabase.rawQuery(sqlGetAllData, null);
        if (cursor==null||cursor.getCount()==0) {
            closeDatabase();
            return stories;
        }
        //dua con tro ve dong dau tien
        cursor.moveToFirst();
        int indexId=cursor.getColumnIndex(COLUMN_STORY_ID);
         int indexTitle=cursor.getColumnIndex(COLUMN_STORY_TITLE);
         int indexContent=cursor.getColumnIndex(COLUMN_STORY_CONTENT);
        while (!cursor.isAfterLast()) {
            String id=cursor.getString(indexId);
            String title=cursor.getString(indexTitle);
            String content=cursor.getString(indexContent);
            stories.add(new Story(id, title, content));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return stories;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        private String sqlCreationStoryTable="CREATE TABLE IF NOT EXISTS "+
                TABLE_NAME_STORY+"("+
                COLUMN_STORY_ID+" TEXT PRIMARY KEY, "+
                COLUMN_STORY_TITLE+" TEXT NOT NULL, "+
                COLUMN_STORY_CONTENT+" TEXT NOT NULL "+
                ")";
        private String sqlDropStoryTable= " DROP TABLE IF EXISTS " +TABLE_NAME_STORY;
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(sqlCreationStoryTable);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL(sqlDropStoryTable);
            onCreate(sqLiteDatabase);
        }
    }
}
