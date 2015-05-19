package com.alimec.joaquim.alimecproject.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.alimec.joaquim.alimecproject.entidades.Produto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProdutoRepository {

    private Produto[] produtos;
    private SQLiteDatabase db;
    private static ProdutoRepository instance;

    public static ProdutoRepository getInstance() {
        if(instance == null){
            instance = new ProdutoRepository(DatabaseHelper.getInstance());
        }
        return instance;
    }

    private ProdutoRepository(DatabaseHelper helper){
        db = helper.getWritableDatabase();
    }


    public void addProdutos(Produto[] produtos){
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Produto produto : produtos) {
                values.put(Produto.Tabela.CODIGO.toString(), produto.getCodigo());
                values.put(Produto.Tabela.DESCRICAO.toString(), produto.getDescricao());

                long insertResult = db.insertOrThrow(Produto.Tabela.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        }catch(SQLException e){
            e.printStackTrace();
        }
        db.endTransaction();

        this.produtos = getProdutosFromDatabase();
        Log.d("PRODUTOS",produtos.length+"");
    }

    public Produto[] getProdutos(){
        if(produtos == null){
            produtos = getProdutosFromDatabase();
            Arrays.sort(produtos, new Comparator<Produto>() {
                @Override
                public int compare(Produto lhs, Produto rhs) {
                    if(lhs == null && rhs == null){
                        return 0;
                    }
                    if(lhs == null){
                        return -1;
                    }
                    if(rhs == null){
                        return 1;
                    }
                    return lhs.getCodigo().compareTo(rhs.getCodigo());
                }
            });
        }
        return produtos;

    }

    public String[] getListaCodigos(){
        List<String> codigos = new ArrayList<String>();
        for(Produto p:getProdutos()){
           codigos.add(p.getCodigo());
        }
        Collections.sort(codigos);

        return codigos.toArray(new String[codigos.size()]);
    }

    public Produto getProduto(String codigo){
        int index = Arrays.binarySearch(produtos,codigo,new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                if(lhs != null && lhs instanceof Produto){
                    if(rhs != null && rhs instanceof String){
                        return ((Produto) lhs).getCodigo().compareTo((String)rhs);
                    }
                }
                return 0;
            }
        });
        if(index >= 0) {
            return produtos[index];
        }
        else{
            return null;
        }
    }

    public void limparProdutos() {
        db.beginTransaction();
        db.delete(Produto.Tabela.TABLE_NAME,null,null);
        db.endTransaction();
    }

    private Produto[] getProdutosFromDatabase() throws SQLiteException{
        List<Produto> produtos = new ArrayList<Produto>();
        Cursor c = db.query(Produto.Tabela.TABLE_NAME,null,null,null,null,null,null);
        while (c.moveToNext()){
            produtos.add(parseProduto(c));
        }

        return produtos.toArray(new Produto[produtos.size()]);
    }


    private Produto parseProduto(Cursor c){
        String codigo = c.getString(0);
        String descricao = c.getString(1);

        return new Produto(codigo,descricao);
    }


}
