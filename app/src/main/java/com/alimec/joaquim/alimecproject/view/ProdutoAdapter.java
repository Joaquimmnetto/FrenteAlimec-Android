package com.alimec.joaquim.alimecproject.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alimec.joaquim.alimecproject.R;
import com.alimec.joaquim.alimecproject.entidades.Item;
import com.alimec.joaquim.alimecproject.entidades.Venda;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class ProdutoAdapter extends BaseAdapter {

    private Venda venda;
    private Context context;

    public ProdutoAdapter(Context context,Venda venda){
        this.context = context;
        this.venda = venda;
    }




    @Override
    public int getCount() {
        return venda.getProdutos().size();
    }

    @Override
    public Object getItem(int position) {
        return venda.getProdutos().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.listitem_produto,null);
        Item prod = (Item) getItem(position);

        ((TextView)v.findViewById(R.id.listItem_produto_descricao)).setText(prod.getProduto().getDescricao());
        ((TextView)v.findViewById(R.id.listItem_produto_quantidade)).setText(String.format("%.2f",prod.getQuantidade()));
        ((TextView)v.findViewById(R.id.listItem_produto_unidade)).setText(prod.getUnidade());
        ((TextView)v.findViewById(R.id.listItem_produto_valorUnitario)).setText(String.format("R$%.2f",prod.getPrecoUnitario()));
        ((TextView)v.findViewById(R.id.listItem_produto_total)).setText(String.format("R$%.2f",prod.getPrecoTotal()));


        return v;
    }


    public void setVenda(Venda venda){
        this.venda = venda;
    }

}
