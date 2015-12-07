package joaquimneto.com.alimec.persistence;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.model.Venda;
import joaquimneto.com.alimec.persistence.dao.AbstractDaoFactory;

/**
 * Created by KithLenovo on 29/07/2015.
 */
@DatabaseTable
public class ItemDB {
	@DatabaseField(id = true)
	String id;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	ProdutoDB produto;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	VendaDB venda;
	@DatabaseField(canBeNull = false)
	double quantidade;
	@DatabaseField(canBeNull = false)
	String unidade;
	@DatabaseField(canBeNull = false)
	double precoUnitario;
	@DatabaseField(canBeNull = false)
	double descontos;
	@DatabaseField(canBeNull = false)
	String meioPgto;
	@DatabaseField(canBeNull = false)
	double precoTotal;
	@DatabaseField(canBeNull = true)
	String comentario;
	@DatabaseField(canBeNull = true)
	String observacoes;

	public ItemDB() {
	}

	public ItemDB(Item item) {
		
		produto = AbstractDaoFactory.getFactory().getProdutoRepository().getProdutoDB(item.getProduto());
		venda = new VendaDB(item.getVenda());
		quantidade = item.getQuantidade();
		unidade = item.getUnidade();
		precoUnitario = item.getPrecoUnitario();
		descontos = item.getDescontos();
		meioPgto = item.getMeioPgto();
		precoTotal = item.getPrecoTotal();
		comentario = item.getComentario();
		observacoes = item.getObservacoes();
		
		id = Long.toString(venda.data) + item.getIndex();
	}

	public Item toModelo(Venda vendaModelo) {
		if (venda.data == vendaModelo.getData().getTime()) {
			
			long index = Long.parseLong(id.substring(Long.toString(venda.data).length(),id.length()));
			return new Item(index, AbstractDaoFactory.getFactory().getProdutoRepository().getProduto(produto.codigo),
					vendaModelo, quantidade, unidade, precoUnitario, descontos, meioPgto, precoTotal, comentario,
					observacoes);
		}
		return null;

	}
}
