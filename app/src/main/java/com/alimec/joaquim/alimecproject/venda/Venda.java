package com.alimec.joaquim.alimecproject.venda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class Venda {

    public static enum Tabela{
        DATA("data"),
        NOME_CLIENTE("nome_cliente"),
        CPF_CNPJ("cpf_cnpj");

        public static final String TABLE_NAME = Venda.class.getSimpleName();
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                DATA + " varchar primary key," +
                NOME_CLIENTE + " varchar," +
                CPF_CNPJ+" varchar" +
                ");";
        public static final String DESTORY_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        private String nome;
        private Tabela(String nome){
            this.nome = nome;
        }
        @Override
        public String toString() {return nome;}
    }

    private Date data;
    private String nomeCliente;
    private String cpfCnpj;
    private List<Item> produtos;

    public Venda(){
        produtos = new ArrayList<Item>();

    }



    public Venda(String nomeCliente,String cpfCnpj,Date data){
        this.nomeCliente = nomeCliente;
        this.cpfCnpj = cpfCnpj;
        produtos = new ArrayList<Item>();
        this.data = data;

    }



    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }


    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }


    public List<Item> getItens() {
        return produtos;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public List<Item> getProdutos() {
        return produtos;
    }


}
