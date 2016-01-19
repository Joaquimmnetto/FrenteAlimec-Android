package com.alimec.joaquim.alimecproject.controle;

import android.content.Context;

import com.alimec.joaquim.alimecproject.configs.ConfiguracaoPrivada;

import java.io.IOException;
import java.util.Locale;

import joaquimneto.com.alimec.model.ProdutoTO;
import joaquimneto.com.alimec.persistence.dao.AbstractDaoFactory;
import joaquimneto.com.alimec.persistence.dao.DatabaseHelper;
import joaquimneto.com.alimec.serverio.IServerModule;
import joaquimneto.com.alimec.serverio.ServerModule;
import joaquimneto.com.alimec.vendas.VendasModule;

/**
 * Created by KithLenovo on 02/05/2015.
 */
public class IntialLoadController {

    private IServerModule server = ServerModule.getInstance();

    public void initContext(Context context) {

        DatabaseHelper.initiate(context);
        ConfiguracaoPrivada.getInstance().initiate(context);
        Locale.setDefault(Locale.ENGLISH);
    }

    public boolean verificarConexao() throws IOException {
        if (server.verificarConexao()) {
            return true;
        } else {
            throw new IOException("Servidor recusou a conexão");
        }
    }

    public boolean atualizarProdutos() throws IOException {

        ProdutoTO[] produtos = server.importarProdutos();

        AbstractDaoFactory.getFactory().getProdutoRepository().limparProdutos();
        AbstractDaoFactory.getFactory().getProdutoRepository().addProdutos(produtos);

        return true;

    }


    public boolean enviarVendasOffline() throws IOException {
        return new VendasModule().enviarVendasPendentes();
    }

}
