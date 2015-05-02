package com.alimec.joaquim.alimecproject.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alimec.joaquim.alimecproject.venda.Item;
import com.alimec.joaquim.alimecproject.venda.Venda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by KithLenovo on 25/01/2015.
 */
public class VendaDAO {

    private SQLiteDatabase db;

    public VendaDAO(DatabaseHelper helper) {
        db = helper.getWritableDatabase();
    }

    public void addVenda(Venda v) throws SQLException {
        ItemDAO dao = new ItemDAO(DatabaseHelper.getInstance());

        for (Item item : v.getProdutos()) {
            item.setVenda(v);
            dao.addItem(item);
        }

        ContentValues values = new ContentValues();
        values.put(Venda.Tabela.DATA.toString(), v.getData().getTime());
        values.put(Venda.Tabela.NOME_CLIENTE.toString(), v.getNomeCliente());
        values.put(Venda.Tabela.CPF_CNPJ.toString(), v.getCpfCnpj());
        db.beginTransaction();
        long result = db.insert(Venda.Tabela.TABLE_NAME, null, values);
        db.endTransaction();


    }

    public Venda[] getVendasAPartirDe(Date startingDate) {

        ItemDAO dao = new ItemDAO(DatabaseHelper.getInstance());
        List<Venda> vendas = new ArrayList<>();

        String where = Venda.Tabela.DATA + " > " + startingDate.getTime();
        String orderBy = Venda.Tabela.DATA + " ASC";


        Cursor c = db.query(true, Venda.Tabela.TABLE_NAME, null, where, null, null, null, orderBy, null);
        if (!c.isAfterLast()) {
            do {
                Venda venda = parseVenda(c);
                venda.getProdutos().addAll(Arrays.asList(dao.getItensFromVenda(venda)));
                vendas.add(venda);

            } while (c.moveToNext());

        }
        return vendas.toArray(new Venda[vendas.size()]);
    }


    private Venda parseVenda(Cursor c) {
        long data = c.getLong(c.getColumnIndex(Venda.Tabela.DATA.toString()));
        String nome = c.getString(c.getColumnIndex(Venda.Tabela.NOME_CLIENTE.toString()));
        String cnpjCpf = c.getString(c.getColumnIndex(Venda.Tabela.CPF_CNPJ.toString()));

        return new Venda(new Date(data), nome, cnpjCpf);
    }


}
