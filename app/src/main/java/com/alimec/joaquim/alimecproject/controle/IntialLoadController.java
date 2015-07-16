package com.alimec.joaquim.alimecproject.controle;

import android.content.Context;

import com.alimec.joaquim.alimecproject.configs.ConfiguracaoPrivada;
import com.alimec.joaquim.alimecproject.entidades.ResultadoProcuraServidor;
import com.alimec.joaquim.alimecproject.persistence.DatabaseHelper;
import com.alimec.joaquim.alimecproject.persistence.ProdutoRepository;
import com.alimec.joaquim.alimecproject.entidades.Produto;
import com.alimec.joaquim.alimecproject.ws.ServerServices;

import org.json.JSONException;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by KithLenovo on 02/05/2015.
 */
public class IntialLoadController {


    public void initContext(Context context) {

        DatabaseHelper.initiate(context);
        ConfiguracaoPrivada.getInstance().initiate(context);
        Locale.setDefault(Locale.ENGLISH);
    }

    public boolean verificarConexao() throws IOException{
        try {
            ResultadoProcuraServidor lookup = ServerServices.procurarServidor();
            if(lookup.isSucesso()){
                return ServerServices.configure(lookup);
            }
            else{
                throw new IOException(lookup.getMensagem());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean atualizarProdutos() throws IOException{
        try {
            if (ServerServices.checkUpdates()) {
                Produto[] produtos = null;
                produtos = ServerServices.importarProdutos();
                ProdutoRepository.getInstance().limparProdutos();
                ProdutoRepository.getInstance().addProdutos(produtos);
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean enviarVendasOffline() throws IOException{
        try {
            new VendaController().enviarVendasPendentes();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

}
