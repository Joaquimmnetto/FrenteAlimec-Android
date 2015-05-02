package com.alimec.joaquim.alimecproject.venda;

import com.alimec.joaquim.alimecproject.ws.ServerServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class Venda implements JSONable{



    public static enum Tabela{
        DATA("data"),
        NOME_CLIENTE("nome_cliente"),
        CPF_CNPJ("cpf_cnpj"),
        ENVIADO("enviado");

        public static final String TABLE_NAME = Venda.class.getSimpleName();
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                DATA + " integer primary key," +
                NOME_CLIENTE + " varchar," +
                CPF_CNPJ+" varchar," +
                ENVIADO+" integer" +
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

    public static Venda fromJSON(String jsonStr){

        try {
            JSONObject object = new JSONObject(jsonStr);
            Date data = new Date(object.getLong(ServerServices.VendaJSONArgs.DATA.toString()));
            String nomeCliente = object.getString(ServerServices.VendaJSONArgs.CLIENTE.toString());
            String cpfCnpj = object.getString(ServerServices.VendaJSONArgs.CPFCNPJ.toString());

            JSONArray produtosJson = object.getJSONArray(ServerServices.VendaJSONArgs.ITENS.toString());

            Venda venda = new Venda(data,nomeCliente,cpfCnpj);

            for(int i = 0;i < produtosJson.length();i++){
                Item item = Item.fromJSON(venda,produtosJson.getJSONObject(i));

                venda.produtos.add(i,item);
            }

            return venda;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Venda(){
        this.data = new Date();

        produtos = new ArrayList<Item>();

    }



    public Venda(Date data,String nomeCliente,String cpfCnpj){
        this.data = data;
        this.nomeCliente = nomeCliente;
        this.cpfCnpj = cpfCnpj;
        produtos = new ArrayList<Item>();

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



    public JSONObject toJSON(){


        JSONObject object = new JSONObject();

        try {
            object.put(ServerServices.VendaJSONArgs.DATA.toString(),Long.toString(data.getTime()));
            object.put(ServerServices.VendaJSONArgs.CLIENTE.toString(),nomeCliente);
            object.put(ServerServices.VendaJSONArgs.CPFCNPJ.toString(),cpfCnpj);
            JSONArray items = new JSONArray();

            for(int i = 0; i < produtos.size(); i++){
                items.put(i,produtos.get(i));
            }

            object.put(ServerServices.VendaJSONArgs.ITENS.toString(),items);

            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String toString(){

        String descricao = "Cliente:"+nomeCliente+"\n" +
                            "CPF:"+cpfCnpj+"\n" +
                            "\n";

        for(Item item:produtos){
            descricao+=item+"\n";
        }


        return descricao;


    }

}
