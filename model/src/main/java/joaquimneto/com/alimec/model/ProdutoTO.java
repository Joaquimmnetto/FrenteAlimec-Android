package joaquimneto.com.alimec.model;

/**
 * Created by KithLenovo on 12/08/2015.
 */
public class ProdutoTO{

    public String codigo;
    public String descricao;
    public String parenteCod;
    public boolean categoria;



    public ProdutoTO(String codigo, String descricao,String parenteCod, boolean categoria){
        this.codigo = codigo;
        this.descricao = descricao;
        this.parenteCod = parenteCod;
        this.categoria = categoria;
    }


    @Override
    public String toString() {
        return codigo + " " +descricao + " ( " + parenteCod+" )" ;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(o instanceof Produto){
            Produto p = (Produto) o;
            boolean result = codigo.equals(p.getCodigo()) && descricao.equals(p.getDescricao());
            if(parenteCod == null && p.getParente() == null){
                return result;
            }else if(p.getParente()!= null && p.getParente().getCodigo() != null){
                return result && parenteCod.equals(p.getParente().getCodigo());
            }
        }
        return super.equals(o);
    }

}
