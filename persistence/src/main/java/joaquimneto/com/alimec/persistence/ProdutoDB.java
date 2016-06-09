package joaquimneto.com.alimec.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import joaquimneto.com.alimec.model.ProdutoTO;

/**
 * Created by KithLenovo on 29/07/2015.
 */
@DatabaseTable
class ProdutoDB {

    @DatabaseField(id = true, unique = true, canBeNull = false)
    public String codigo;
    @DatabaseField(canBeNull = false)
    public String descricao;
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = true)
    public ProdutoDB parente;
    @DatabaseField(canBeNull = false)
    public boolean categoria;

    public ProdutoDB(){};


    public ProdutoDB(ProdutoTO produto){
        codigo = produto.codigo;
        descricao = produto.descricao;
        parente = null;
        categoria = produto.categoria;
    }


    @Override
    public boolean equals(Object o) {
        if(o instanceof ProdutoDB && o != null){
            return codigo.equals(((ProdutoDB) o).codigo);
        }
        return false;

    }

    @Override
    public String toString() {
        return codigo + " " + descricao + " ( " + parente+ " )";
    }
}
