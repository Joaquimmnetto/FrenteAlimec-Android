package com.alimec.joaquim.alimecproject.controle;

import com.alimec.joaquim.alimecproject.configs.ConfiguracaoPrivada;
import com.alimec.joaquim.alimecproject.persistence.DatabaseHelper;
import com.alimec.joaquim.alimecproject.persistence.VendaDAO;
import com.alimec.joaquim.alimecproject.modelo.Venda;
import com.alimec.joaquim.alimecproject.ws.ServerServices;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by joaquim on 23/04/15.
 */
public class VendaController {

    public VendaDAO dao;

    private static VendaController instance;

    private VendaController(){
        dao = new VendaDAO(DatabaseHelper.getInstance());
    }

    public static VendaController getInstance() {
        if(instance == null){
            instance = new VendaController();
        }
        return instance;
    }

    public VendaResult enviarVenda(Venda venda){
        if (venda.getItens().size() == 0) {
            return new VendaResult(false,false,"Essa venda não tem produtos!");
        }


        boolean success = false;
        try {
            success = new VendaDAO(DatabaseHelper.getInstance()).addVenda(venda);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public VendaResult enviarVendasPendentes() throws IOException, JSONException, SQLException {
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



    public List<Venda> buscaMes() throws SQLException {
        Date ultimoMes = new Date(new Date().getTime() - new Date(0,1,0).getTime());

        Venda[] resultado = dao.getVendasAPartirDe(ultimoMes);

        if(resultado == null){
            return new ArrayList<>();
        }

        return Arrays.asList(resultado);
    }

    public List<Venda> buscaHoje() throws SQLException {
        Calendar hoje = GregorianCalendar.getInstance();
        hoje.set(Calendar.HOUR_OF_DAY,0);
        hoje.set(Calendar.MINUTE,0);
        hoje.set(Calendar.SECOND,0);
        hoje.set(Calendar.MILLISECOND,0);

        Venda[] resultado = dao.getVendasAPartirDe(hoje.getTime());

        if(resultado == null){
            return new ArrayList<>();
        }

        return Arrays.asList(resultado);
    }

    public List<Venda> buscaHora() throws SQLException {
        Calendar hoje = GregorianCalendar.getInstance();
        hoje.set(Calendar.HOUR_OF_DAY,hoje.get(Calendar.HOUR_OF_DAY)-1);

        Venda[] resultado = dao.getVendasAPartirDe(hoje.getTime());

        if(resultado == null){
            return new ArrayList<>();
        }

        return Arrays.asList(resultado);
    }


}
