package com.alimec.joaquim.alimecproject.controle;

import android.content.Context;

import com.alimec.joaquim.alimecproject.configs.ConfiguracaoPrivada;
import com.alimec.joaquim.alimecproject.modelo.ResultadoProcuraServidor;
import com.alimec.joaquim.alimecproject.persistence.DatabaseHelper;
import com.alimec.joaquim.alimecproject.persistence.ProdutoRepository;
import com.alimec.joaquim.alimecproject.modelo.ProdutoTO;
import com.alimec.joaquim.alimecproject.ws.ServerServices;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
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
            if (ServerServices.haveUpdates()) {
                ProdutoTO[] produtos = ServerServices.importarProdutos();

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
            VendaController.getInstance().enviarVendasPendentes();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
