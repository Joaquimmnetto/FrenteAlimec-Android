package joaquimneto.com.alimec.serverio;

import joaquimneto.com.alimec.model.ProdutoTO;
import joaquimneto.com.alimec.model.Venda;


public interface IServerModule {

	boolean enviarVenda(Venda venda) throws ServerModuleException;

	boolean enviarVendas(Venda[] vendas) throws ServerModuleException;

	ProdutoTO[] importarProdutos() throws ServerModuleException;
	
	boolean verificarConexao() throws ServerModuleException;

	

	public static enum ItemJSONArgs {
		DATA, QUANTIDADE, UNIDADE, CODITEM, CLIENTE, COMPLEMENTO, MEIO_PGTO, VALOR_TOTAL, OBSERVACOES;

		@Override
		public String toString() {
			return super.name().toLowerCase();
		}
	}

	public static enum VendaJSONArgs {
		DATA, CLIENTE, CPFCNPJ, ITENS;

		@Override
		public String toString() {
			return super.name().toLowerCase();
		}
	}

}
