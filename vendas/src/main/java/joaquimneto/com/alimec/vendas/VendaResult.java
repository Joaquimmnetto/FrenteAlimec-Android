package joaquimneto.com.alimec.vendas;

public class VendaResult {
	
	private long timestamp;
	private boolean salvo;
	private boolean enviado;

	
	
	VendaResult( long timestamp, boolean salvo, boolean enviado ){
		this.timestamp = timestamp;
		this.salvo = salvo;
		this.enviado = enviado;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public boolean isSalvo() {
		return salvo;
	}

	public boolean isEnviado() {
		return enviado;
	}


}
