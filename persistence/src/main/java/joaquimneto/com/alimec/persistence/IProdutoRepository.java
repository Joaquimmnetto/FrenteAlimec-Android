package joaquimneto.com.alimec.persistence;

import joaquimneto.com.alimec.model.Produto;
import joaquimneto.com.alimec.model.ProdutoComposite;
import joaquimneto.com.alimec.model.ProdutoTO;

public interface IProdutoRepository {

	public void addProdutos(ProdutoTO[] produtosTO) throws DBModuleException;

	public Produto[] getProdutos();

	public String[] getListaCodigos();

	public Produto getProduto(String prodCode);

	public ProdutoDB getProdutoDB(ProdutoComposite produto) throws DBModuleException;

	public void limparProdutos() throws DBModuleException;

}
