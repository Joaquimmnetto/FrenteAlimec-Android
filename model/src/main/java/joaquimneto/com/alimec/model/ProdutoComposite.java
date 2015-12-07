package joaquimneto.com.alimec.model;

import java.io.Serializable;

/**
 * Created by KithLenovo on 24/07/2015.
 */
public interface ProdutoComposite extends Serializable,Comparable<ProdutoComposite>{

    public String getCodigo();

    public String getDescricao();

    public Categoria getParente();

    public void setParente(Categoria categoria);
}
