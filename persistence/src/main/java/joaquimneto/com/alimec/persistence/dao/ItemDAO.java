package joaquimneto.com.alimec.persistence.dao;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.model.Venda;
import joaquimneto.com.alimec.persistence.ItemDB;
import joaquimneto.com.alimec.persistence.interfaces.IItemDAO;

public class ItemDAO implements IItemDAO {

	private Dao<ItemDB, Integer> itemDAO;

	ItemDAO(Dao<ItemDB, Integer> dao) {
		this.itemDAO = dao;
	}

	public void addItem(Item p) throws SQLException {
		ItemDB item = new ItemDB(p);
		itemDAO.create(item);
	}

	public void addItens(final List<Item> itens) throws SQLException {

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
		}

	}

	public Item[] getItensFromVenda(Venda venda) throws SQLException {
		List<Item> result = new ArrayList<>();
		List<ItemDB> queryResult = itemDAO.queryForEq("venda_id", venda.getData().getTime());

		for (ItemDB item : queryResult) {
			result.add(item.toModelo(venda));
		}

		return result.toArray(new Item[result.size()]);

	}

}
