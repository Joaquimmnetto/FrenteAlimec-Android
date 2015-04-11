package com.alimec.joaquim.alimecproject.persistence;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alimec.joaquim.alimecproject.venda.Venda;
import com.alimec.joaquim.alimecproject.venda.Item;

/**
 * Created by KithLenovo on 25/01/2015.
 */
public class VendaDAO {

    private SQLiteDatabase db;

    public VendaDAO(DatabaseHelper helper){
        db = helper.getWritableDatabase();
    }

    public void addVenda(Venda v){
        ItemDAO dao = new ItemDAO(DatabaseHelper.getInstance());
    try{
        for(Item item:v.getProdutos()){
            item.setVenda(v);
            dao.addItem(item);
        }

        ContentValues values = new ContentValues();
        values.put(Venda.Tabela.DATA.toString(),String.valueOf(v.getData().getTime()));
        values.put(Venda.Tabela.NOME_CLIENTE.toString(),v.getNomeCliente());
        values.put(Venda.Tabela.CPF_CNPJ.toString(),v.getCpfCnpj());
        db.beginTransaction();
            long result = db.insert(Venda.Tabela.TABLE_NAME,null,values);

        }catch(SQLException e){
            e.printStackTrace();
        }
        db.endTransaction();
    }
}
