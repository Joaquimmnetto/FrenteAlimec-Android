package com.alimec.joaquim.alimecproject.controle;

import android.widget.Toast;

import com.alimec.joaquim.alimecproject.configs.ConfiguracaoPrivada;
import com.alimec.joaquim.alimecproject.persistence.DatabaseHelper;
import com.alimec.joaquim.alimecproject.persistence.VendaDAO;
import com.alimec.joaquim.alimecproject.entidades.Venda;
import com.alimec.joaquim.alimecproject.ws.ServerServices;

import org.json.JSONException;

import java.io.IOException;
import java.util.Date;

/**
 * Created by joaquim on 23/04/15.
 */
public class VendaController {


    public VendaResult enviarVenda(Venda venda){
        VendaResult result = null;

        if (venda.getProdutos().size() == 0) {
            return new VendaResult(false,false,"Essa venda não tem produtos!");
        }


        boolean success = new VendaDAO(DatabaseHelper.getInstance()).addVenda(venda);
        if(success){
           return enviarVendaSemPersistencia(venda);
        }
        else{
            return new VendaResult(success,false,"Erro ao salvar a venda no banco de dados");
        }

    }

    public VendaResult enviarVendaSemPersistencia(Venda venda) {
        boolean sent = false;
        try {
            sent = ServerServices.enviarVenda(venda);
            if (sent) {
                ConfiguracaoPrivada.getInstance().setDataUltimaVenda(venda.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            return new VendaResult(false, sent, "Um erro inesperado ocorreu");
        }

        return new VendaResult(true, sent, "Venda enviada com sucesso");
    }

    public VendaResult enviarVendasPendentes() throws IOException, JSONException {
        VendaDAO dao = new VendaDAO(DatabaseHelper.getInstance());
        Date ultimaVendaEnviada = ConfiguracaoPrivada.getInstance().getDataUltimaVenda();
        Venda[] vendas = dao.getVendasAPartirDe(ultimaVendaEnviada);

        if (vendas.length > 0) {

            boolean sent = ServerServices.enviarVendas(vendas);
            if (sent) {
                long biggest = 0;

                for (Venda v : vendas) {
                    if (v.getData().getTime() > biggest) {
                        biggest = v.getData().getTime();
                    }
                }
                ConfiguracaoPrivada.getInstance().setDataUltimaVenda(new Date(biggest));
            }
            return new VendaResult(true, sent, "Vendas enviadas com sucesso");
        } else {
            return new VendaResult(true, false, "Sem vendas para enviar");
        }
    }


}
