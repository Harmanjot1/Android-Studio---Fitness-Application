package com.example.app1.FoodDiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.app1.FoodDiary.Food;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DataBaseHelper.class.getName();

    // Column Names
    public static final String FOOD_NAME = "FOOD_NAME";
    public static final String FOOD_CALORIES = "FOOD_CALORIES";
    public static final String FOOD_PROTEIN = "FOOD_PROTEIN";
    public static final String FOOD_PICTURE = "FOOD_PICTURE";
    public static final String COLUMN_ID = "ID";
    public static final String DATE_ADDED = "DATE_ADDED";

    public static final String FAV_FOOD_NAME = "FOOD_NAME";
    public static final String FAV_FOOD_CALORIES = "FOOD_CALORIES";
    public static final String FAV_FOOD_PROTEIN = "FOOD_PROTEIN";
    public static final String FAV_FOOD_PICTURE = "FOOD_PICTURE";
    public static final String FAV_COLUMN_ID = "ID";
    public static final String FAV_DATE_ADDED = "DATE_ADDED";

    // Table Names
    public static final String FOOD_DIARY = "Food_Diary";
    public static final String FAV_FOOD_DIARY = "Fav_Food_Diary";


    //Calender import
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String todaysDate;

    // Database Version
    private static final int DATABASE_VERSON = 3;
    // Database Name
    private static final String DATABASE_NAME = "Food_Diary";

    // Create Table Statements
    private static final String CREATE_FOOD_TABLE = "CREATE TABLE " + FOOD_DIARY + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + FOOD_NAME + " TEXT, " + FOOD_CALORIES + " INT, " + FOOD_PROTEIN + " INT, " + FOOD_PICTURE + " INT, " + DATE_ADDED + " TEXT)";
    private static final String CREATE_FAV_FOOD_TABLE = "CREATE TABLE " + FAV_FOOD_DIARY + " (" + FAV_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + FAV_FOOD_NAME + " TEXT, " + FAV_FOOD_CALORIES + " INT, " + FAV_FOOD_PROTEIN + " INT, " + FAV_FOOD_PICTURE + " INT, " + FAV_DATE_ADDED + " TEXT)";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSON);
    }

    // This is called first time database is accessed. The code within this
    // method will create the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create required tables
        db.execSQL(CREATE_FAV_FOOD_TABLE);
        db.execSQL(CREATE_FOOD_TABLE);
    }

    // This method is for when in the future the database is changed/upgraded. This will contain the
    // version number and will prevent used from crashing when there has been a database update.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Update or Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + FOOD_DIARY);
        db.execSQL("DROP TABLE IF EXISTS " + FAV_FOOD_DIARY);
        // Create tables again
        onCreate(db);
    }

    void addFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        getDate();

        cv.put(FOOD_NAME, food.getName());
        cv.put(FOOD_CALORIES, food.getCalories());
        cv.put(FOOD_PROTEIN, food.getProtein());
        cv.put(FOOD_PICTURE, food.getPicture());
        cv.put(DATE_ADDED, todaysDate);

        long insert = db.insert(FOOD_DIARY, null, cv);
        db.close();
    }

    void fav_addFood(Food food){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues fav_cv = new ContentValues();
        getDate();

        fav_cv.put(FAV_FOOD_NAME, food.getName());
        fav_cv.put(FAV_FOOD_CALORIES, food.getCalories());
        fav_cv.put(FAV_FOOD_PROTEIN, food.getProtein());
        fav_cv.put(FAV_FOOD_PICTURE, food.getPicture());
        fav_cv.put(FAV_DATE_ADDED, todaysDate);

        long insert = db.insert(FAV_FOOD_DIARY, null, fav_cv);
        db.close();
    }
    public List<Food> getSavedFavFood(){
        List<Food> returnList = new ArrayList<>();

        //String queryString = "SELECT * FROM " + FAV_FOOD_DIARY;
        //Log.e(LOG,queryString);
        SQLiteDatabase db = this.getReadableDatabase();

        //Log.e(LOG,queryString);
        Cursor cursor = db.rawQuery("select * from " + FAV_FOOD_DIARY, null);
        getDate();

        if (cursor.moveToFirst()) {
            do {
                int FoodID = cursor.getInt(0);
                String FoodName = cursor.getString(1);
                int FoodCalories = cursor.getInt(2);
                int FoodProtein = cursor.getInt(3);
                int FoodPicture = cursor.getInt(4);
                String FoodDate = cursor.getString(5);

                Food newfood = new Food(FoodID, FoodName, FoodCalories, FoodProtein, FoodPicture, FoodDate);
                returnList.add(newfood);
            } while (cursor.moveToNext()); // keep repeating until list is complete
        } else {
            // Else do nothing
        }
        // Close both cursor and database
        cursor.close();
        db.close();
        return returnList;
    }

    public List<Food> getSavedFood() {
        List<Food> returnList = new ArrayList<>();

        String fav_queryString = "SELECT * FROM " + FOOD_DIARY;
        SQLiteDatabase db = this.getReadableDatabase();

        Log.e(LOG,fav_queryString);

        Cursor cursor = db.rawQuery(fav_queryString, null);
        getDate();

        if (cursor.moveToFirst()) {
            do {
                int FoodID = cursor.getInt(0);
                String FoodName = cursor.getString(1);
                int FoodCalories = cursor.getInt(2);
                int FoodProtein = cursor.getInt(3);
                int FoodPicture = cursor.getInt(4);
                String FoodDate = cursor.getString(5);

                Food newfood = new Food(FoodID, FoodName, FoodCalories, FoodProtein, FoodPicture, FoodDate);
                returnList.add(newfood);
            } while (cursor.moveToNext()); // keep repeating until list is complete
        } else {
            // Else do nothing
        }
        // Close both cursor and database
        cursor.close();
        db.close();
        return returnList;

    }

    public boolean deleteFood(Food food) {
        // if food is inside database then it will be deleted and return true hence its found
        // if food not in database then return false
        SQLiteDatabase db = this.getWritableDatabase();
        // The reason we are calling WritableDatabase is because we are altering it
        String queryString = "DELETE FROM " + FOOD_DIARY + " WHERE " + COLUMN_ID + " = " + food.getID();

        Cursor cursor = db.rawQuery(queryString, null);
        return cursor.moveToFirst();
    }
    public boolean fav_deleteFood(Food food) {
        // if food is inside database then it will be deleted and return true hence its found
        // if food not in database then return false
        SQLiteDatabase db = this.getWritableDatabase();
        // The reason we are calling WritableDatabase is because we are altering it
        String queryString = "DELETE FROM " + FAV_FOOD_DIARY + " WHERE " + COLUMN_ID + " = " + food.getID();

        Cursor cursor = db.rawQuery(queryString, null);
        return cursor.moveToFirst();
    }

    public void getDate() {
        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todaysDate = dateFormat.format(calendar.getTime());
    }

    public void deleteAllFood() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + FOOD_DIARY);
    }
}
