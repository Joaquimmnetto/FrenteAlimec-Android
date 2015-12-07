package joaquimneto.com.alimec.persistence.dao;


import joaquimneto.com.alimec.persistence.interfaces.IItemDAO;
import joaquimneto.com.alimec.persistence.interfaces.IProdutoRepository;
import joaquimneto.com.alimec.persistence.interfaces.IVendaDAO;

/**
 * 
 * Mock a ser implementada e testada no android
 * 
 * @author KithLenovo
 *
 */
public class AndroidDaoFactory extends AbstractDaoFactory{
	
	
	private DatabaseHelper helper = DatabaseHelper.getInstance();
	
	AndroidDaoFactory(){}

	@Override
	public IItemDAO getItemDAO() {
//		return new ItemDAO(helper.getDao()));
		return null;
	}

	@Override
	public IVendaDAO getVendaDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProdutoRepository getProdutoRepository() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
