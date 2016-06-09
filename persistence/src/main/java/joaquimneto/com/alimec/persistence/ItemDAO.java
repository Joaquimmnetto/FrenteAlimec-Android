package joaquimneto.com.alimec.persistence;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.model.Venda;

class ItemDAO implements IItemDAO {

    private Dao<ItemDB, String> itemDAO;

    ItemDAO(Dao<ItemDB, String> dao) {
        this.itemDAO = dao;
    }

    public void addItem(Item p) throws DBModuleException {
        try {
            ProdutoDB produto = AbstractDaoFactory.getFactory().getProdutoRepository().getProdutoDB(p.getProduto());
            ItemDB item = new ItemDB(p, produto);

            itemDAO.create(item);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBModuleException(e);
        }
    }

    public void addItens(final List<Item> itens) throws DBModuleException {

        try {
            itemDAO.callBatchTasks(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    for (Item i : itens) {
                        addItem(i);
                    }
                    return -1;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBModuleException(e);
        }

    }

    public Item[] getItensFromVenda(Venda venda) throws DBModuleException {
        List<Item> result = new ArrayList<>();
        List<ItemDB> queryResult = null;
        try {
            queryResult = itemDAO.queryForEq("venda_id", venda.getData().getTime());


            for (ItemDB item : queryResult) {
                result.add(item.toModelo(venda));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBModuleException(e);
        }
        return result.toArray(new Item[result.size()]);
    }

}
