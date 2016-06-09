package com.alimec.joaquim.alimecproject.controle;

import android.content.Context;

import com.alimec.joaquim.alimecproject.configs.ConfiguracaoPrivada;

import java.util.Locale;

import joaquimneto.com.alimec.model.ProdutoTO;
import joaquimneto.com.alimec.persistence.AbstractDaoFactory;
import joaquimneto.com.alimec.persistence.DBModuleException;
import joaquimneto.com.alimec.persistence.DatabaseHelper;
import joaquimneto.com.alimec.persistence.IProdutoRepository;
import joaquimneto.com.alimec.serverio.IServerModule;
import joaquimneto.com.alimec.serverio.ServerModule;
import joaquimneto.com.alimec.serverio.ServerModuleException;
import joaquimneto.com.alimec.vendas.VendasModule;

/**
 * Created by KithLenovo on 02/05/2015.
 */
public class IntialLoadController {

    private IServerModule server = ServerModule.getInstance();
    private IProdutoRepository repo = AbstractDaoFactory.getFactory().getProdutoRepository();

    public void initContext(Context context) {

        DatabaseHelper.initiate(context);
        ConfiguracaoPrivada.getInstance().initiate(context);
        Locale.setDefault(Locale.ENGLISH);

    }

    public boolean verificarConexao(){
        try {
            return server.verificarConexao();
        } catch (ServerModuleException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean atualizarProdutos() throws ServerModuleException, DBModuleException {

        ProdutoTO[] produtos = server.importarProdutos();

        repo.limparProdutos();
        repo.addProdutos(produtos);

        return true;

    }


    public boolean enviarVendasPendentes() throws DBModuleException, ServerModuleException {
        return new VendasModule(server).enviarVendasPendentes();
    }

}
