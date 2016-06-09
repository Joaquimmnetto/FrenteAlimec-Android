package com.alimec.joaquim.alimecproject.controle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import joaquimneto.com.alimec.model.Venda;
import joaquimneto.com.alimec.vendas.IVendasLista;

/**
 * Created by KithLenovo on 05/03/2016.
 */
public class BuscaVendaController {

    public IVendasLista lista;

    public List<Venda> buscaMes() {
        Date ultimoMes = new Date(new Date().getTime() - new Date(0,1,0).getTime());


        Venda[] resultado = lista.listar(ultimoMes, Calendar.getInstance().getTime());

        if(resultado == null){
            return new ArrayList<>();
        }


        return Arrays.asList(resultado);
    }

    public List<Venda> buscaHoje()  {
        Calendar hoje = GregorianCalendar.getInstance();
        hoje.set(Calendar.HOUR_OF_DAY,0);
        hoje.set(Calendar.MINUTE,0);
        hoje.set(Calendar.SECOND,0);
        hoje.set(Calendar.MILLISECOND,0);

        Venda[] resultado = lista.listar(hoje.getTime());

        if(resultado == null){
            return new ArrayList<>();
        }

        return Arrays.asList(resultado);
    }

    public List<Venda> buscaHora() {
        Calendar ultimaHora = GregorianCalendar.getInstance();
        ultimaHora.set(Calendar.HOUR_OF_DAY,ultimaHora.get(Calendar.HOUR_OF_DAY)-1);

        Venda[] resultado = lista.listar(ultimaHora.getTime(), Calendar.getInstance().getTime());

        if(resultado == null){
            return new ArrayList<>();
        }

        return Arrays.asList(resultado);
    }


}
