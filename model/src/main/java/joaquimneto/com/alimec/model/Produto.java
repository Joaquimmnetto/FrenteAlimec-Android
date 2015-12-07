package joaquimneto.com.alimec.model;


/**
 * Created by KithLenovo on 23/01/2015.
 */
public class Produto implements ProdutoComposite {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2504296152781262164L;
	private String codigo;
    private String descricao;
    private Categoria parente;

    public Produto(String codigo, String descricao){
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @Override
    public Categoria getParente() {
        return parente;
    }
    @Override
    public void setParente(Categoria parente) {
        this.parente = parente;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
    @Override
    public String toString() {
        return codigo+" - "+descricao;
    }

    
    @Override
    public boolean equals(Object another) {

        if(another == null || !(another instanceof Produto)){
            return false;
        }
        return this.getCodigo().equals(((Produto) another).getCodigo())
                                        &&
               this.getDescricao().equals(((Produto) another).getDescricao());
    }

    @Override
    public int compareTo(ProdutoComposite another) {

        if(another == null){
            return 1;
        }
        return another.getCodigo().compareTo(another.getCodigo());
    }
}
