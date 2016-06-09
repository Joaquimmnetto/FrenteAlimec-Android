package joaquimneto.com.alimec.persistence;

import java.util.List;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.model.Venda;

public interface IItemDAO {

	public void addItem(Item p) throws DBModuleException;

	public void addItens(final List<Item> itens) throws DBModuleException;

	public Item[] getItensFromVenda(Venda venda) throws DBModuleException;
	
}
