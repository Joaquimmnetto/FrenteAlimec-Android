package com.alimec.joaquim.alimecproject.activities.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alimec.joaquim.alimecproject.R;
import com.alimec.joaquim.alimecproject.activities.ItemTO;
import com.alimec.joaquim.alimecproject.controle.ProdutoController;

import java.util.Locale;

import joaquimneto.com.alimec.model.Produto;


/**
 * Created by Joaquim Neto on 23/01/2015.
 */
public class ItemDialogFragment extends RetDialogFragment {

    public static final String ADD_ITEM_MODE = "produtoAdd";
    public static final String EDIT_ITEM_MODE = "produtoEdit";
    public static final String ITEM_SELECIONADO = "itemSelecionado";

    private Locale locale = new Locale("pt", "BR");

    private boolean modoEdicao;

    private ProdutoController produtoController = ProdutoController.getInstance();

    private Produto prodSelecionado = null;
    private ItemTO item = null;


    private AutoCompleteTextView descricao;
    private EditText quantidadeView;
    private double quantidade;

    private EditText unidadeView;

    private EditText precoUnitarioView;
    private double precoUnitario;

    private Spinner modoPgtoView;

    private EditText precoTotalView;
    private double precoTotal;

    private EditText comentarios;
    private EditText observacoes;


    private DialogInterface.OnClickListener positiveAction = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                descricao.requestFocusFromTouch();

                ItemTO itemTO = new ItemTO();
                itemTO.codProduto = prodSelecionado.getCodigo();
                itemTO.quantidade = quantidade;
                itemTO.unidade = unidadeView.getText().toString().isEmpty() ? "UN" : unidadeView.getText().toString();
                itemTO.precoUnitario = precoUnitario;
                itemTO.comentarios = comentarios.getText().toString();
                itemTO.meioPgto = modoPgtoView.getSelectedItem().toString();
                itemTO.observacoes = observacoes.getText().toString();

                if (prodSelecionado == null) {
                    throw new IllegalArgumentException("Selecione um produtoController!");
                }

//                ItemDialogFragment.this.item = itemTO;

                dialogSuccess(itemTO, true);

            } catch (NumberFormatException e) {
                dialogFail(e, "Precos, desconto e quantidade devem ser números!");
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                dialogFail(e, e.getMessage());
                e.printStackTrace();
            }
        }
    };

    private DialogInterface.OnClickListener neutralAction = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialogSuccess(item, false);
        }
    };

    private DialogInterface.OnClickListener negativeAction = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = View.inflate(getActivity(), R.layout.dialog_produto, null);

        init(dialogView);
        if (modoEdicao) {
            preencherValorArgumentos();
        }
        setAutoCompleteListeners();


        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()).
                setView(dialogView).
                setPositiveButton("Confirmar", positiveAction).
                setNegativeButton("Voltar", negativeAction);

        if (modoEdicao) {
            dialog.setNeutralButton("Remover", neutralAction);
        }

        return dialog.create();
    }


    private void init(View parent) {

        if (getArguments() != null) {
            item = (ItemTO) getArguments().getSerializable(ITEM_SELECIONADO);
            modoEdicao = (item != null);
        } else {
            modoEdicao = false;
        }

        if (modoEdicao) {
            prodSelecionado = produtoController.getProduto(item.codProduto);
        }

        descricao = (AutoCompleteTextView) parent.findViewById(R.id.produto_descricao);
        quantidadeView = (EditText) parent.findViewById(R.id.produto_quantidade);
        unidadeView = (EditText) parent.findViewById(R.id.produto_unidade);
        precoUnitarioView = (EditText) parent.findViewById(R.id.produto_precoUnit);
        modoPgtoView = (Spinner) parent.findViewById(R.id.produto_modoPgto);
        precoTotalView = (EditText) parent.findViewById(R.id.produto_precoTotal);
        comentarios = (EditText) parent.findViewById(R.id.produto_comentario);
        observacoes = (EditText) parent.findViewById(R.id.produto_observacoes);
    }


    private void preencherValorArgumentos() {
        descricao.setText(prodSelecionado.toString());
        quantidadeView.setText(String.format(locale, "%.3f", item.quantidade));
        unidadeView.setText(item.unidade);
        precoUnitarioView.setText(String.format(locale, "%.3f", item.precoUnitario));


        comentarios.setText(item.comentarios);
        observacoes.setText(item.observacoes);

    }


    private void setAutoCompleteListeners() {

        modoPgtoView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getActivity().getResources().getStringArray(R.array.meio_pagamento)));

        descricao.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, produtoController.getProdutos()));

        descricao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prodSelecionado = ((Produto) descricao.getAdapter().getItem(position));
            }
        });


        quantidadeView.addTextChangedListener(new TextWatcher() {
            String oldText;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(oldText)) {
                    return;
                }

                quantidade = toDouble(s.toString());
                precoTotal = quantidade * precoUnitario;

                if (precoTotalView.getText().toString().trim().equals("") && precoTotal == 0) {
                    return;
                }
                precoTotalView.setText(String.format(locale, "%.3f", precoTotal));
            }
        });

        precoUnitarioView.addTextChangedListener(new TextWatcher() {
            String oldText = "";

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldText = s.toString();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(oldText)) {
                    return;
                }

                precoUnitario = toDouble(s.toString());
                precoTotal = quantidade * precoUnitario;

                if (precoTotalView.getText().toString().trim().equals("") && precoTotal == 0) {
                    return;
                }
                precoTotalView.setText(String.format(locale, "%.3f", precoTotal));

            }
        });

        precoTotalView.addTextChangedListener(new TextWatcher() {
            String oldText;

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldText = s.toString();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(oldText)) {
                    return;
                }

                precoTotal = toDouble(s.toString());

                precoUnitario = precoTotal / quantidade;

                if (precoUnitarioView.getText().toString().trim().equals("") && precoUnitario == 0) {
                    return;
                }
                precoUnitarioView.setText(String.format(locale, "%.3f", precoUnitario));
            }

        });
    }

    private double toDouble(String entradaTxt) {
        try {
            entradaTxt = entradaTxt.replace(',', '.');

            return Double.parseDouble(entradaTxt);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Somente valores numéricos permitidos!", Toast.LENGTH_LONG).show();
            return 0.0;
        }

    }
}
