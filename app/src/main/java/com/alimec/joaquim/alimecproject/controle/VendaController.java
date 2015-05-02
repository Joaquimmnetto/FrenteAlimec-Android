package com.alimec.joaquim.alimecproject.controle;

import com.alimec.joaquim.alimecproject.configs.ConfiguracaoPrivada;
import com.alimec.joaquim.alimecproject.persistence.DatabaseHelper;
import com.alimec.joaquim.alimecproject.persistence.VendaDAO;
import com.alimec.joaquim.alimecproject.venda.Venda;
import com.alimec.joaquim.alimecproject.ws.ServerServices;

import org.json.JSONException;

import java.io.IOException;
import java.util.Date;

/**
 * Created by joaquim on 23/04/15.
 */
public class VendaController {


    public boolean enviarVenda(Venda venda) throws IOException, JSONException {

        new VendaDAO(DatabaseHelper.getInstance()).addVenda(venda);
        return enviarVendaSemPersistencia(venda);
    }

    public boolean enviarVendaSemPersistencia(Venda venda) throws IOException, JSONException {
        boolean success = ServerServices.enviarVenda(venda);
        if (success) {
            ConfiguracaoPrivada.getInstance().setDataUltimaVenda(venda.getData());
        }

        return success;
    }

    public boolean enviarVendasPendentes() throws IOException, JSONException {
        VendaDAO dao = new VendaDAO(DatabaseHelper.getInstance());
        Date ultimaVendaEnviada = ConfiguracaoPrivada.getInstance().getDataUltimaVenda();
        Venda[] vendas = dao.getVendasAPartirDe(ultimaVendaEnviada);

        if (vendas.length > 0) {

            boolean success = ServerServices.enviarVendas(vendas);
            if (success) {
                long biggest = 0;

                for (Venda v : vendas) {
                    if (v.getData().getTime() > biggest) {
                        biggest = v.getData().getTime();
                    }
                }
                ConfiguracaoPrivada.getInstance().setDataUltimaVenda(new Date(biggest));
            }
            return success;
        } else {
            return true;
        }
    }


}
