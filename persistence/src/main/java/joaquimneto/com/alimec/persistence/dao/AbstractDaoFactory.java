package joaquimneto.com.alimec.persistence.dao;

import joaquimneto.com.alimec.persistence.interfaces.IItemDAO;
import joaquimneto.com.alimec.persistence.interfaces.IProdutoRepository;
import joaquimneto.com.alimec.persistence.interfaces.IVendaDAO;

public abstract class AbstractDaoFactory {

	public static AbstractDaoFactory getFactory() {
		boolean android = System.getProperty("java.vm.name").equals("Dalvik");

		if (android) {
			return new AndroidDaoFactory();
		}
		return null;
	}

	public abstract IItemDAO getItemDAO();

	public abstract IVendaDAO getVendaDAO();

	public abstract IProdutoRepository getProdutoRepository();
}
