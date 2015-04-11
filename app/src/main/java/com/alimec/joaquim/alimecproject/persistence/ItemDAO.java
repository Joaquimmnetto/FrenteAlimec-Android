package com.alimec.joaquim.alimecproject.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alimec.joaquim.alimecproject.venda.Item;
import com.alimec.joaquim.alimecproject.venda.Produto;
import com.alimec.joaquim.alimecproject.venda.Venda;

import java.util.ArrayList;
import java.util.List;

class ItemDAO {

    private SQLiteDatabase db;


    public ItemDAO(DatabaseHelper helper){
        db = helper.getWritableDatabase();

    }

    public void addItem(Item p){
        ContentValues values = new ContentValues();
        values.put(Item.Tabela.COD_PRODUTO.toString(),p.getProduto().getCodigo());
        values.put(Item.Tabela.COD_VENDA.toString(),p.getVenda().getData().getTime()+"");
        values.put(Item.Tabela.QUANTIDADE.toString(),p.getQuantidade());
        values.put(Item.Tabela.UNIDADE.toString(),p.getUnidade());
        values.put(Item.Tabela.PRECO_UNITARIO.toString(),p.getPrecoUnitario());
        values.put(Item.Tabela.MEIO_PGTO.toString(),p.getMeioPgto());
        values.put(Item.Tabela.PRECO_TOTAL.toString(),p.getPrecoTotal());
        values.put(Item.Tabela.COMENTARIO.toString(),p.getComentario());

        db.beginTransaction();
        try{
            long result = db.insert(Item.Tabela.TABLE_NAME,null,values);

        }catch(SQLException e){
            e.printStackTrace();
        }
        db.endTransaction();
    }

    public void addItens(List<Item> produtos) {
        for(Item p:produtos){
            addItem(p);
        }


    }

    public Item[] getItens(Venda venda){
        List<Item> produtos = new ArrayList<Item>();
        String where = Item.Tabela.COD_VENDA+"="+venda.getData().getTime();
        Cursor c = db.query(Item.Tabela.TABLE_NAME,null,where,null,null,null,null);
        while(c.moveToNext()){
            produtos.add(parseItem(c, venda));
        }
        return produtos.toArray(new Item[produtos.size()]);
    }


    private Item parseItem(Cursor c, Venda venda){
        Produto prod = ProdutoRepository.getInstance().getProduto(c.getString(c.getColumnIndex(Item.Tabela.COD_PRODUTO.toString())));
        double quantidade = c.getDouble(c.getColumnIndex(Item.Tabela.QUANTIDADE.toString()));
        String unidade = c.getString(c.getColumnIndex(Item.Tabela.UNIDADE.toString()));
        double precoUnitario = c.getDouble(c.getColumnIndex(Item.Tabela.PRECO_UNITARIO.toString()));
        double desconto = c.getDouble(c.getColumnIndex(Item.Tabela.DESCONTOS.toString()));
        String meioPgto = c.getString(c.getColumnIndex(Item.Tabela.MEIO_PGTO.toString()));
        double precoTotal = c.getDouble(c.getColumnIndex(Item.Tabela.PRECO_TOTAL.toString()));
        String comentario = c.getString(c.getColumnIndex(Item.Tabela.COMENTARIO.toString()));

        return new Item(prod,venda,quantidade,unidade,precoUnitario,desconto,meioPgto,precoTotal,comentario);
    }


}
