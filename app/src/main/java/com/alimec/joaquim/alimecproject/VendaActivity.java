package com.alimec.joaquim.alimecproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alimec.joaquim.alimecproject.persistence.DatabaseHelper;
import com.alimec.joaquim.alimecproject.persistence.VendaDAO;
import com.alimec.joaquim.alimecproject.venda.Venda;
import com.alimec.joaquim.alimecproject.venda.Item;
import com.alimec.joaquim.alimecproject.ws.ServerServices;

import java.util.Date;


public class VendaActivity extends ActionBarActivity {

    public static final String PROD_SELECIONADO = "PROD_SELECIONADO";

    private Venda venda;
    private ProdutoAdapter adapter;
    private ListView produtoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_venda);
        init();
        setListeners();
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
        venda = new Venda();
        adapter = new ProdutoAdapter(getApplicationContext(), venda.getProdutos());
        produtoList = ((ListView) findViewById(R.id.venda_listItens));
        produtoList.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addVendaProduto(Item prod) {
        prod.setVenda(venda);
        venda.getProdutos().add(prod);
        adapter.notifyDataSetChanged();
        //meu amor, essa a ultima oracao, pra salvar seu coracao, coracao nao e tao simples quanto pensa, nele cabe o que nao cabe na dispensa
        //cabe o meu amor, cabe 3 vidas inteiras, cabe uma penteadeira, cabe nos dois... e cabe ate o[...]
    }

    public void removerVendaProduto(Item produto) {
        venda.getProdutos().remove(produto);
    }


    public void onClickConfirmar(View view) {
        //TODO: Criar layout do dialog dos modos de pgto.
        //TODO: Mostrar dialog de modos de pgto, antes de confirmar.

        //TODO: Mostrar resumo da venda no dialog de confirmacao.

        AlertDialog confirmDialog = new AlertDialog.Builder(this)
                .setTitle("Confirmar Venda")
                .setMessage("Você tem certeza que deseja completar essa venda?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (venda.getProdutos().size() == 0) {
                            Toast.makeText(getApplicationContext(),"Essa venda não tem produtos!",Toast.LENGTH_LONG).show();
                            return;
                        }
                        venda.setData(new Date());
                        venda.setNomeCliente(((TextView) findViewById(R.id.venda_nomeCliente)).getText().toString());
                        venda.setCpfCnpj(((TextView) findViewById(R.id.venda_cpfcnpj)).getText().toString());
                        new VendaDAO(DatabaseHelper.getInstance()).addVenda(venda);
                        //ServerServices.uploadVenda(venda);
                    }
                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();


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

}
