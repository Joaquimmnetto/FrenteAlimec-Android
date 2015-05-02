package com.alimec.joaquim.alimecproject.configs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by joaquim on 23/04/15.
 */
public class ConfiguracaoPrivada {


    private static final String DATA_ULTIMA_VENDA = "dataUltimaVenda";

    private Context context;
    private SharedPreferences privatePrefs;
    private SharedPreferences.Editor prefsEdit;
    private static ConfiguracaoPrivada instance;


    public static ConfiguracaoPrivada getInstance() {
        if(instance == null){
            instance = new ConfiguracaoPrivada();
        }
            return instance;
    }

    private ConfiguracaoPrivada(){};

    public void initiate(Context context){
        this.context = context;
        privatePrefs = context.getSharedPreferences("PrivatePrefs",Context.MODE_PRIVATE);
        prefsEdit = privatePrefs.edit();

    }


    public Date getDataUltimaVenda(){
        return new Date(privatePrefs.getLong(DATA_ULTIMA_VENDA,System.currentTimeMillis()));
    }

    public void setDataUltimaVenda(Date data){

        prefsEdit.putLong(DATA_ULTIMA_VENDA, data.getTime());
        prefsEdit.apply();
    }






}
