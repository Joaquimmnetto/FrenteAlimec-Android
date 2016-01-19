package com.alimec.joaquim.alimecproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.alimec.joaquim.alimecproject.R;
import com.alimec.joaquim.alimecproject.controle.IntialLoadController;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by KithLenovo on 24/01/2015.
 * <p/>
 * TODO: Preco unitario calculado incorretamente
 *  É para ter %.3f de precisao!
 * TODO: Testar modo offline
 *  Verificar envio de itens
 * TODO: Ajeitar cores da lista de itens(EASY!) ...                                 D T OK
 * Pretão
 * TODO: Remover item da lista de itens                                             D T OK
 * Botao lateral, como feito antes.
 * Longo clique é para edição
 * Colocar a opcao de remover no dialog de edicao
 * TODO: Testar observacoes(EASY!)                                                  D T OK
 * TODO: Interface e Logica do Menu (Interface DONE!)
 * Seta de voltar no topo   NOPE
 * Hamburguer ou Bottom bar?    Hamburguer
 * Como fica na horizontal?     So o futuro dira
 * TODO: Fragment da listagem de produtos   DOING
 * TODO: Associar Listagem c/ edicao
 * TODO: Editar e mudar vendas na planilha (HARD!)
 * Editar a venda inteira
 * Identificar a venda pela Data(timestamp) e substituir tudo pelos novos valores.
 * Servico de edicao de venda
 * TODO: Interface Horizontal
 * Permitir mudanças entre vertical/horizontal
 * TODO: Mudar de lugar os botoes de confirmar venda e adicionar produtos(EASY!)
 * Adicionar produtos é mais usado, logo fica melhor próximo das mãos(em baixo)
 * TODO: Projetar seleção de itens para venda usando touch
 * Usar o conceito de árvore, porque ele deu trabalho pra caralho.
 * TODO: Conceito de unidade padrão
 * Necessita o input de info. pela alimec.
 * Conversões entre unidades automágicamente?
 *
 */
public class BeginActivity extends Activity {

    private final String[] NEXT_SCREEN = null;
    private AsyncTask<Void, String, Void> loadTask = new AsyncTask<Void, String, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
            IntialLoadController load = new IntialLoadController();
            try {
                publishProgress("Iniciando banco de dados...");
                load.initContext(getApplicationContext());
                publishProgress("Verificando conexão com o servidor...");
                if (load.verificarConexao()) {
                    publishProgress("Atualizando a base de produtos...");
                    load.atualizarProdutos();
                    publishProgress("Enviando vendas acumuladas em modo offline...");
                    load.enviarVendasOffline();
                } else {
                    publishProgress("Conexão com o servidor não disponível.");
                }

            } catch (IOException e) {
                e.printStackTrace();
                publishProgress("Problemas com a conexão. Continuando em modo offline.");
            } finally {
                publishProgress(NEXT_SCREEN);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values == NEXT_SCREEN) {
                trySleep(1000);
                startActivity(new Intent(BeginActivity.this, VendaActivity.class));
                finish();
            } else {
                message.setText(values[0]);
            }
        }
    };
    private final int MAX_DOTS = 6;
    private AsyncTask<Void, Integer, Void> amazingDotEffect = new AsyncTask<Void, Integer, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
            int polkas = 0;
            while (!BeginActivity.this.isDestroyed()) {
                trySleep(200);
                polkas++;
                polkas = polkas % MAX_DOTS;
                publishProgress(polkas);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            StringBuilder dots = new StringBuilder();
            for (int i = 0; i < values[0]; i++) {
                dots.append(".");
            }
            dotdotdotdotdotdotdot.setText(dots);
        }
    };
    private TextView message;
    private TextView dotdotdotdotdotdotdot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);

        message = (TextView) findViewById(R.id.splash_msg);
        dotdotdotdotdotdotdot = (TextView) findViewById(R.id.splash_dots);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Executor exec = Executors.newFixedThreadPool(2);

        //Why so much effort on this? Polka effects are fucking cool.
        amazingDotEffect.executeOnExecutor(exec);
        //Thing that this class should be acctualy doing, instead of being pretty.
        loadTask.executeOnExecutor(exec);
    }

    private void trySleep(int ms) {
        try {
            Thread.yield();
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
