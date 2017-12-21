package com.janosmancik.adrregistr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.xml.sax.ErrorHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.

    private static String DB_PATH = "";
    private static final String DB_NAME = "r.db";
    private static final String DB_TABLE = "register";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_UN = "UN";
    public static final String KEY_KEMLER = "KEMLER";
    public static final String KEY_NAME = "LATKA";
    public static final String KEY_NAMEB = "LATKABEZD";
    public static final String KEY_TRIDA = "TRIDA";
    public static final String KEY_OHROZENI = "OHROZENI";
    public static final String KEY_OCHRANA = "OCHRANA";
    public static final String KEY_POZAR = "POZAR";
    public static final String KEY_ZNECISTENI = "ZNECISTENI";
    public static final String KEY_POMOC = "POMOC";
    public static final String KEY_KOD = "KOD";

    public static final String[] BASIC_KEYS = {KEY_UN, KEY_KEMLER, KEY_NAME};
    public static final String[] ALL_KEYS = {KEY_ROWID, KEY_UN, KEY_KEMLER, KEY_NAME, KEY_NAMEB, KEY_TRIDA, KEY_OHROZENI, KEY_OCHRANA, KEY_POZAR, KEY_ZNECISTENI, KEY_POMOC, KEY_KOD};

    public static final int COL_ROWID = 0;
    public static final int COL_UN = 1;
    public static final int COL_KEMLER = 2;
    public static final int COL_NAME = 3;
    public static final int COL_NAMEB = 4;
    public static final int COL_TRIDA = 5;
    public static final int COL_OHROZENI = 6;
    public static final int COL_OCHRANA = 7;
    public static final int COL_POZAR = 8;
    public static final int COL_ZNECISTENI = 9;
    public static final int COL_POMOC = 10;
    public static final int COL_KOD = 11;

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    //The Android's default system path of your application database.

    public static ArrayList<SubstanceObjectModel> arrayList = new ArrayList<>();
    public static ArrayList<SubstanceObjectModel> arrayListKemler = new ArrayList<>();

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = myContext.getDatabasePath(DB_NAME).toString();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table register " + "(id integer primary key, name text)");
        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getWritableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS register");
        onCreate(db);
    }


    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    public void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        //  this.getReadableDatabase();

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    /*
        public boolean insertContact(String name)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            db.insert("contacts", null, contentValues);
            return true;
        }
    */


    // TODO


    //Cursor representuje vracena data
    public SubstanceObjectModel getData(String un) {
        SubstanceObjectModel item = new SubstanceObjectModel();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db.isOpen()) {
            try {
                cursor = db.rawQuery("SELECT * FROM register WHERE UN LIKE " + un + "", null);
                cursor.moveToNext();
                item.setKemler(cursor.getString(COL_KEMLER));
                item.setLatka(cursor.getString(COL_NAME));
                item.setUn(cursor.getString(COL_UN));
                return item;
            }
            finally {

                db.close();
            }
        }else {
            Log.i("DBInfo","Databaze neni otevřená!");
            return null;
        }
        //Cursor res =  db.rawQuery( "select * from contacts LIMIT 1 OFFSET "+id+"", null );

    }

    /*

    public String getEmployeeName(String empNo) {
    Cursor cursor = null;
    String empName = "";
    try {
        cursor = SQLiteDatabaseInstance_.rawQuery("SELECT EmployeeName FROM Employee WHERE EmpNo=?", new String[] {empNo + ""});
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            empName = cursor.getString(cursor.getColumnIndex("EmployeeName"));
        }
        return empName;
    }finally {
        cursor.close();
    }
}




        public boolean updateContact (Integer id, String name)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("update name in register set name='" + name + "' where id="+id+"", null);
            return true;
        }
    */
    public void setAllSubstances() {
        arrayList.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        //String query = ("SELECT * FROM register").trim();
        if(db.isOpen()){
            Cursor res =  db.rawQuery( "SELECT * FROM "+ DB_TABLE+"", null);
            res.moveToFirst();

            try {
                while (!res.isAfterLast()) {
                    SubstanceObjectModel item = new SubstanceObjectModel();

                    item.setUn(res.getString(COL_UN));
                    item.setKemler(res.getString(COL_KEMLER));
                    item.setLatka(res.getString(COL_NAME));

                    arrayList.add(item);
                    res.moveToNext();
                }
            }catch (Exception e){
                e.printStackTrace();
                res.close();
            }finally {
                db.close();
            }
        }else{
               Log.i("DBInfo","Databaze neni otevřená!");
            }
    }

    public ArrayList<SubstanceObjectModel> getAllSubstancesNames() {
        return arrayList;
    }

    public ArrayList<SubstanceObjectModel> getAllSubstancesByKemler(String input) {
        arrayListKemler.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        //String query = ("SELECT * FROM register").trim();
        if(db.isOpen()){
            Cursor res =  db.rawQuery( "SELECT * FROM "+ DB_TABLE+" WHERE "+ KEY_KEMLER + " LIKE '"+input+"%'", null);
            res.moveToFirst();

            try {
                while (!res.isAfterLast()) {
                    SubstanceObjectModel item = new SubstanceObjectModel();

                    item.setUn(res.getString(COL_UN));
                    item.setKemler(res.getString(COL_KEMLER));
                    item.setLatka(res.getString(COL_NAME));

                    arrayListKemler.add(item);
                    res.moveToNext();
                }
            }catch (Exception e){
                e.printStackTrace();
                res.close();
            }finally {
                db.close();
            }
        }else{
            Log.i("DBInfo","Databaze neni otevřená!");
        }
        return arrayListKemler;
    }

    public ArrayList<SubstanceObjectModel> getAllSubstancesByUN(String input) {
        arrayListKemler.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        //String query = ("SELECT * FROM register").trim();
        if(db.isOpen()){
            Cursor res =  db.rawQuery( "SELECT * FROM "+ DB_TABLE+" WHERE "+ KEY_UN + " LIKE '"+input+"%'", null);
            res.moveToFirst();

            try {
                while (!res.isAfterLast()) {
                    SubstanceObjectModel item = new SubstanceObjectModel();

                    item.setUn(res.getString(COL_UN));
                    item.setKemler(res.getString(COL_KEMLER));
                    item.setLatka(res.getString(COL_NAME));

                    arrayListKemler.add(item);
                    res.moveToNext();
                }
            }catch (Exception e){
                e.printStackTrace();
                res.close();
            }finally {
                db.close();
            }
        }else{
            Log.i("DBInfo","Databaze neni otevřená!");
        }
        return arrayListKemler;
    }

    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, "1", null);
    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

}

