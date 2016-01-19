package joaquimneto.com.alimec.persistence.interfaces;

import java.sql.SQLException;
import java.util.Date;

import joaquimneto.com.alimec.model.Venda;


public interface IVendaDAO {

	public boolean addVenda(Venda v) throws SQLException;
	
	public Venda[] getVendasAPartirDe(Date startingDate) throws SQLException;
	
	public Venda getVenda(long timestamp) throws SQLException;

    public Venda[] getVendasPendentes() throws SQLException;

}
