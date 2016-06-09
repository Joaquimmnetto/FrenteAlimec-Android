package com.alimec.joaquim.alimecproject.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alimec.joaquim.alimecproject.R;
import com.alimec.joaquim.alimecproject.activities.adapter.VendaAdapter;
import com.alimec.joaquim.alimecproject.controle.BuscaVendaController;

import java.util.List;

import joaquimneto.com.alimec.model.Venda;

/**
 * Created by KithLenovo on 21/08/2015.
 */
public class BuscaVendasFragment extends Fragment {


    private Button mes;
    private Button hoje;
    private Button hora;
    private Button outros;

    private ListView vendas;
    private VendaAdapter adapter;
    private BuscaVendaController buscaControl = new BuscaVendaController();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLogic();
    }

    private void initLogic() {
        adapter = new VendaAdapter(getActivity().getApplicationContext(), buscaControl.buscaHoje());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup baseView = (ViewGroup) View.inflate(getActivity(), R.layout.layout_lista_vendas, null);
        initView(baseView);
        setListeners();
        return baseView;
    }


    private void initView(View baseView) {
        mes = (Button) baseView.findViewById(R.id.lista_vendas_mes);
        hoje = (Button) baseView.findViewById(R.id.lista_vendas_hoje);
        hora = (Button) baseView.findViewById(R.id.lista_vendas_hora);
        outros = (Button) baseView.findViewById(R.id.lista_vendas_outros);

        vendas = (ListView) baseView.findViewById(R.id.lista_vendas_vendas);
        vendas.setAdapter(adapter);
    }

    private void setListeners() {
        mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateList(adapter, buscaControl.buscaMes());
            }
        });

        hoje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateList(adapter, buscaControl.buscaHoje());
            }
        });

        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateList(adapter, buscaControl.buscaHora());
            }
        });

        outros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: abrir dialog de busca avancada
            }
        });

        vendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickVenda(view, (Venda) vendas.getItemAtPosition(position));
            }
        });
    }

    private void onClickVenda(View v, Venda venda) {
        View expansao = v.findViewById(R.id.lista_venda_itens);
        boolean expandido = expansao.getVisibility() == View.VISIBLE;
        TextView expansaoIndicador = (TextView) v.findViewById(R.id.lista_venda_expandir);

        int corMenos = getActivity().getResources().getColor(android.R.color.holo_red_dark);
        int corMais = getActivity().getResources().getColor(android.R.color.holo_blue_bright);


        if (!expandido) {
            expansaoIndicador.setText("-");
            expansaoIndicador.setTextColor(corMenos);
            expansao.setVisibility(View.VISIBLE);
        } else {
            expansaoIndicador.setText("+");
            expansaoIndicador.setTextColor(corMais);
            expansao.setVisibility(View.GONE);
        }
        v.invalidate();
    }

    private void populateList(VendaAdapter adapter, List<Venda> vendas) {
        adapter.setVendas(vendas);
        adapter.notifyDataSetChanged();
    }
}