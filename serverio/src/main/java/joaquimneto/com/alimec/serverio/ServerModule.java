package joaquimneto.com.alimec.serverio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import joaquimneto.com.alimec.model.ProdutoTO;
import joaquimneto.com.alimec.model.Validador;
import joaquimneto.com.alimec.model.Venda;

/**
 * Created by KithLenovo on 23/01/2015.
 */
public class ServerModule implements IServerModule {

    public static final String SERVER_SUCCESS = "success";
    private static final int BROADCAST_PORT = 9008;
    private static final String BROADCAST_ADDRESS;
    private static String serverAddress = "localhost";
    private static int serverPort = 9009;

    private static ServerModule instance = new ServerModule();

    static {
        InetAddress broadcast = NetworkUtils.getEnderecoBroadcast();
        if (broadcast != null) {
            BROADCAST_ADDRESS = broadcast.getHostAddress();
        } else {
            BROADCAST_ADDRESS = "localhost";
        }
    }

    private boolean initialized = false;

    public static ServerModule getInstance() {
        instance = instance == null ? new ServerModule() : instance;

        return instance;
    }

    private ServerModule() {
    }

    private boolean initialize() throws ServerModuleException, IOException {
        ResultadoProcuraServidor lookup = procurarServidor();
        if (lookup.isSucesso()) {
            serverAddress = lookup.getEndereco().getHostAddress();
            serverPort = lookup.getPorta();
            initialized = true;
            return verificarConexao();
        }
        return false;
    }

    private synchronized ResultadoProcuraServidor procurarServidor() throws IOException {
        String mensagemFalha = "";
        JSONObject comando = JSONUtils.makeComando("serverLookup");
        JSONObject response = Transaction.newTransaction(BROADCAST_ADDRESS, BROADCAST_PORT).fazerComandoUDP(comando);

        try {
            if (response.getBoolean(SERVER_SUCCESS)) {
                response = response.getJSONObject("lookup");
                String endereco = response.getString("endereco");
                int porta = response.getInt("porta");
                String versao = response.optString("versao");

                return ResultadoProcuraServidor.gerarSucesso(InetAddress.getByName(endereco), porta, versao);

            } else {
                return ResultadoProcuraServidor.gerarFalha(response.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mensagemFalha = e.getMessage();

        }

        return ResultadoProcuraServidor.gerarFalha(mensagemFalha);
    }

    @Override
    public synchronized ProdutoTO[] importarProdutos() throws ServerModuleException {

        try {
            if (!initialized) {
                if (!initialize()) {
                    return null;
                }
            }
            try {
                JSONObject comando = null;

                comando = JSONUtils.makeComando("importarProdutos");

                JSONObject response = Transaction.newTransaction(serverAddress, serverPort).fazerComando(comando);

                ArrayList<ProdutoTO> result = new ArrayList<>();

                if (response.getBoolean(SERVER_SUCCESS)) {
                    JSONArray produtos = response.getJSONArray("produtos");
                    for (int i = 0; i < produtos.length(); i++) {
                        JSONObject produto = produtos.getJSONObject(i);
                        result.add(JSONParser.toProdutoTO(produto));

                    }
                    return result.toArray(new ProdutoTO[result.size()]);
                } else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        } catch (Exception e) {
            throw new ServerModuleException(e);
        }
    }

    @Override
    public synchronized boolean enviarVenda(Venda venda) throws ServerModuleException {
        return enviarVendas(new Venda[]{venda});
    }

    @Override
    public synchronized boolean enviarVendas(Venda[] vendas) throws ServerModuleException {
        try {

            if (!initialized) {
                if (!initialize()) {
                    return false;
                }
            }

            for (Venda venda : vendas) {
                Validador.validarVenda(venda);
            }
            try {
                JSONObject comando = JSONUtils.makeComando("EnviarVenda", vendas);

                Transaction transaction = Transaction.newTransaction(serverAddress, serverPort);
                JSONObject response = transaction.fazerComando(comando);

                return response.getBoolean(SERVER_SUCCESS);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        } catch (Exception e) {
            throw new ServerModuleException(e);
        }
    }

    @Override
    public boolean verificarConexao() throws ServerModuleException {
        try {
            if (!initialized) {
                if (!initialize()) {
                    return false;
                }
            }

            JSONObject comando = null;
            try {
                comando = JSONUtils.makeComando("serverStatus");
                Transaction transaction = Transaction.newTransaction(serverAddress, serverPort);

                JSONObject response = transaction.fazerComando(comando);

                return response.getBoolean(SERVER_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        } catch (Exception e) {
            throw new ServerModuleException(e);
        }

    }

    // pararms

}
