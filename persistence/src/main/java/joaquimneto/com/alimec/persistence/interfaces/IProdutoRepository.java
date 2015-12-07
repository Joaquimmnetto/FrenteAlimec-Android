package joaquimneto.com.alimec.persistence.interfaces;

import joaquimneto.com.alimec.model.Produto;
import joaquimneto.com.alimec.model.ProdutoComposite;
import joaquimneto.com.alimec.model.ProdutoTO;
import joaquimneto.com.alimec.persistence.ProdutoDB;

public interface IProdutoRepository {

	public void addProdutos(ProdutoTO[] produtosTO);

	public Produto[] getProdutos();

	public String[] getListaCodigos();

	public Produto getProduto(String prodCode);

	public ProdutoDB getProdutoDB(ProdutoComposite produto);

	public void limparProdutos();

}
