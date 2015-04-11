package com.alimec.joaquim.alimecproject.venda;

import com.alimec.joaquim.alimecproject.persistence.ProdutoRepository;
import com.alimec.joaquim.alimecproject.ws.ServerServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class Item implements Serializable{



    //TODO: add comentarios
    public static enum Tabela{
      ID("id"),
       COD_PRODUTO("cod_produto"),
       COD_VENDA ("cod_venda"),
       QUANTIDADE ("quantidade"),
       UNIDADE("unidade"),
       PRECO_UNITARIO("preco_unitario"),
       DESCONTOS("descontos"),
       MEIO_PGTO("meio_pgto"),
       PRECO_TOTAL("preco_total"),
       COMENTARIO("comentario");

    public static final String TABLE_NAME = Item.class.getSimpleName();
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            ID + " integer primary key autoincrement," +
            COD_PRODUTO + " varchar," +
            COD_VENDA+" varchar," +
            QUANTIDADE+" integer," +
            UNIDADE + " varchar," +
            PRECO_UNITARIO+" real," +
            DESCONTOS + " integer," +
            MEIO_PGTO + " varchar," +
            PRECO_TOTAL+" real," +
            COMENTARIO+" varchar" +
            ");";
        public static final String DESTORY_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        private String nome;
       private Tabela(String nome){
           this.nome = nome;
       }
        @Override
        public String toString() {return nome;}
    }



    private Produto produto;
    private Venda venda;
    private double quantidade;
    private String unidade;
    private double precoUnitario;
    private double descontos;
    private String meioPgto;
    private double precoTotal;
    private String comentario;

//    public static Item buildWithJSON(String json) throws JSONException {
//
//        JSONObject object = new JSONObject(json);
//
//        String prodCode = object.getString(Tabela.COD_PRODUTO.toString());
//        String vendaCode = object.getString(Tabela.COD_VENDA.toString());
//        double quantidade = object.getDouble(Tabela.QUANTIDADE.toString());
//        String unidade = object.getString(Tabela.UNIDADE.toString());
//        double precoUnitario = object.getDouble(Tabela.PRECO_UNITARIO.toString());
//        double descontos = object.getDouble(Tabela.DESCONTOS.toString());
//        String meioPgto = object.getString(Tabela.MEIO_PGTO.toString());
//        double precoTotal = object.getDouble(Tabela.PRECO_TOTAL.toString());
//
//        return new Item(ProdutoRepository.getInstance().getProduto(prodCode),null,quantidade,unidade,precoUnitario,descontos,meioPgto,precoTotal);
//    }

    public Item(Produto produto, Venda venda, double quantidade, String unidade, double precoUnitario, double descontos,String meioPgto, double precoTotal,String comentario) {
        this.produto = produto;
        this.venda = venda;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.precoUnitario = precoUnitario;
        this.descontos = descontos;
        this.meioPgto = meioPgto;
        this.precoTotal = precoTotal;
    }


    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getDescontos() {
        return descontos;
    }

    public void setDescontos(double descontos) {
        this.descontos = descontos;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getUnidade() {
        return unidade;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }


    public String getMeioPgto() {
        return meioPgto;
    }

    public void setMeioPgto(String meioPgto) {
        this.meioPgto = meioPgto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public JSONObject toJSON() throws JSONException {


        JSONObject obj = new JSONObject();

        obj.put(ServerServices.JSONArgs.CODITEM.toString(),produto.getCodigo());
        obj.put(ServerServices.JSONArgs.DATA.toString(),String.valueOf(venda.getData().getTime()));
        obj.put(ServerServices.JSONArgs.QUANTIDADE.toString(),getQuantidade());
        obj.put(ServerServices.JSONArgs.UNIDADE.toString(),getUnidade());
        obj.put(ServerServices.JSONArgs.MEIO_PAGAMENTO.toString(),getMeioPgto());
        obj.put(ServerServices.JSONArgs.VALOR_TOTAL.toString(),getPrecoTotal());
        obj.put(ServerServices.JSONArgs.CLIENTE.toString(),getVenda().getNomeCliente());
        obj.put(ServerServices.JSONArgs.COMPLEMENTO.toString(),getComentario());



        return obj;
    }
}