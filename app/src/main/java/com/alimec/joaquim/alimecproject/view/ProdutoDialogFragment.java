package com.alimec.joaquim.alimecproject.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alimec.joaquim.alimecproject.R;
import com.alimec.joaquim.alimecproject.activities.VendaActivity;
import com.alimec.joaquim.alimecproject.persistence.ProdutoRepository;
import com.alimec.joaquim.alimecproject.venda.Produto;
import com.alimec.joaquim.alimecproject.venda.Item;


/**
 * Created by Joaquim Neto on 23/01/2015.
 */
public class ProdutoDialogFragment extends DialogFragment {


    private Produto prodSelecionado = null;
    private Item item = null;
    private AutoCompleteTextView descricao;
    private EditText quantidade;
    private EditText unidade;
    private EditText precoUnitario;
    private Spinner modoPgto;
    private EditText precoTotal;
    private DialogInterface.OnClickListener positiveAction = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                descricao.requestFocusFromTouch();

                double qtd = getDouble(quantidade);
                String und = unidade.getText().toString().isEmpty() ? "UN" : unidade.getText().toString();
                double precoUn = getDouble(precoUnitario);
                double total = getDouble(precoTotal);
                String comentarioStr = comentario.getText().toString();
                String meioPgto = modoPgto.getSelectedItem().toString();

                //Caso de edicao{
                if (prodSelecionado == null) {
                    if (item == null) {
                        throw new IllegalArgumentException("Selecione um item!");
                    } else {
                        prodSelecionado = item.getProduto();
                    }
                }

                if (item != null) {
                    ((VendaActivity) getActivity()).removerVendaProduto(item);
                }
                //}



                item = new Item(prodSelecionado, null, qtd, und, precoUn, 0.0, meioPgto, total, comentarioStr);
                ((VendaActivity) getActivity()).addVendaProduto(item);

            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Precos, desconto e quantidade devem ser números!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    };
    private EditText comentario;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = View.inflate(getActivity(), R.layout.fragment_produto, null);

        init(dialogView);
        if (item != null) {
            preencherValorArgumentos(dialogView);
        }
        setAutoCompleteListeners(dialogView);
        setCalculosListeners();

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).
                setView(dialogView).
                setPositiveButton("Confirmar", positiveAction).create();


        return dialog;
    }

    private void preencherValorArgumentos(View dialogView) {
        descricao.setText(item.getProduto().toString());
        quantidade.setText(String.format("%.2f", item.getQuantidade()));
        unidade.setText(item.getUnidade());
        precoUnitario.setText(String.format("%.2f", item.getPrecoUnitario()));
        precoTotal.setText(String.format("%.2f", item.getPrecoTotal()));

    }

    private void init(View parent) {

        if (getArguments() != null) {
            item = (Item) getArguments().getSerializable(VendaActivity.PROD_SELECIONADO);
        }
        descricao = (AutoCompleteTextView) parent.findViewById(R.id.produto_descricao);
        quantidade = (EditText) parent.findViewById(R.id.produto_quantidade);
        unidade = (EditText) parent.findViewById(R.id.produto_unidade);
        precoUnitario = (EditText) parent.findViewById(R.id.produto_precoUnit);
        modoPgto = (Spinner) parent.findViewById(R.id.produto_modoPgto);
        precoTotal = (EditText) parent.findViewById(R.id.produto_precoTotal);
        comentario = (EditText) parent.findViewById(R.id.produto_comentario);
    }

    private void setCalculosListeners() {
        View.OnFocusChangeListener atualizarPrecos = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    atualizarPrecos(v);
                }
            }
        };
        quantidade.setOnFocusChangeListener(atualizarPrecos);
        precoUnitario.setOnFocusChangeListener(atualizarPrecos);
        precoTotal.setOnFocusChangeListener(atualizarPrecos);


    }

    private void atualizarPrecos(View focused) {
        double qtdVal = quantidade.getText().toString().isEmpty() ? 0.0 : getDouble(quantidade); //valor fixo
        double precoUntVal = precoUnitario.getText().toString().isEmpty() ? 0.0 : getDouble(precoUnitario); //varia c/ calculos
        double precoTotVal = precoTotal.getText().toString().isEmpty() ? 0.0 : getDouble(precoTotal); //varia c/ calculos
        if (focused.equals(quantidade)) {
            precoTotVal = qtdVal * precoUntVal;
        } else if (focused.equals(precoUnitario)) {
            precoTotVal = qtdVal * precoUntVal;
        } else if (focused.equals(precoTotal)) {
            precoUntVal = qtdVal > 0 ? precoTotVal / qtdVal : 0.0;
        }

        quantidade.setText(String.format("%.2f", qtdVal));
        precoUnitario.setText(String.format("%.2f", precoUntVal));
        precoTotal.setText(String.format("%.2f", precoTotVal));

    }


    private double getDouble(TextView entrada) {
        try {
            String entradaTxt = entrada.getText().toString();
            entradaTxt = entradaTxt.replace(',', '.');

            return Double.parseDouble(entradaTxt);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Somente valores numéricos permitidos!", Toast.LENGTH_LONG).show();
            return 0.0;
        }

    }

    private void setAutoCompleteListeners(View parent) {

        modoPgto.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getActivity().getResources().getStringArray(R.array.meio_pagamento)));

        descricao.setAdapter(new ArrayAdapter<Produto>(getActivity(), android.R.layout.simple_list_item_1, ProdutoRepository.getInstance().getProdutos()));

        descricao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prodSelecionado = ((Produto) descricao.getAdapter().getItem(position));
            }
        });

    }
}
