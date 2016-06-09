package com.alimec.joaquim.alimecproject.controle;

import com.alimec.joaquim.alimecproject.activities.ItemTO;

import java.util.List;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.persistence.DBModuleException;
import joaquimneto.com.alimec.serverio.ServerModule;
import joaquimneto.com.alimec.serverio.ServerModuleException;
import joaquimneto.com.alimec.vendas.IVendasModule;
import joaquimneto.com.alimec.vendas.VendaResult;
import joaquimneto.com.alimec.vendas.VendasModule;
import joaquimneto.com.alimec.vendas.VendasModuleException;

/**
 * Created by joaquim on 23/04/15.
 */
public class VendaController {


    private IVendasModule mVenda = new VendasModule(ServerModule.getInstance());


    public void carregar(long timestamp) throws VendasModuleException {
        mVenda.carregar(timestamp);
    }

     public void addItem(ItemTO item) throws VendasModuleException {
        IVendasModule.MeioPgto pgto = VendasModule.MeioPgto.valueOf(item.meioPgto.toUpperCase());

        mVenda.criarItem(item.codProduto,item.quantidade,item.unidade,item.precoUnitario,
                        pgto,item.comentarios,item.observacoes);
    }


    public List<Item> getItens(){
        return mVenda.getItens();
    }

    public VendaResult finalizar(String cliente, String cpfCnpj) throws VendasModuleException {
       return mVenda.finalizar(cliente,cpfCnpj);
    }

    public boolean enviarVendasPendentes() throws DBModuleException, ServerModuleException {
       return mVenda.enviarVendasPendentes();
    }


    public void atualizaItem(Item item, boolean manter) {
        if(manter){
            mVenda.atualizarItem(item);
        }else{
            mVenda.removerItem(item.getId());
        }

    }

    public double calcularTotal() {
        double total = 0;
        for(Item i:getItens()){
            total += i.getPrecoTotal();
        }
        return total;
    }

}
