package com.example.libraryproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE USERS(ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT UNIQUE, " +
                "GENDER TEXT, EMAIL TEXT, PASSWORD TEXT, AGE INTEGER, RESERVATIONS INT, BLOCKED BOOLEAN)");

        sqLiteDatabase.execSQL("CREATE TABLE BOOKS(ISBN TEXT PRIMARY KEY UNIQUE, TITLE TEXT, " +
                "AUTHOR TEXT, PUBLISHER TEXT, PAGES INTEGER)");

        sqLiteDatabase.execSQL("CREATE TABLE RESERVATIONS(ISBN TEXT, USERNAME TEXT, RESERVATIONDATE TEXT, " +
                "FOREIGN KEY(ISBN) REFERENCES BOOKS(ISBN), FOREIGN KEY(USERNAME) REFERENCES USERS(USERNAME))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertUser(User user) {
    SQLiteDatabase sqLiteDatabase = getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("USERNAME", user.getName());
    contentValues.put("GENDER", user.getGender());
    contentValues.put("EMAIL", user.getEmail());
    contentValues.put("PASSWORD", user.getPassword());
    contentValues.put("AGE", user.getAge());
    contentValues.put("RESERVATIONS", user.getNumberOfReservedBooks());
    contentValues.put("BLOCKED", user.isBlocked());
    sqLiteDatabase.insert("USERS", null, contentValues);
    }

    public void updateUserData(int id, int age, String gender){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues updated = new ContentValues();
        updated.put("AGE", age);
        updated.put("GENDER", gender);
        sqLiteDatabase.update("USERS", updated, "ID = '" + id + "'", null);
    }

    public Cursor AuthenticateUser(String username, String password) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM USERS WHERE USERS.USERNAME='" + username + "' AND USERS.PASSWORD='" + password + "'", null);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM USERS", null);
    }

    public Cursor getUser(String username) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM USERS WHERE USERS.USERNAME='" + username + "'", null);
    }

    public int getNumberOfReservations(String username){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM USERS WHERE USERNAME='" + username +"'", null);
        if(cursor.getCount() == 0)
            return -7;
        else {
            cursor.moveToFirst();
            int ans = cursor.getInt(6);
            return ans;
        }
    }

    public void unblockUser(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("BLOCKED", false);
        sqLiteDatabase.update("USERS", contentValues, "ID=" + id, null);
    }

    public void blockUser(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("BLOCKED", true);
        sqLiteDatabase.update("USERS", contentValues, "ID=" + id, null);
    }

    public boolean addReservationToUser(String username){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        //String[] bindingArgs = new String[]{ "1", username };
        //sqLiteDatabase.execSQL("UPDATE USERS SET RESERVATIONS = RESERVATIONS + ? WHERE USERNAME = ?", bindingArgs);
        ContentValues contentValues = new ContentValues();
        int newReservationCount = getNumberOfReservations(username) + 1;
        System.out.println(newReservationCount + "****************");
        contentValues.put("RESERVATIONS", newReservationCount);
        sqLiteDatabase.update("USERS", contentValues, "USERNAME='" + username + "'",null);
        sqLiteDatabase.close();
        return true;
    }

    public boolean removeReservationFromUser(String username){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        //String[] bindingArgs = new String[]{ "1", username };
        //sqLiteDatabase.execSQL("UPDATE USERS SET RESERVATIONS = RESERVATIONS + ? WHERE USERNAME = ?", bindingArgs);
        ContentValues contentValues = new ContentValues();
        int newReservationCount = getNumberOfReservations(username) - 1;
        contentValues.put("RESERVATIONS", newReservationCount);
        sqLiteDatabase.update("USERS", contentValues, "USERNAME='" + username + "'",null);
        sqLiteDatabase.close();
        return true;
    }

    public void insertBook(Book book) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ISBN", book.getIsbn());
        contentValues.put("TITLE", book.getTitle());
        contentValues.put("AUTHOR", book.getAuthor());
        contentValues.put("PUBLISHER", book.getPublisher());
        contentValues.put("PAGES", book.getPages());
        sqLiteDatabase.insert("BOOKS", null, contentValues);
    }

    public Cursor getBook(String isbn) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM BOOKS WHERE ISBN='" + isbn + "'", null);
    }

    public Cursor getAllBooks() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM BOOKS", null);
    }

    public void deleteAllBooks(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.delete("BOOKS",null,null);
    }

    public void newReservation(BookReservations reservation){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ISBN", reservation.getIsbn());
        contentValues.put("USERNAME", reservation.getUsername());
        contentValues.put("RESERVATIONDATE", reservation.getReservationDate());
        sqLiteDatabase.insert("RESERVATIONS", null, contentValues);
    }

    public Cursor getAllReservations() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM RESERVATIONS", null);
    }

    public Cursor getUserReservations(String username) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM RESERVATIONS WHERE USERNAME = '" + username + "'", null);
    }

    public void deleteAllResrevations(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.delete("RESERVATIONS",null,null);
    }

    public void deleteResrevation(String isbn, String username){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.delete("RESERVATIONS", "ISBN='" + isbn + "' AND USERNAME='" + username + "'",null);
    }

    public Cursor getBookReservedByUser(String isbn, String username){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM RESERVATIONS WHERE USERNAME='" + username + "' AND ISBN='"+isbn+"'", null);
    }

    public Cursor getBooksByPublisher(String publisher){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM BOOKS WHERE PUBLISHER = '" + publisher + "'", null);
    }

    public Cursor getBooksByPages(int min, int max){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM BOOKS WHERE PAGES >= '" + min + "' AND PAGES <= '" + max + "'", null);
    }

    public Cursor getBooksByPagesAndPublisher(int min, int max, String publisher){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM BOOKS WHERE PUBLISHER = '" + publisher +
                "' AND PAGES >= " + min + " AND PAGES <= " + max, null);
    }

}
