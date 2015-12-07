package joaquimneto.com.alimec.model;

import java.net.InetAddress;

/**
 * Created by KithLenovo on 15/07/2015.
 */
public class ResultadoProcuraServidor {

    private InetAddress endereco;
    private String versao;
    private int porta;
    private boolean sucesso;
    private String mensagem;


    public static ResultadoProcuraServidor gerarSucesso(InetAddress endereco, int porta, String versao) {
        ResultadoProcuraServidor res = new ResultadoProcuraServidor(endereco,porta,versao);
        res.sucesso = true;
        res.mensagem = "Busca realizada com sucesso";
        return res;
    }

    public static ResultadoProcuraServidor gerarFalha(String message) {
        ResultadoProcuraServidor res = new ResultadoProcuraServidor(null,-1,null);
        res.sucesso = false;
        res.mensagem = "Erro ao realizar ao consolidar a busca, resposta do servidor e:"+ message;

        return res;
    }

    private ResultadoProcuraServidor(InetAddress endereco,int porta, String versao){
        this.endereco = endereco;
        this.versao = versao;
        this.porta = porta;

    }


    public InetAddress getEndereco() {
        return endereco;
    }

    public String getVersao() {
        return versao;
    }

    public int getPorta() {
        return porta;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }
}
