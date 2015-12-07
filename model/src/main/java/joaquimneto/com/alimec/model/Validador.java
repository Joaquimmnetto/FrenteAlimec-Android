package joaquimneto.com.alimec.model;


public class Validador {

	public static void validarVenda(Venda venda) {
		if (venda == null)
			atire("Venda nula");
		if (venda.getItens().size() == 0)
			atire("Venda com itens vazios");

		for (Item item : venda.getItens()) {
			validarItem(item);
		}

	}

	public static void validarItem(Item item) {
		if (item == null)
			atire("item nulo");
		// TODO: pegar lista dos meios de pagamento validos do server
		if (item.getProduto() == null)
			atire("Produto nulo");

		if (item.getPrecoUnitario() < 0)
			atire("Pre�o unit�rio negativo");

		if (item.getQuantidade() <= 0)
			atire("Quantidade negativa");

		if (item.getUnidade() == null || item.getUnidade().equals(""))
			atire("Unidade nula ou vazia");

		if (item.getMeioPgto() == null || item.getMeioPgto().equals(""))
			atire("Meio Pagamento nulo ou vazio");

		if (item.getPrecoTotal() != item.getQuantidade() * item.getPrecoUnitario())
			atire("Pre�o total n�o coerente");

		if (item.getVenda() == null)
			atire("Venda nula");

	}

	private static void atire(String mensagem) {
		throw new IllegalArgumentException(mensagem);
	}

}
