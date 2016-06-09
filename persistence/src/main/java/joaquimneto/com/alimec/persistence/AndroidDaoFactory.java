package joaquimneto.com.alimec.persistence;


import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * 
 * Mock a ser implementada e testada no android
 * 
 * @author KithLenovo
 *
 */
class AndroidDaoFactory extends AbstractDaoFactory{
	
	
	private DatabaseHelper helper;
	
	AndroidDaoFactory(DatabaseHelper helper){
        this.helper = helper;
    }

	@Override
	public IItemDAO getItemDAO() {
        try {
            return new ItemDAO((Dao<ItemDB, String>) helper.getDao(ItemDB.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public IVendaDAO getVendaDAO() {
        try {
            return new VendaDAO((Dao<VendaDB, Long>) helper.getDao(VendaDB.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public IProdutoRepository getProdutoRepository() {
        try {
            return new ProdutoRepository((Dao<ProdutoDB, String>) helper.getDao(ProdutoDB.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}
	

}
