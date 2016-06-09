package com.alimec.joaquim.alimecproject.activities.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alimec.joaquim.alimecproject.R;

import java.util.List;

import joaquimneto.com.alimec.model.Venda;

/**
 * Created by KithLenovo on 22/08/2015.
 */
public class VendaAdapter extends BaseAdapter {

    private Context context;
    private List<Venda> vendas;


    public VendaAdapter(Context context, List<Venda> vendas) {
        this.context = context;
        this.vendas = vendas;
    }


    @Override
    public int getCount() {
        return vendas.size();
    }

    @Override
    public Object getItem(int position) {
        return vendas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = null;
        if (convertView != null && convertView instanceof ViewGroup) {
            v = convertView;
        } else {
            v = View.inflate(context, R.layout.item_lista_venda, null);
        }


        Venda venda = vendas.get(position);
        ListView itens = (ListView) v.findViewById(R.id.lista_venda_itens);
        ItemAdapter adapter = new ItemAdapter(context, venda.getItens());


        ((TextView) v.findViewById(R.id.lista_venda_cliente)).setText(venda.getNomeCliente());
        ((TextView) v.findViewById(R.id.lista_venda_cpfcnpj)).setText(venda.getCpfCnpj());

        ((TextView) v.findViewById(R.id.lista_venda_total)).setText(String.format("R$%.2f", venda.getTotal()));

        itens.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return v;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }
}
