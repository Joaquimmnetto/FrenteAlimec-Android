package joaquimneto.com.alimec.persistence;

import java.util.Date;

import joaquimneto.com.alimec.model.Venda;


public interface IVendaDAO {

	public boolean addVenda(Venda v) throws DBModuleException;
	
	public Venda[] getVendasAPartirDe(Date startingDate) throws DBModuleException;
	
	public Venda getVenda(long timestamp) throws DBModuleException;

    public Venda[] getVendasPendentes() throws DBModuleException;

}
