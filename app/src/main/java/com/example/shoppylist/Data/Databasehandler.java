package com.example.shoppylist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.shoppylist.Model.Grocery;
import com.example.shoppylist.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Databasehandler extends SQLiteOpenHelper {

    private Context ctx;



    public Databasehandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_version);
        this.ctx = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROCERY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_GROCERY_ITEM + " TEXT," + Constants.KEY_QUANTITY_NUMBER + " TEXT,"
                + Constants.KEY_DATE_NAME + " LONG);";

        db.execSQL(CREATE_GROCERY_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);


    }

    //CRUD OPERATIONS - create read update and delete

    //Add grocery
    public void addgrocery (Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues(); //map type datastructure
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Constants.KEY_QUANTITY_NUMBER, grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME , java.lang.System.currentTimeMillis());

        //inserting values in table
        db.insert(Constants.TABLE_NAME, null, values);

        Log.d("Saved!!", "Saved to Database" );


    }

    //get a Grocery item
    public Grocery getGrocery (int id){
        SQLiteDatabase db = this.getWritableDatabase();

        //allows us to curse through our database
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID,
                Constants.KEY_GROCERY_ITEM, Constants.KEY_QUANTITY_NUMBER, Constants.KEY_DATE_NAME}, //all this equivalent to id
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null , null, null, null);

       if (cursor != null)
           cursor.moveToFirst();

           Grocery grocery = new Grocery();
           grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
           grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
           grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QUANTITY_NUMBER)));

           //converting time to readable time

           java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
           String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

           grocery.setDateitemAdded(formatedDate);

       return grocery;
    }

    //Get all groceries
    public List<Grocery> getALLGroceries (){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Grocery> groceryList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME , new String[]
                {Constants.KEY_ID , Constants.KEY_GROCERY_ITEM, Constants.KEY_QUANTITY_NUMBER, Constants.KEY_DATE_NAME} ,
                null, null , null, null , Constants.KEY_DATE_NAME + " DESC");

        if(cursor.moveToFirst()){
            do{

                Grocery grocery = new Grocery();

                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QUANTITY_NUMBER)));

                //converting time to readable time
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

                grocery.setDateitemAdded(formatedDate);

                //adding to list
                groceryList.add(grocery);


            }while (cursor.moveToNext());
        }

        return groceryList;
    }


    //Updated Grocery
    public int updategrocery (Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues(); //hashmap type datastructure
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Constants.KEY_QUANTITY_NUMBER , grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME , java.lang.System.currentTimeMillis()); //system time

        //update row
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[] {String.valueOf(grocery.getId())});


    }

    //Delete grocery
    public void deletegrocery (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?",
                new String[] {String.valueOf(id)});

        db.close();


    }

    //get count
    public int getgroceriescount (){
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }
}
