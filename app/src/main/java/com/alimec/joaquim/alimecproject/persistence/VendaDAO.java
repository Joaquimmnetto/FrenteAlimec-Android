package com.alimec.joaquim.alimecproject.persistence;

import com.alimec.joaquim.alimecproject.modelo.Item;
import com.alimec.joaquim.alimecproject.modelo.Venda;
import com.alimec.joaquim.alimecproject.persistence.entidades.VendaDB;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by KithLenovo on 25/01/2015.
 */
public class VendaDAO {

    private Dao<VendaDB,Integer> dao;

    public VendaDAO(OrmLiteSqliteOpenHelper helper) {
        try {
            dao = helper.getDao(VendaDB.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addVenda(Venda v) throws SQLException {
        ItemDAO itemDAO = new ItemDAO(DatabaseHelper.getInstance());

        for (Item item : v.getItens()) {
            item.setVenda(v);//TODO: isso eh regra, colocar no controler.
            itemDAO.addItem(item);
        }
        Dao.CreateOrUpdateStatus result = dao.createOrUpdate(new VendaDB(v));

        return result.isCreated() || result.isUpdated();

    }

    public Venda[] getVendasAPartirDe(Date startingDate) throws SQLException {

        ItemDAO itemDAO = new ItemDAO(DatabaseHelper.getInstance());
        List<Venda> vendas = new ArrayList<>();

        PreparedQuery<VendaDB> query = dao.queryBuilder().orderBy("data",true).where().gt("data",startingDate.getTime()).prepare();
        List<VendaDB> result = dao.query(query);

        for(VendaDB vendaDB:result){
            Venda venda = vendaDB.toModelo();
            venda.getItens().addAll(Arrays.asList(itemDAO.getItensFromVenda(venda)));
            vendas.add(venda);
        }

        return vendas.toArray(new Venda[vendas.size()]);
    }


//    private Venda parseVenda(Cursor c) {
//        long data = c.getLong(c.getColumnIndex(Venda.Tabela.DATA.toString()));
//        String nome = c.getString(c.getColumnIndex(Venda.Tabela.NOME_CLIENTE.toString()));
//        String cnpjCpf = c.getString(c.getColumnIndex(Venda.Tabela.CPF_CNPJ.toString()));
//
//        return new Venda(new Date(data), nome, cnpjCpf);
//    }


}
