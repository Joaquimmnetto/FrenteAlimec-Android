package com.alimec.joaquim.alimecproject.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alimec.joaquim.alimecproject.entidades.Produto;
import com.alimec.joaquim.alimecproject.entidades.Venda;
import com.alimec.joaquim.alimecproject.entidades.Item;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ALIMEC_DB";
    private static final int DB_VERSION = 7;


    private static DatabaseHelper instance;

    public static void initiate(Context context){
        instance = new DatabaseHelper(context);
    }

    public static DatabaseHelper getInstance() {
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Produto.Tabela.CREATE_TABLE);
        Log.d("DB",Produto.Tabela.CREATE_TABLE);
        db.execSQL(Venda.Tabela.CREATE_TABLE);
        Log.d("DB",Venda.Tabela.CREATE_TABLE);
        db.execSQL(Item.Tabela.CREATE_TABLE);
        Log.d("DB", Item.Tabela.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Produto.Tabela.DESTORY_TABLE);
        db.execSQL(Venda.Tabela.DESTORY_TABLE);
        db.execSQL(Item.Tabela.DESTORY_TABLE);
        onCreate(db);
    }


}
