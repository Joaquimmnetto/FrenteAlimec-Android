package joaquimneto.com.alimec.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO: database modifications
//TODO: DAO modifications
//TODO: server modifications
//TODO: JSON modifications
/**
 * Created by KithLenovo on 24/07/2015.
 */

public class Categoria implements ProdutoComposite{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2400419304000464515L;
	protected String codigo;
    protected String descricao;
    protected Categoria parente;
    protected List<ProdutoComposite> filhos = new ArrayList<>();


    public Categoria(Categoria parente, String codigo, String descricao,ProdutoComposite... filhos){
        this(codigo,descricao,filhos);
        this.parente = parente;

    }

    public Categoria(String codigo, String descricao,ProdutoComposite... filhos){
        this.parente = null;
        this.codigo = codigo;
        this.descricao = descricao;
        this.filhos.addAll(Arrays.asList(filhos));
    }


    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
    @Override
    public Categoria getParente() {
        return parente;
    }
    @Override
    public void setParente(Categoria parente) {
        this.parente = parente;
    }

    public ProdutoComposite[] getFilhos(){
        return filhos.toArray(new ProdutoComposite[filhos.size()]);
    }

    public Produto[] getAllProdutos(){
        List<Produto> produtos = new ArrayList<>();
        for(ProdutoComposite filho:filhos){
            if(filho instanceof Produto){
                produtos.add((Produto) filho);
            }
            else if(filho instanceof Categoria){
                produtos.addAll(Arrays.asList(((Categoria) filho).getAllProdutos()));
            }
        }
        return produtos.toArray(new Produto[produtos.size()]);
    }


    @Override
    public int compareTo(ProdutoComposite another) {
        if(another == null){
            return 1;
        }

        return another.getCodigo().compareTo(getCodigo());
    }
}
