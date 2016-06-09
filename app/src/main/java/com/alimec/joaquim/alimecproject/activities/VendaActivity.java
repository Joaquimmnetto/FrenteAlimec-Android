package com.alimec.joaquim.alimecproject.activities;

import android.os.Bundle;


public class VendaActivity extends BaseMenuActivity {

//    public static final String VENDA_SHARED_PREF = "VENDA_PREFS";


//    private VendaBuilder venda = new VendaBuilder();
//    private ItemAdapter adapter;
//    private ListView produtoList;
//    private VendaController controler = new VendaController();

    public static final String VENDA_MENU = "Nova Venda";
    public static final String BUSCA_VENDA_MENU = "Buscar Vendas";

    private VendaFragment vendaFrag = new VendaFragment();
    private BuscaVendasFragment buscaFrag = new BuscaVendasFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addMenuOption(null, VENDA_MENU, vendaFrag);
        addMenuOption(null, BUSCA_VENDA_MENU, buscaFrag);
    }


    @Override
    protected void onStart() {
        super.onStart();
        startMenuOption(VENDA_MENU);

    }
}