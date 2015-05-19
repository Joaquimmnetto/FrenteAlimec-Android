package com.alimec.joaquim.alimecproject.ws;

import com.alimec.joaquim.alimecproject.persistence.ProdutoRepository;
import com.alimec.joaquim.alimecproject.entidades.JSONable;
import com.alimec.joaquim.alimecproject.entidades.Produto;
import com.alimec.joaquim.alimecproject.entidades.Venda;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class ServerServices {

    public static final String SERVER_SUCCESS = "success";

    public static final String SERVER_ADDRESS = "192.168.0.106";
    public static final int SERVER_PORT = 9009;

    public static synchronized Produto[] importarProdutos() throws IOException, JSONException {

        JSONObject comando = makeComando("importarProdutos");
        JSONObject response = Transaction.newTransaction(SERVER_ADDRESS, SERVER_PORT).fazerComando(comando);

        ArrayList<Produto> result = new ArrayList<>();


        if(response.getBoolean(SERVER_SUCCESS)){
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
    }

    public static synchronized boolean enviarVenda(Venda venda) throws IOException, JSONException {
        return enviarVendas(new Venda[]{venda});
    }

    public static synchronized boolean enviarVendas(Venda[] vendas) throws IOException, JSONException {

        JSONObject comando = makeComando("EnviarVenda", vendas);

        Transaction transaction = Transaction.newTransaction(SERVER_ADDRESS, SERVER_PORT);
        JSONObject response = transaction.fazerComando(comando);

        return response.getBoolean(SERVER_SUCCESS);
    }

    public static synchronized boolean checkUpdates() throws IOException, JSONException {
        Produto[] serverProds = importarProdutos();
        Produto[] localProds = ProdutoRepository.getInstance().getProdutos();

        if(serverProds.length!=localProds.length){
            return true;
        }
        for(int i = 0;i<serverProds.length;i++){
            if(!serverProds[i].equals(localProds[i])){
                return true;
            }
        }


        return false;
    }


    public static boolean isServerVisivel() {
        JSONObject comando = null;
        try {
            comando = makeComando("serverStatus");
            Transaction transaction = Transaction.newTransaction(SERVER_ADDRESS, SERVER_PORT);

            JSONObject response = transaction.fazerComando(comando);

            return response.getBoolean(SERVER_SUCCESS);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }


    private static JSONObject makeComando(String nomeComando) throws JSONException {
        return makeComando(nomeComando,(JSONArray)null);
    }



    private static JSONObject makeComando(String nomeComando,JSONable args) throws JSONException {

        return makeComando(nomeComando,new JSONable[]{args});
    }

    private static JSONObject makeComando(String nomeComando,JSONable[] args) throws JSONException {
        return makeComando(nomeComando,arrayToJSONArray(args));
    }

    private static JSONObject makeComando(String nomeComando,JSONArray args) throws JSONException {
        JSONObject comando = new JSONObject();
        comando.put("comando",nomeComando);
        comando.putOpt("args",args);
        return comando;
    }

    private static JSONArray arrayToJSONArray(JSONable[] objects){

        JSONArray result = new JSONArray();

        for(JSONable obj : objects){
            result.put(obj.toJSON());
        }

        return result;
    }



    // pararms
    public static enum ItemJSONArgs {
        DATA, QUANTIDADE, UNIDADE, CODITEM, CLIENTE, COMPLEMENTO, MEIO_PGTO, VALOR_TOTAL;

        @Override
        public String toString() {
            return super.name().toLowerCase();
        }
    }

    public static enum VendaJSONArgs{
        DATA, CLIENTE, CPFCNPJ, ITENS;

        @Override
        public String toString() {
            return super.name().toLowerCase();
        }
    }


}
