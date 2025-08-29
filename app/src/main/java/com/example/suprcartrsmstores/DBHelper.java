package com.example.suprcartrsmstores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "rasamstore.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTableQuery = "CREATE TABLE users (userid INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, email TEXT, phoneno TEXT)";
        String createRegisterationTableQuery = "CREATE TABLE admins (adminid INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, email TEXT, phoneno TEXT)";
        String createProductTableQuery = "CREATE TABLE products (productid INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, brand TEXT, price REAL, quantity INTEGER, mfd TEXT, expiry TEXT)";
        String createStockTableQuery = "CREATE TABLE stock (stockid INTEGER PRIMARY KEY AUTOINCREMENT, product_name INTEGER, productid INTEGER, quantity INTEGER, brand TEXT)";
        String createPurchaseTableQuery = "CREATE TABLE purchase (purchaseid INTEGER PRIMARY KEY AUTOINCREMENT, bill_id TEXT, product_name TEXT, price REAL, quantity INTEGER, subtotal REAL)";
        String createBillTableQuery = "CREATE TABLE bills (billingid INTEGER PRIMARY KEY AUTOINCREMENT, bill_id TEXT, date TEXT, total REAL, username TEXT)";

        db.execSQL(createUserTableQuery);
        db.execSQL(createProductTableQuery);
        db.execSQL(createStockTableQuery);
        db.execSQL(createPurchaseTableQuery);
        db.execSQL(createBillTableQuery);
        db.execSQL(createRegisterationTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS admins");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS stock");
        db.execSQL("DROP TABLE IF EXISTS purchase");
        db.execSQL("DROP TABLE IF EXISTS bills");
        onCreate(db);
    }

    public boolean isValidUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;

    }

    public boolean insertUser(String username, String password, String email, String phoneno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        values.put("phoneno", phoneno);
        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    public boolean insertAdmin(String username1, String password1, String email1, String phoneno1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username1);
        values.put("password", password1);
        values.put("email", email1);
        values.put("phoneno", phoneno1);
        long result = db.insert("admins", null, values);
        db.close();
        return result != -1;
    }

    public boolean isAdmin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM admins WHERE username=? AND password=?", new String[]{username, password});
        boolean isAdmin = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isAdmin;
    }

    public boolean isProductExists(String proid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE productid=?", new String[]{proid});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public int getStockQuantity(String proid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT quantity FROM stock WHERE product_name=?", new String[]{proid});
        int quantity = 0;
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
        }
        cursor.close();
        return quantity;
    }

    public void updateStockQuantity(String proid, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", newQuantity);
        db.update("stock", values, "product_name=?", new String[]{proid});
        db.close();
    }

    public void addProduct(String proid, String proname, String probrand, double proprice, int proquan, String promfd, String proexpry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("productid", proid);
        values.put("name", proname);
        values.put("brand", probrand);
        values.put("price", proprice);
        values.put("quantity", proquan);
        values.put("mfd", promfd);
        values.put("expiry", proexpry);
        db.insert("products", null, values);

        // Insert into stock table
        ContentValues stockValues = new ContentValues();
        stockValues.put("productid", proname);
        stockValues.put("product_name", proid);
        stockValues.put("quantity", proquan);
        stockValues.put("brand", probrand);
        db.insert("stock", null, stockValues);

        db.close();
    }

    public ArrayList<String> getAllStockData() {
        ArrayList<String> stockList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM stock", null);
        while (cursor.moveToNext()) {
            String productid = cursor.getString(cursor.getColumnIndex("productid"));
            String productName = cursor.getString(cursor.getColumnIndex("product_name"));
            int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
            String brand = cursor.getString(cursor.getColumnIndex("brand"));

            String stockData = "Product NAME: " + productid +","+ "\n"+" Quantity: " + quantity +","+ "\n"+ " Brand: " + brand +".";
            stockList.add(stockData);
        }
        cursor.close();
        db.close();
        return stockList;
    }

    public ArrayList<String> getAllProductsData() {
        ArrayList<String> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM products", null);
        while (cursor.moveToNext()) {
            int productId = cursor.getInt(cursor.getColumnIndex("productid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String brand = cursor.getString(cursor.getColumnIndex("brand"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
            String mfd = cursor.getString(cursor.getColumnIndex("mfd"));
            String expiry = cursor.getString(cursor.getColumnIndex("expiry"));

            String productData = "Product ID: " + productId +","+
                    "\n"+" Name: " + name +","+
                    "\n"+" Brand: " + brand +","+
                    "\n"+" Price: " + price +","+
                    "\n"+" MFD: " + mfd +","+
                    "\n"+" Expiry: " + expiry+".";
            productList.add(productData);
        }
        cursor.close();
        db.close();
        return productList;
    }
    public ArrayList<String> getAllBillData() {
        ArrayList<String> billDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM bills", null);

        if (cursor.moveToFirst()) {
            do {
                String billId = cursor.getString(cursor.getColumnIndex("bill_id"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                Double total = cursor.getDouble(cursor.getColumnIndex("total"));
                String totall=String.valueOf(total);
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String billData = "Bill ID: " + billId + "\n"+" Date: " + date +"\n"+ " Total: " + totall +"\n"+ " Username: " + username;
                billDataList.add(billData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return billDataList;
    }
    public void reduceStockQuantity(String productName, int quantityToReduce) {
        SQLiteDatabase db = this.getWritableDatabase();
        int productId = getProductIdByName(productName);
        int currentQuantity = getStockQuantity(String.valueOf(productId));
        int newQuantity = currentQuantity - quantityToReduce;
        ContentValues values = new ContentValues();
        values.put("quantity", newQuantity);
        db.update("stock", values, "product_name=?", new String[]{String.valueOf(productId)});
        db.close();
    }
    public void addStockQuantity(String productName, int quantityToAdd) {
        SQLiteDatabase db = this.getWritableDatabase();
        int productId = getProductIdByName(productName);
        int currentQuantity = getStockQuantity(String.valueOf(productId));
        int newQuantity = currentQuantity + quantityToAdd;
        ContentValues values = new ContentValues();
        values.put("quantity", newQuantity);
        db.update("stock", values, "product_name=?", new String[]{String.valueOf(productId)});
        db.close();
    }
    private int getProductIdByName(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT productid FROM products WHERE name=?", new String[]{productName});
        int productId = -1;
        if (cursor.moveToFirst()) {
            productId = cursor.getInt(cursor.getColumnIndex("productid"));
        }
        cursor.close();
        db.close();
        return productId;
    }
    public boolean insertBill(String billId, String date, double totalAmount, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bill_id", billId);
        values.put("date", date);
        values.put("total", totalAmount);
        values.put("username", username);
        long result = db.insert("bills", null, values);
        db.close();
        return result != -1;
    }
    public boolean insertPurchase(String billId, String productName, double price, int quantity, double subtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bill_id", billId);
        values.put("product_name", productName);
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("subtotal", subtotal);
        long result = db.insert("purchase", null, values);
        db.close();
        return result != -1;
    }
    public void updateStock(String productName, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT quantity FROM stock WHERE product_name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{productName});
        int currentQuantity = 0;

        if (cursor.moveToFirst()) {
            currentQuantity = cursor.getInt(cursor.getColumnIndex("quantity"));
        }

        int newQuantity = currentQuantity + quantity;
        if (newQuantity < 0) {
            // Optionally, you can handle cases where the quantity becomes negative (e.g., throw an exception)
        } else {
            ContentValues values = new ContentValues();
            values.put("quantity", newQuantity);
            db.update("stock", values, "product_name = ?", new String[]{productName});
        }

        cursor.close();
    }
    public ArrayList<String>getUserTotalAmounts() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> usertotalList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT username,SUM(total) as total_amount FROM bills GROUP BY username", null);
        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                double totalll = cursor.getDouble(cursor.getColumnIndex("total_amount"));
                usertotalList.add(username + ":" + totalll+"Rs");
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return usertotalList;
    }

    public ArrayList<String> getAllbBillData(String loggedInUsername) {
        ArrayList<String> billDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = { loggedInUsername };
        try (Cursor cursor = db.rawQuery("SELECT * FROM bills WHERE username = ?", selectionArgs)) {
            while (cursor.moveToNext()) {
                String billId = cursor.getString(cursor.getColumnIndex("bill_id"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                double total = cursor.getDouble(cursor.getColumnIndex("total"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String billData = "Bill ID: " + billId + "\n" + " Date: " + date + "\n" + " Total: " + total + "\n" + " Username: " + username;
                billDataList.add(billData);
            }
        } catch (Exception e) {
            Log.e("ERROR", "Error retrieving bill data for username " + loggedInUsername, e);
        } finally {
            db.close();
        }
        return billDataList;
    }
}

