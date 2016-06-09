package com.alimec.joaquim.alimecproject.controle;

import com.alimec.joaquim.alimecproject.activities.ItemTO;

/**
 * Created by KithLenovo on 03/02/2016.
 */
public class ItemController {


    private static ItemController instance;

    private ItemTO item;


    public static ItemController getInstance() {
        instance = instance == null?new ItemController():instance;
        return instance;
    }


    private ItemController(){}

    public String getCodProduto(){return item.codProduto;}
    public double getQuantidade(){return item.quantidade;}
    public String getUnidade(){return item.unidade;}
    public double getPrecoUnitario(){return item.precoUnitario;};
    public String getComentarios(){return item.comentarios;}
    public String getMeioPgto(){return item.meioPgto;}
    public String getObservacoes(){return item.observacoes;}


}
