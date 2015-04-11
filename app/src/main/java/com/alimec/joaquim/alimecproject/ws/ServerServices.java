package com.alimec.joaquim.alimecproject.ws;

import com.alimec.joaquim.alimecproject.venda.Item;
import com.alimec.joaquim.alimecproject.venda.Produto;
import com.alimec.joaquim.alimecproject.venda.Venda;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class ServerServices {

    public static Produto[] importarProdutos() throws IOException, JSONException {
        String nomeComando = "SyncProdutos";

        JSONObject response = Client.atomicTransaction(nomeComando);

        ArrayList<Produto> result = new ArrayList<Produto>();
        if(response.getBoolean("success")){
            JSONArray produtos = response.getJSONArray("produtos");
            for(int i = 0 ; i < produtos.length() ; i++){
                JSONObject produto = produtos.getJSONObject(i);
                result.add(new Produto(produto));

            }
            return result.toArray(new Produto[result.size()]);
        }
        else{
            return null;
        }


//        return new Produto[]{new Produto("PP10", "Pote P1000 c/ tampa")};


    }


    public static boolean uploadVenda(Venda venda) throws IOException, JSONException {
        String nomeComando = "EnviarVenda";
        String comando = new String(nomeComando+ " ");

        for (Item p : venda.getItens()) {
            comando += p.toJSON().toString(4);
        }
        JSONObject response = Client.atomicTransaction(comando);
        if (response.getBoolean("success")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkUpdates() {
        //TODO: comparar produtos do BD com locais
        return true;
    }

    private static void getServerStatus() {
        //TODO: verificar se o server esta online
    }


    public static enum JSONArgs {
        DATA, QUANTIDADE, UNIDADE, CODITEM, CLIENTE, COMPLEMENTO, MEIO_PAGAMENTO, VALOR_TOTAL;

        private static final long TIMEOUT = 5000;

        @Override
        public String toString() {
            return super.name().toLowerCase();
        }

    }


}
