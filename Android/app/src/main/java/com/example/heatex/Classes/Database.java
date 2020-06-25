package com.example.heatex.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "diametroshairpindb.db";
    private static final String TABELAHAIRPINBITUBULAR = "hairpinbitubular";
    private static final String TABELAHAIRPINMULTITUBULAR = "hairpinmultitubular";
    public static final String DATABASE_ID = "_id";
    public static final String DATABASE_GROUP_1 = "_id";
    public static final String COLUNA1_DO_BITUB="Schedule Designations ANSI/ASME";
    public static final int TABELAHAIRPINBITUBULAR_ID =1, TABELAHAIRPINMULTITUBULAR_ID=2, TABELACASCOETUBOS_ID=3;
    private final Context mContext;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDB;

    public Database(Context context) {
        mContext = context;
    }

    public void open() {
        mDatabaseHelper = new DatabaseHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDatabaseHelper.getWritableDatabase();
    }


    public void close() {
        if (mDatabaseHelper != null) mDatabaseHelper.close();
    }

    public Cursor getDatabase(int qualTabela) {
        if(qualTabela == TABELAHAIRPINBITUBULAR_ID)
            return mDB.query(TABELAHAIRPINBITUBULAR, null, null, null, null, null, DATABASE_GROUP_1);


        else if(qualTabela == TABELAHAIRPINMULTITUBULAR_ID)
            return mDB.query(TABELAHAIRPINMULTITUBULAR, null, null, null, null, null, DATABASE_GROUP_1);

        else if(qualTabela == TABELACASCOETUBOS_ID)
            return mDB.query(TABELAHAIRPINBITUBULAR, null, null, null, null, null, DATABASE_GROUP_1);

        //Se nada der certo...
        else return null;
    }


    public Cursor getID(long rowID) {
        return mDB.query(TABELAHAIRPINBITUBULAR, null, "_id" + " = "
                + rowID, null, null, null, null);
    }

    public class DatabaseHelper extends SQLiteAssetHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
    }

    public List<TuboHairpin> getDiametrosBitub(@Nullable String padraoProjeto){
        int id_tabela = TABELAHAIRPINBITUBULAR_ID;
        List<TuboHairpin> listaTubos = new ArrayList<TuboHairpin>();

        //Variáveis que salvarão os valores para serem passados p/ os objetos
        String id, ScheduleDesignations, DNmm, NPSin, ODin, ODmm, IDin, IDmm, 重量lb, 重量kg;

        Database dbzinho = new Database(mContext);
        dbzinho.open();
        Cursor mCursor = dbzinho.getDatabase(id_tabela);
        //mCursor = mDB.rawQuery("SELECT * FROM " + TABELAHAIRPINBITUBULAR, null);


        if (mCursor.moveToFirst()) {
            while (!mCursor.isAfterLast()) {
                id = mCursor.getString(0);
                ScheduleDesignations = mCursor.getString(1);

                DNmm = mCursor.getString(2);
                NPSin = mCursor.getString(3);
                ODin = mCursor.getString(4);
                ODmm = mCursor.getString(5);
                IDin = mCursor.getString(6);
                IDmm = mCursor.getString(7);
                重量lb = mCursor.getString(8);
                重量kg = mCursor.getString(9);

                String[] descricao = {ScheduleDesignations, DNmm, NPSin};
                String[] valores = {ODin, ODmm, IDin, IDmm, 重量lb, 重量kg};
                //Pecorre as strings trocando as vírgulas por pontos
                TuboHairpin tubino = new TuboHairpin(id, descricao, valores);
                //Cria um objeto tubo com os valores passados:
                if(padraoProjeto==null){
                    listaTubos.add(tubino);
                    mCursor.moveToNext();
                }else{
                    if(ScheduleDesignations.contains(padraoProjeto) && !listaTubos.contains(tubino)){
                        listaTubos.add(tubino);
                        mCursor.moveToNext();
                        break;
                    }
                }


                //TuboHairpin tubino = new TuboHairpin();
            }

        }
        mCursor.moveToFirst();

        dbzinho.close();

    return  listaTubos;
    }

    public List<SpecHairpinMultitub> getDiametrosHairpinMultitub(){
        List<SpecHairpinMultitub> listaTubos = new ArrayList<SpecHairpinMultitub>();


        int id_tabela = TABELAHAIRPINMULTITUBULAR_ID;
        //Variáveis que salvarão os valores para serem passados p/ os objetos

        String id, NPSin, DNmm, tubes, ODin, ODmm, IDin, IDmm;

        Database dbzinho = new Database(mContext);
        dbzinho.open();
        Cursor mCursor = dbzinho.getDatabase(id_tabela);
        //mCursor = mDB.rawQuery("SELECT * FROM " + TABELAHAIRPINBITUBULAR, null);

        if (mCursor.moveToFirst()) {
            while (!mCursor.isAfterLast()) {
                id = mCursor.getString(0);
                DNmm = mCursor.getString(1);
                NPSin = mCursor.getString(2);
                tubes = mCursor.getString(3);
                ODin = mCursor.getString(4);
                ODmm = mCursor.getString(5);
                IDin = mCursor.getString(6);
                IDmm = mCursor.getString(7);

                String[] descricao = {DNmm, NPSin};
                String[] valores = {tubes, ODin, ODmm, IDin, IDmm};


                //Pecorre as strings trocando as vírgulas por pontos

                //Cria um objeto tubo com os valores passados:
                SpecHairpinMultitub tubino = new SpecHairpinMultitub(id, descricao, valores);
                listaTubos.add(tubino);
                mCursor.moveToNext();
                //TuboHairpin tubino = new TuboHairpin();
            }

        }
        mCursor.moveToFirst();

        dbzinho.close();
        return listaTubos;
    }

}


