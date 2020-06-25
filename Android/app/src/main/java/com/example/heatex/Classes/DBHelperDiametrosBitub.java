package com.example.heatex.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelperDiametrosBitub extends SQLiteOpenHelper {

    private SQLiteOpenHelper openHelper;
    private static String DB_PATH="";
    private static String DB_NAME= "diametroshairpindb.db";
    private final String PASSWORD = "";
    private static final int DB_VERSION=2;
    private SQLiteDatabase mDatabase;
    private Context mContext=null;
    public String COLUNA1 = "id";
    String TABLENAME = "hairpinbitubular"; // por padrão
    Cursor c = null;
    private static DBHelperDiametrosBitub instance = null;

    public DBHelperDiametrosBitub(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        if(Build.VERSION.SDK_INT>=17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data" + context.getPackageName()+"/databases/";
        this.mContext = context;
        mDatabase = getWritableDatabase();

    }

    static public synchronized  DBHelperDiametrosBitub getInstance(Context context){
        if(instance==null)
            instance = new DBHelperDiametrosBitub(context);
        return instance;
    }

    public void createDatabase() throws IOException{
        //If the database doesn't exist, copy it from the assets folder
        boolean isExistingDB = checkExistingDB();
        if(!isExistingDB){
            this.getReadableDatabase();
            this.close();
            try{
                copyDatabase();
            }
            catch (Exception e){

            }
        }
    }

    private boolean checkExistingDB(){
        File dbFile = new File(new StringBuilder(DB_PATH).append(DB_NAME).toString());
        return dbFile.exists();
    }

    private void copyDatabase(){
        InputStream input = null;
        try{
            input = mContext.getAssets().open(DB_NAME);
            String outputFileName = new StringBuilder(DB_PATH).append(DB_NAME).toString();
            OutputStream outputStream = new FileOutputStream(outputFileName);
            byte[] mBuffer = new byte[1024];
            int lenght;
            while((lenght = input.read(mBuffer))>0){
                outputStream.write(mBuffer, 0, lenght);
            }
            outputStream.flush();
            outputStream.close();
            input.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean openDatabase() throws SQLiteException{
        String path = new StringBuilder(DB_PATH).append(DB_NAME).toString();
        //mDatabase = SQLiteDatabase.openDatabase(path, PASSWORD, null, SQLiteDatabase.CREATE_IF_NECESSARY);

        //mDatabase = SQLiteDatabase.openDatabase(path, PASSWORD, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        mDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ///data/data/<your.app.package>/databases/foo.db
        return mDatabase != null;
    }

    @Override
    public synchronized void close() {
        if(mDatabase != null)
            mDatabase.close();
        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        mDatabase.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        this.onCreate(sqLiteDatabase);
    }

    public void getDiametros(){
        String query = "SELECT * FROM " + TABLENAME;
        Cursor mCursor = mDatabase.rawQuery(query, new String[] {"IDmm", "ODmm"});
        mDatabase.query(TABLENAME, null, null, null, null, null, COLUNA1);
        System.out.println("OLHAQUE    " + mCursor.getColumnNames() );
    }

    public Cursor getDatabase() {
        return mDatabase.query(TABLENAME, null, null, null, null, null, COLUNA1);
    }


    public class DatabaseHelper extends SQLiteAssetHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
    }


}

