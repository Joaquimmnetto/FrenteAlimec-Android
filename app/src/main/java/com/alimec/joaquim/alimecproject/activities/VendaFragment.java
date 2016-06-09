package com.alimec.joaquim.alimecproject.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alimec.joaquim.alimecproject.R;
import com.alimec.joaquim.alimecproject.activities.adapter.ItemAdapter;
import com.alimec.joaquim.alimecproject.activities.fragment.dialog.ItemDialogFragment;
import com.alimec.joaquim.alimecproject.activities.fragment.dialog.RetDialogFragment;
import com.alimec.joaquim.alimecproject.controle.VendaController;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.vendas.VendaResult;
import joaquimneto.com.alimec.vendas.VendasModuleException;

/**
 * Created by KithLenovo on 21/08/2015.
 */
public class VendaFragment extends Fragment {

//    public static final String VENDA_SHARED_PREF = "VENDA_PREFS";

    private EditText vCliente;
    private String cliente;

    private EditText vCpfCnpj;
    private String cpfCnpj;

    private Button addItem;
    private Button confirmar;
    private ViewGroup baseView;
    private ListView produtoList;

    private ItemAdapter adapter;

    private VendaController venda = new VendaController();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLogic();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        baseView = (ViewGroup) ViewGroup.inflate(getActivity(), R.layout.layout_venda, null);
        initView();
        setListeners();


        return baseView;
    }


    @Override
    public void onResume() {
        super.onResume();

        //TODO: porque eu comentei isso?
//        SharedPreferences pref = getActivity().getPreferences(Activity.MODE_PRIVATE);
//
//        String jsonStr = pref.getString(VENDA_SHARED_PREF, null);
//        if (jsonStr != null) {
//            venda = new VendaBuilder(Venda.fromJSON(jsonStr));
//
//        }
//        atualizarTotal();
        preencherTela(venda);
    }

    private void preencherTela(VendaController venda) {
        vCliente.setText(cliente);
        vCpfCnpj.setText(cpfCnpj);
        adapter.setItens(venda.getItens());

        atualizarTotal();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onPause() {
        super.onPause();
        //TODO: porque eu comentei isso? Arranjar alternativas.
//        SharedPreferences pref = getActivity().getPreferences(Activity.MODE_PRIVATE);
//
//        preencherVenda(venda);
//        pref.edit().putString(VENDA_SHARED_PREF, venda.getVenda().toJSON().toString());
//        pref.edit().apply();
    }


    private void initLogic() {
        venda = new VendaController();
        adapter = new ItemAdapter(getActivity().getApplicationContext(), venda.getItens());
    }

    private void initView() {

        produtoList = ((ListView) baseView.findViewById(R.id.venda_listItens));
        produtoList.setAdapter(adapter);

        vCliente = (EditText) baseView.findViewById(R.id.venda_nomeCliente);
        vCpfCnpj = (EditText) baseView.findViewById(R.id.venda_cpfcnpj);

        addItem = (Button) baseView.findViewById(R.id.venda_addProduto);
        confirmar = (Button) baseView.findViewById(R.id.venda_confirmar);
    }

    private void setListeners() {
        produtoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickProdutoListItem(view, (Item) parent.getItemAtPosition(position));
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddProduto(v);
            }
        });

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickConfirmar(v);
            }
        });

    }

    public void onClickConfirmar(View view) {

        AlertDialog confirmDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Confirmar Venda")
                .setMessage("Você tem certeza que deseja completar essa venda?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String message = null;
                        try {
                            VendaResult result = venda.finalizar(cliente, cpfCnpj);

                            message = "Erro ao salvar a venda, tente novamente";
                            if (result.isEnviado() && result.isSalvo()) {
                                message = "Venda enviada e salva com sucesso.";
                            }
                            if (!result.isEnviado() && result.isSalvo()) {
                                message = "Falha ao enviar venda, venda salva para envio posterior.";
                            }
                            if (result.isSalvo()) {
                                limparFragmento();
                            }
                        } catch (VendasModuleException e) {
                            e.printStackTrace();
                            message = e.getMessage();
                        }
                        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        confirmDialog.setMessage("Você deseja realizar essa venda?\n" + venda.toString());

        confirmDialog.show();
    }


    public void onClickAddProduto(View view) {
        final ItemDialogFragment dialog = new ItemDialogFragment();

        dialog.setDialogSuccessListener(new RetDialogFragment.DialogSuccessListener() {
            @Override
            public void onDialogSuccess(Object... obj) {
                ItemTO item = (ItemTO) obj[0];
                boolean manter = (boolean) obj[1];
                if (manter) {
                    try {
                        venda.addItem(item);
                    } catch (VendasModuleException e) {
                        e.printStackTrace();
                        Toast.makeText(dialog.getActivity(), "Erro ao adcionar o item!", Toast.LENGTH_SHORT).show();
                    }

                    adapter.notifyDataSetChanged();
                }
                atualizarTotal();
            }
        });

        dialog.show(getFragmentManager(), ItemDialogFragment.ADD_ITEM_MODE);
    }


    public void onClickProdutoListItem(View view, final Item item) {
        ItemDialogFragment dialog = new ItemDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(ItemDialogFragment.ITEM_SELECIONADO, item);
        dialog.setArguments(args);

        dialog.setDialogSuccessListener(new RetDialogFragment.DialogSuccessListener() {
            @Override
            public void onDialogSuccess(Object... obj) {
                Item item = (Item) obj[0];
                boolean manter = (boolean) obj[1];

                venda.atualizaItem(item, manter);

                adapter.notifyDataSetChanged();
                atualizarTotal();
            }
        });

        dialog.show(getFragmentManager(), ItemDialogFragment.EDIT_ITEM_MODE);
    }

    private void limparFragmento() {

    }


    private void atualizarTotal() {
        ((TextView) baseView.findViewById(R.id.venda_total)).setText(String.format("R$%.2f", venda.calcularTotal()));
    }


}
