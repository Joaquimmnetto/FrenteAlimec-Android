package com.alimec.joaquim.alimecproject.activities.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alimec.joaquim.alimecproject.R;

import java.util.List;

import joaquimneto.com.alimec.model.Item;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class ItemAdapter extends BaseAdapter {

    private Context context;
    private List<Item> itens;

    public ItemAdapter(Context context, List<Item> itens) {
        this.context = context;
        this.itens = itens;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null){
            v = View.inflate(context, R.layout.item_lista_item, null);
        }
        final Item item = (Item) getItem(position);
        TextView descricao = ((TextView) v.findViewById(R.id.listItem_item_descricao));
        descricao.setText(item.getProduto().getDescricao());
        ((TextView) v.findViewById(R.id.listItem_item_quantidade)).setText(String.format("%.2f", item.getQuantidade()));
        ((TextView) v.findViewById(R.id.listItem_item_unidade)).setText(item.getUnidade());
        ((TextView) v.findViewById(R.id.listItem_item_valorUnitario)).setText(String.format("R$%.2f", item.getPrecoUnitario()));
        ((TextView) v.findViewById(R.id.listItem_item_total)).setText(String.format("R$%.2f", item.getPrecoTotal()));
        ((TextView) v.findViewById(R.id.listItem_item_meioPgto)).setText(item.getMeioPgto());

        return v;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }
}
