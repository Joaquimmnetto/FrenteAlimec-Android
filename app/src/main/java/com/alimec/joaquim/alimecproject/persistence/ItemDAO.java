package com.alimec.joaquim.alimecproject.persistence;


import com.alimec.joaquim.alimecproject.modelo.Item;
import com.alimec.joaquim.alimecproject.modelo.Venda;
import com.alimec.joaquim.alimecproject.persistence.entidades.ItemDB;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ItemDAO {

    private Dao<ItemDB,Integer> itemDAO;

    public ItemDAO(DatabaseHelper helper){
        try {
            this.itemDAO = helper.getDao(ItemDB.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addItem(Item p) throws SQLException {
            ItemDB item = new ItemDB(p);
            itemDAO.create(item);
    }

    public Item[] getItensFromVenda(Venda venda) throws SQLException {
        List<Item> result = new ArrayList<>();
        List<ItemDB> queryResult = itemDAO.queryForEq("venda_id",venda.getData().getTime());

        for(ItemDB item:queryResult){
            result.add(item.toModelo(venda));
        }

        return result.toArray(new Item[result.size()]);

    }
}
