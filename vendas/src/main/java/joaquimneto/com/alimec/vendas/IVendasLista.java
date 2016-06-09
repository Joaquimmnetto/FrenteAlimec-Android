package joaquimneto.com.alimec.vendas;

import java.util.Date;

import joaquimneto.com.alimec.model.Venda;


public interface IVendasLista {
	
	
	public Venda[] listar(Date dataInicial, Date dataFinal);
	
	
	public Venda[] listar(Date umDia);

	
}
