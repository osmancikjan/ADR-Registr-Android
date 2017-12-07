package com.janosmancik.adrregistr;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper{

    //The Android's default system path of your application database.

    private static final String DB_PATH = "/data/data/com.janosmancik.adrregistr/databases/";
    private static final String DB_NAME = "rdb";
    private static final String DATABASE_TABLE = "register";
//    public static final String KEY_ROWID = "_id";
    public static final String KEY_UN = "UN";
    public static final String KEY_KEMLER = "KEMLER";
    public static final String KEY_NAME = "LATKA";
/*    public static final String KEY_NAMEB = "LATKABEZD";
   public static final String KEY_TRIDA = "TRIDA";
   public static final String KEY_OHROZENI = "OHROZENI";
    public static final String KEY_OCHRANA = "OCHRANA";
    public static final String KEY_POZAR = "POZAR";
    public static final String KEY_ZNECISTENI = "ZNECISTENI";
    public static final String KEY_POMOC = "POMOC";
    public static final String KEY_KOD = "KOD";
*/
    public static final String[] BASIC_KEYS = {KEY_UN,KEY_KEMLER,KEY_NAME};
 /*   public static final String[] ALL_KEYS = {KEY_ROWID,KEY_UN,KEY_KEMLER,KEY_NAME,KEY_NAMEB,KEY_TRIDA,KEY_OHROZENI,KEY_OCHRANA,KEY_POZAR,KEY_ZNECISTENI,KEY_POMOC,KEY_KOD};

    public static final int COL_ROWID = 0;
    private static final int COL_UN = 1;
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
*/
    private SQLiteDatabase myDataBase;

    private final Context myContext;
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DBHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        Log.e("Path 1", DB_PATH);
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void create() throws IOException{

        boolean dbExist = check();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copy();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean check(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copy() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void open() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    public Cursor getAllBasicRows() {
        String where = null;
        Cursor c = myDataBase.query(DATABASE_TABLE, BASIC_KEYS, where, null, null, null, null);
        if(c != null) {
            c.moveToFirst();
        }
        return c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}

