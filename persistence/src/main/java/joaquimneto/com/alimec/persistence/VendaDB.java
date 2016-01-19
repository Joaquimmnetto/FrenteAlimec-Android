package joaquimneto.com.alimec.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import joaquimneto.com.alimec.model.Venda;

/**
 * Created by KithLenovo on 29/07/2015.
 */

@DatabaseTable
public class VendaDB {
    @DatabaseField(id = true, canBeNull = false, unique = true)
    long data;
    @DatabaseField(canBeNull = true)
    String nomeCliente;
    @DatabaseField(canBeNull = true)
    String cpfCnpj;
    @DatabaseField(canBeNull = false)
    boolean enviado;

    public VendaDB(){}

    public VendaDB(Venda venda){
        data = venda.getData().getTime();
        nomeCliente = venda.getNomeCliente();
        cpfCnpj = venda.getCpfCnpj();
    }

    public Venda toModelo() {
        return new Venda(new Date(data),nomeCliente,cpfCnpj);
    }
}
