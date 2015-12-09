package com.alimec.joaquim.alimecproject.ws;

import android.util.Log;

import com.alimec.joaquim.alimecproject.modelo.ResultadoProcuraServidor;
import com.alimec.joaquim.alimecproject.persistence.ProdutoRepository;
import com.alimec.joaquim.alimecproject.modelo.JSONable;
import com.alimec.joaquim.alimecproject.modelo.Produto;
import com.alimec.joaquim.alimecproject.modelo.Venda;
import com.alimec.joaquim.alimecproject.modelo.ProdutoTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class ServerServices {

    public static final String SERVER_SUCCESS = "success";

    private static String serverAddress = "localhost";
    private static int serverPort = 9009;

    private static final String BROADCAST_ADDRESS;
    static{
        InetAddress broadcast = NetworkUtils.getEnderecoBroadcast();
        if(broadcast != null){
            BROADCAST_ADDRESS = broadcast.getHostAddress();
        }
        else{
            BROADCAST_ADDRESS = "192.168.1.255";
        }
    }

    private static final int BROADCAST_PORT = 9008;

    public static boolean configure(ResultadoProcuraServidor lookup){
        if(lookup.isSucesso()){
            serverAddress = lookup.getEndereco().getHostAddress();
            serverPort = lookup.getPorta();

            return isServerVisivel();
        }
        return false;


    }

    public static synchronized ResultadoProcuraServidor procurarServidor() throws IOException, JSONException {

        JSONObject comando = makeComando("serverLookup");
        JSONObject response = Transaction.newTransaction(BROADCAST_ADDRESS,BROADCAST_PORT).fazerComandoUDP(comando);

        if(response.getBoolean(SERVER_SUCCESS)){
            response = response.getJSONObject("lookup");
            String endereco = response.getString("endereco");
            int porta = response.getInt("porta");
            String versao = response.getString("versao");

            return ResultadoProcuraServidor.gerarSucesso(InetAddress.getByName(endereco),porta,versao);

        }else{
            return ResultadoProcuraServidor.gerarFalha(response.getString("message"));
        }

    }



    public static synchronized ProdutoTO[] importarProdutos() throws IOException, JSONException {

        JSONObject comando = makeComando("importarProdutos");
        JSONObject response = Transaction.newTransaction(serverAddress, serverPort).fazerComando(comando);

        ArrayList<ProdutoTO> result = new ArrayList<>();


        if(response.getBoolean(SERVER_SUCCESS)){
            JSONArray produtos = response.getJSONArray("produtos");
            for(int i = 0 ; i < produtos.length() ; i++){
                JSONObject produto = produtos.getJSONObject(i);
                result.add(new ProdutoTO(produto));

            }
            Log.d("PRODUTOS",result.toString());
            return result.toArray(new ProdutoTO[result.size()]);
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

        Transaction transaction = Transaction.newTransaction(serverAddress, serverPort);
        JSONObject response = transaction.fazerComando(comando);

        return response.getBoolean(SERVER_SUCCESS);
    }

    public static synchronized boolean haveUpdates() throws IOException, JSONException {
        ProdutoTO[] serverProds = importarProdutos();
        Produto[] localProds = ProdutoRepository.getInstance().getProdutos();

        if(serverProds.length != localProds.length){
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

        try {
            JSONObject comando = makeComando("serverStatus");
            Transaction transaction = Transaction.newTransaction(serverAddress, serverPort);

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

    private static JSONObject makeComando(String nomeComando,JSONable... args) throws JSONException {
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
        DATA, QUANTIDADE, UNIDADE, CODITEM, CLIENTE, COMPLEMENTO, MEIO_PGTO, VALOR_TOTAL, OBSERVACOES;

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
