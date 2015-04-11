package com.alimec.joaquim.alimecproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alimec.joaquim.alimecproject.persistence.DatabaseHelper;
import com.alimec.joaquim.alimecproject.persistence.ProdutoRepository;
import com.alimec.joaquim.alimecproject.venda.Produto;
import com.alimec.joaquim.alimecproject.ws.ServerServices;

import org.json.JSONException;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by KithLenovo on 24/01/2015.
 */
public class BeginActivity extends Activity{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper.initiate(getApplicationContext());
        Locale.setDefault(Locale.ENGLISH);

        if(ServerServices.checkUpdates()){
            Produto[] produtos = null;
            try {
                produtos = ServerServices.importarProdutos();
                ProdutoRepository.getInstance().limparProdutos();
                ProdutoRepository.getInstance().addProdutos(produtos);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        startActivity(new Intent(this,VendaActivity.class));
        finish();
    }


}
