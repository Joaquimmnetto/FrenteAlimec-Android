package com.alimec.joaquim.alimecproject.controle;

import joaquimneto.com.alimec.model.Produto;
import joaquimneto.com.alimec.persistence.AbstractDaoFactory;
import joaquimneto.com.alimec.persistence.IProdutoRepository;

/**
 * Created by KithLenovo on 03/02/2016.
 */
public class ProdutoController {

    private static ProdutoController instance;

    private IProdutoRepository repo = AbstractDaoFactory.getFactory().getProdutoRepository();

    public static ProdutoController getInstance() {
        instance = instance == null ? new ProdutoController() : instance;
        return instance;
    }


    private ProdutoController(){}


    public Produto getProduto(String codigo){

        Produto prod = repo.getProduto(codigo);

        if(prod == null){
            throw new IllegalArgumentException("Código não existe no banco de dados!");
        }

        return prod;

    }

    public Produto[] getProdutos() {
        Produto[] produtos = repo.getProdutos();

        return produtos;
    }
}
