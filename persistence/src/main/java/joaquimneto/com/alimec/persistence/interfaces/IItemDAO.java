package joaquimneto.com.alimec.persistence.interfaces;

import java.sql.SQLException;
import java.util.List;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.model.Venda;

public interface IItemDAO {

	public void addItem(Item p) throws SQLException;

	public void addItens(final List<Item> itens) throws SQLException;

	public Item[] getItensFromVenda(Venda venda) throws SQLException;
	
}
