package joaquimneto.com.alimec.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class Venda implements JSONable{


    private Date data;
    private String nomeCliente;
    private String cpfCnpj;
    private List<Item> itens = new ArrayList<Item>();

    public Venda(){
        this.data = new Date();
        this.itens = new ArrayList<Item>();
    }

    public Venda(Venda venda) {
        this(venda.data,venda.nomeCliente,venda.cpfCnpj);
        Collections.copy(itens,venda.getItens());
    }

    public Venda(Date data,String nomeCliente,String cpfCnpj){
        this(data,nomeCliente,cpfCnpj, new ArrayList<Item>());
    }

    public Venda(Date data,String nomeCliente,String cpfCnpj,List<Item> itens){
        this.data = data;
        this.nomeCliente = nomeCliente;
        this.cpfCnpj = cpfCnpj;
        
        if(itens != null){
        	this.itens = new ArrayList<>(itens);
        }
        else{
        	this.itens = null;
        }
        
    }

    public Date getData() {
	    return data;
	}

	public void setData(Date data) {
	    this.data = data;
	}

	public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
	    this.nomeCliente = nomeCliente;
	}

	public String getCpfCnpj() {
        return cpfCnpj;
    }


    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public List<Item> getItens() {
	    return itens;
	}

	public double getTotal() {
	    double total = 0;
	    for (Item item : getItens()) {
	        total += item.getPrecoTotal();
	    }
	
	    return total;
	}

	@Override
    public String toString(){

        String descricao = "Cliente:"+nomeCliente+"\n" +
                            "CPF:"+cpfCnpj+"\n" +
                            "\n";

        for(Item item: itens){
            descricao+=item+"\n";
        }


        return descricao;


    }
}
