package com.boom.vyveska.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


//класс для работы сбд
public class DBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "ecommerce"; //название файла бд
    public final static int DB_VERSION = 1; //версия бд
    public static SQLiteDatabase db; //подключение к библиотеке бд
    private final Context context; //контекст
    private String DB_PATH; //

    private final String TABLE_CART = "tbl_cart";// название таблицы в бд
    private final String CART_ID = "id"; //
    private final String PRODUCT_NAME = "product_name";
    private final String QUANTITY = "quantity";
    private final String TOTAL_PRICE = "total_price";


        //метод получения адреса  хранения бд
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }
        //метод создания бд
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        SQLiteDatabase db_Read = null;
        if (dbExist) {
        } else {
            db_Read = this.getReadableDatabase();
            db_Read.close();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    //Проверяет, существует ли уже эта база, чтобы не копировать каждый раз при запуске приложения
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();

    }
        //метод копирование бд
    private void copyDataBase() throws IOException {
        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }
        //открыти бд
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
        //закрыть бд
    @Override
    public void close() {
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
        //обновить бд
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
        //получение всех данных из таблицы корзина
    public ArrayList<ArrayList<Object>> getAllData() {
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_CART, new String[]{CART_ID, PRODUCT_NAME, QUANTITY},
                    null, null, null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cursor.getLong(0));
                    dataList.add(cursor.getString(1));
                    dataList.add(cursor.getString(2));

                    dataArrays.add(dataList);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
        return dataArrays;
    }


    //проверка наличия данных в таблице корзина по id
    public boolean isDataExist(long id) {
        boolean exist = false;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_CART, new String[]{CART_ID}, CART_ID + "=" + id, null, null, null, null);
            if (cursor.getCount() > 0) {
                exist = true;
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
        return exist;
    }




        //добавить в таблицу корзина
    public void addData(String product_name, int quantity) {
        ContentValues values = new ContentValues();

        values.put(PRODUCT_NAME, product_name);
        values.put(QUANTITY, quantity);

        try {
            db.insert(TABLE_CART, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

        //удалить из таблицы корзина по id товара
    public void deleteData(long id) {
        try {
            db.delete(TABLE_CART, CART_ID + "=" + id, null);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    //удалить все из таблицы корзина
    public void deleteAllData() {
        try {
            db.delete(TABLE_CART, null, null);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

        //обновить данные продукта в корзине по id продукта
    public void updateData(long id, int quantity, double total_price) {
        ContentValues values = new ContentValues();
        values.put(QUANTITY, quantity);
        values.put(TOTAL_PRICE, total_price);
        try {
            db.update(TABLE_CART, values, CART_ID + "=" + id, null);
        } catch (Exception e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

}