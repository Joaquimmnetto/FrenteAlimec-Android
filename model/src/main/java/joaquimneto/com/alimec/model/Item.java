package joaquimneto.com.alimec.model;

import java.io.Serializable;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class Item implements Serializable, JSONable, Cloneable {


    /**
     *
     */
    private static final long serialVersionUID = 4030022527137087920L;


    private long id;
    private Produto produto;
    private Venda venda;
    private double quantidade;
    private String unidade;
    private double precoUnitario;
    private double descontos;
    private String meioPgto;
    private double precoTotal;
    private String comentario;
    private String observacoes;

    public Item(long id, Produto produto, Venda venda, double quantidade, String unidade, double precoUnitario, double descontos, String meioPgto, double precoTotal, String comentario, String observacoes) {
        this.id = id;
        this.produto = produto;
        this.venda = venda;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.precoUnitario = precoUnitario;
        this.descontos = descontos;
        this.meioPgto = meioPgto;
        this.precoTotal = precoTotal;
        this.comentario = comentario;
        this.observacoes = observacoes;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public double getDescontos() {
        return descontos;
    }

    public void setDescontos(double descontos) {
        this.descontos = descontos;
    }

    public String getMeioPgto() {
        return meioPgto;
    }

    public void setMeioPgto(String meioPgto) {
        this.meioPgto = meioPgto;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return quantidade + " " + unidade + " " + produto + " = " + precoTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item)) {
            return false;
        }

        if(venda == null && ((Item) o).getVenda() == null){
            return id == ((Item) o).getId();
        }

        if( (venda == null && ((Item) o).getVenda() != null) || ( ((Item) o).getVenda() == null && venda != null) ){
            return false;
        }


        return (venda.equals(((Item) o).getVenda()) && id == ((Item) o).getId());
    }
}