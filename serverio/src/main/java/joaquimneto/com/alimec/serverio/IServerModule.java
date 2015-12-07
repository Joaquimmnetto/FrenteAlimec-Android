package joaquimneto.com.alimec.serverio;

import java.io.IOException;

import joaquimneto.com.alimec.model.ProdutoTO;
import joaquimneto.com.alimec.model.Venda;


public interface IServerModule {

	boolean enviarVenda(Venda venda) throws IOException, IllegalArgumentException;

	boolean enviarVendas(Venda[] vendas) throws IOException, IllegalArgumentException;

	ProdutoTO[] importarProdutos() throws IOException ;
	
	boolean verificarConexao() throws IOException;

	

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
