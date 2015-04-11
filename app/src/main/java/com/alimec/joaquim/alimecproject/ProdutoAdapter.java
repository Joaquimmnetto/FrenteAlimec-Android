package com.alimec.joaquim.alimecproject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alimec.joaquim.alimecproject.venda.Item;

import java.util.List;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class ProdutoAdapter extends BaseAdapter {

    private List<Item> produtos;
    private Context context;

    public ProdutoAdapter(Context context,List<Item> produtos){
        this.context = context;
        this.produtos = produtos;
    }


    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Object getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context,R.layout.listitem_produto,null);
        Item prod = produtos.get(position);

        ((TextView)v.findViewById(R.id.listItem_produto_descricao)).setText(prod.getProduto().getCodigo()+" - "+prod.getProduto().getDescricao());
        ((TextView)v.findViewById(R.id.listItem_produto_quantidade)).setText(String.format("%.2f",prod.getQuantidade()));
        ((TextView)v.findViewById(R.id.listItem_produto_unidade)).setText(prod.getUnidade());
        ((TextView)v.findViewById(R.id.listItem_produto_valorUnitario)).setText(String.format("R$%.2f",prod.getPrecoUnitario()));
        ((TextView)v.findViewById(R.id.listItem_produto_total)).setText(String.format("R$%.2f",prod.getPrecoTotal()));


        return v;
    }

    public List<Item> getProdutos(){
        return produtos;

    }

}
