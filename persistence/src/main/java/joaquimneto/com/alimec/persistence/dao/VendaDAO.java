package joaquimneto.com.alimec.persistence.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.model.Venda;
import joaquimneto.com.alimec.persistence.VendaDB;
import joaquimneto.com.alimec.persistence.interfaces.IItemDAO;
import joaquimneto.com.alimec.persistence.interfaces.IVendaDAO;

/**
 * Created by KithLenovo on 25/01/2015.
 */
public class VendaDAO implements IVendaDAO {

	private Dao<VendaDB, Long> dao;

	VendaDAO(Dao<VendaDB, Long> dao) {
		this.dao = dao;
	}

	public boolean addVenda(Venda v) throws SQLException {
		IItemDAO itemDAO = AbstractDaoFactory.getFactory().getItemDAO();

		for (Item item : v.getItens()) {
			item.setVenda(v);
			itemDAO.addItem(item);
		}
		Dao.CreateOrUpdateStatus result = dao.createOrUpdate(new VendaDB(v));

		return result.isCreated() || result.isUpdated();

	}
	
	@Override
	public Venda getVenda(long timestamp) throws SQLException {
		Venda venda = dao.queryForId(timestamp).toModelo();
		
		if(venda == null){
			return null;
		}
		
		IItemDAO itemDAO = AbstractDaoFactory.getFactory().getItemDAO();
		venda.getItens().addAll(Arrays.asList(itemDAO.getItensFromVenda(venda)));
		
		
		return venda;
	}

	public Venda[] getVendasAPartirDe(Date startingDate) throws SQLException {

		IItemDAO itemDAO = AbstractDaoFactory.getFactory().getItemDAO();
		List<Venda> vendas = new ArrayList<>();

		PreparedQuery<VendaDB> query = dao.queryBuilder().orderBy("data", true).where()
				.gt("data", startingDate.getTime()).prepare();
		List<VendaDB> result = dao.query(query);

		for (VendaDB vendaDB : result) {
			Venda venda = vendaDB.toModelo();
			venda.getItens().addAll(Arrays.asList(itemDAO.getItensFromVenda(venda)));
			vendas.add(venda);
		}

		return vendas.toArray(new Venda[vendas.size()]);
	}

}
