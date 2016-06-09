package joaquimneto.com.alimec.vendas;

import java.util.List;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.persistence.DBModuleException;
import joaquimneto.com.alimec.serverio.ServerModuleException;

public interface IVendasModule {

	public boolean carregar(long timestamp) throws VendasModuleException;

	public long criarItem(String codProduto, double quantidade, String unidade, double precoUnitario,
                          MeioPgto meioPgto, String comentario, String observacoes) throws VendasModuleException;
	
	public boolean removerItem(long codItem);

    public boolean atualizarItem(Item item);

    public List<Item> getItens();

	public VendaResult finalizar(String nomeCliente, String cpf) throws VendasModuleException;

    boolean enviarVendasPendentes() throws DBModuleException, ServerModuleException;

    boolean isFinalizado();

    enum MeioPgto {
		DINHEIRO, CHEQUE, BOLETO, PROMISSORIA;
	}
}

