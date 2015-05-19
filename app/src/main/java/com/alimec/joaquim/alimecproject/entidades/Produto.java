package com.alimec.joaquim.alimecproject.entidades;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class Produto implements Serializable,Comparable<Produto>{


    public static enum Tabela{
        CODIGO("codigo"),
        DESCRICAO("descricao");

        public static final String TABLE_NAME = Produto.class.getSimpleName();
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                CODIGO + " varchar primary key unique," +
                DESCRICAO + " varchar);";
        public static final String DESTORY_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        private String nome;
        private Tabela(String nome){
            this.nome = nome;
        }
        @Override
        public String toString() {return nome;}
    }


    private String codigo;
    private String descricao;


    public Produto(JSONObject json) throws JSONException {
        this.codigo = json.getString("codigo");
        this.descricao = json.getString("descricao");
    }

    public Produto(String codigo, String descricao){
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }


    @Override
    public String toString() {
        return codigo+" - "+descricao;
    }

    @Override
    public boolean equals(Object another) {

        if(another == null || !(another instanceof Produto)){
            return false;
        }
        return this.getCodigo().equals(((Produto) another).getCodigo())
                                        &&
               this.getDescricao().equals(((Produto) another).getDescricao());
    }

    @Override
    public int compareTo(Produto another) {

        if(another == null){
            return 1;
        }
        return another.getCodigo().compareTo(another.getCodigo());
    }
}
