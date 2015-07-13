package com.alimec.joaquim.alimecproject.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alimec.joaquim.alimecproject.controle.VendaController;
import com.alimec.joaquim.alimecproject.controle.VendaResult;
import com.alimec.joaquim.alimecproject.view.ProdutoAdapter;
import com.alimec.joaquim.alimecproject.view.ProdutoDialogFragment;
import com.alimec.joaquim.alimecproject.R;
import com.alimec.joaquim.alimecproject.entidades.Venda;
import com.alimec.joaquim.alimecproject.entidades.Item;

import org.json.JSONException;

import java.io.IOException;
import java.util.Date;


public class VendaActivity extends ActionBarActivity {

    public static final String PROD_SELECIONADO = "PROD_SELECIONADO";

    public static final String VENDA_SHARED_PREF = "VENDA_PREFS";


    private Venda venda = new Venda();
    private ProdutoAdapter adapter;
    private ListView produtoList;
    private VendaController controler = new VendaController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_venda);
        init();
        setListeners();
    }

    @Override
    protected void onStop() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);

        preencherVenda(venda);
        pref.edit().putString(VENDA_SHARED_PREF, venda.toJSON().toString());
        pref.edit().apply();


        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences pref = getPreferences(MODE_PRIVATE);

        String jsonStr = pref.getString(VENDA_SHARED_PREF, null);
        if (jsonStr != null) {
            venda = Venda.fromJSON(jsonStr);
        }

        init();
        setListeners();
        atualizarTotal();

        adapter.notifyDataSetChanged();

    }

    private void setListeners() {
        produtoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickProdutoListItem(view, (Item) parent.getItemAtPosition(position));
            }
        });

    }

    private void init() {
        adapter = new ProdutoAdapter(getApplicationContext(), venda);
        produtoList = ((ListView) findViewById(R.id.venda_listItens));
        produtoList.setAdapter(adapter);


    }

    public void addVendaProduto(Item prod) {
        prod.setVenda(venda);
        venda.getProdutos().add(prod);
        adapter.notifyDataSetChanged();

        atualizarTotal();
        //meu amor, essa a ultima oracao, pra salvar seu coracao, coracao nao e tao simples quanto pensa, nele cabe o que nao cabe na dispensa
        //cabe o meu amor, cabe 3 vidas inteiras, cabe uma penteadeira, cabe nos dois... e cabe ate o[...]
    }


    public void removerVendaProduto(Item produto) {
        venda.getProdutos().remove(produto);
    }


    public void onClickConfirmar(View view) {

        preencherVenda(venda);

        AlertDialog confirmDialog = new AlertDialog.Builder(this)
                .setTitle("Confirmar Venda")
                .setMessage("Você tem certeza que deseja completar essa venda?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        VendaResult result = controler.enviarVenda(venda);
                        Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                        if(result.isSuccess()){
                            restartActivity();
                        }



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
        ProdutoDialogFragment dialog = new ProdutoDialogFragment();
        dialog.show(getFragmentManager(), "produtoAdd");
    }


    public void onClickProdutoListItem(View view, Item produto) {
        ProdutoDialogFragment dialog = new ProdutoDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(PROD_SELECIONADO, produto);
        dialog.setArguments(args);

        dialog.show(getFragmentManager(), "produtoEdit");
    }


    private void atualizarTotal() {
        double total = 0;
        for (Item item : venda.getProdutos()) {
            total += item.getPrecoTotal();
        }

        ((TextView) findViewById(R.id.venda_total)).setText(String.format("%.2f", total));
    }

    private Venda preencherVenda(Venda venda) {
        venda.setData(new Date());
        venda.setNomeCliente(((TextView) findViewById(R.id.venda_nomeCliente)).getText().toString());
        venda.setCpfCnpj(((TextView) findViewById(R.id.venda_cpfcnpj)).getText().toString());

        return venda;
    }

    private void restartActivity(){
        startActivity(new Intent(this,this.getClass()));
        finish();
    }
}