package com.alimec.joaquim.alimecproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alimec.joaquim.alimecproject.controle.VendaController;
import com.alimec.joaquim.alimecproject.configs.ConfiguracaoPrivada;
import com.alimec.joaquim.alimecproject.persistence.DatabaseHelper;
import com.alimec.joaquim.alimecproject.persistence.ProdutoRepository;
import com.alimec.joaquim.alimecproject.venda.Produto;
import com.alimec.joaquim.alimecproject.ws.ServerServices;

import org.json.JSONException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by KithLenovo on 24/01/2015.
 */
public class BeginActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper.initiate(getApplicationContext());
        ConfiguracaoPrivada.getInstance().initiate(getApplicationContext());

        Locale.setDefault(Locale.ENGLISH);

        if (!ServerServices.isServerReachable()) {
            Toast.makeText(getApplicationContext(), "Servidor não está disponível, por favor conecte esse computador a uma rede", Toast.LENGTH_LONG).show();

        } else {
            try {
                if (ServerServices.checkUpdates()) {
                    Produto[] produtos = null;
                    produtos = ServerServices.importarProdutos();
                    ProdutoRepository.getInstance().limparProdutos();
                    ProdutoRepository.getInstance().addProdutos(produtos);
                }
                new VendaController().enviarVendasPendentes();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Não foi possível estabelecer conexão com o servidor. Aplicativo funcionará no modo offline", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("PRODUTOS", Arrays.asList(ProdutoRepository.getInstance().getListaCodigos()).toString());
        startActivity(new Intent(this, VendaActivity.class));
        finish();
    }


}
