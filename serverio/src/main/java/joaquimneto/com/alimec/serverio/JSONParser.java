package joaquimneto.com.alimec.serverio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import joaquimneto.com.alimec.model.Item;
import joaquimneto.com.alimec.model.JSONable;
import joaquimneto.com.alimec.model.ProdutoTO;
import joaquimneto.com.alimec.model.Venda;

public class JSONParser {

	public static JSONObject toJSON(JSONable obj){
		if(obj instanceof Item){
			return toJSON((Item)obj);
		}else if( obj instanceof Venda){
			return toJSON((Venda)obj);
		}
		throw new IllegalArgumentException("Tipo n�o registrado em toJSON");
	}
	
	
	public static JSONObject toJSON(Item item) {

		try {
			JSONObject obj = new JSONObject();
			obj.put(IServerModule.ItemJSONArgs.CODITEM.toString(), item.getProduto().getCodigo());
			obj.put(IServerModule.ItemJSONArgs.DATA.toString(), String.valueOf(item.getVenda().getData().getTime()));
			obj.put(IServerModule.ItemJSONArgs.QUANTIDADE.toString(), item.getQuantidade());
			obj.put(IServerModule.ItemJSONArgs.UNIDADE.toString(), item.getUnidade());
			obj.put(IServerModule.ItemJSONArgs.MEIO_PGTO.toString(), item.getMeioPgto());
			obj.put(IServerModule.ItemJSONArgs.VALOR_TOTAL.toString(), item.getPrecoTotal());
			obj.put(IServerModule.ItemJSONArgs.CLIENTE.toString(), item.getVenda().getNomeCliente());
			obj.put(IServerModule.ItemJSONArgs.COMPLEMENTO.toString(), item.getComentario());
			obj.put(IServerModule.ItemJSONArgs.OBSERVACOES.toString(), item.getObservacoes());
			return obj;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static JSONObject toJSON(Venda venda) {

		JSONObject object = new JSONObject();

		try {
			object.put(IServerModule.VendaJSONArgs.DATA.toString(), Long.toString(venda.getData().getTime()));
			object.put(IServerModule.VendaJSONArgs.CLIENTE.toString(), venda.getNomeCliente());
			object.put(IServerModule.VendaJSONArgs.CPFCNPJ.toString(), venda.getCpfCnpj());
			JSONArray items = new JSONArray();

			for (int i = 0; i < venda.getItens().size(); i++) {
				items.put(i, toJSON(venda.getItens().get(i)));
			}

			object.put(IServerModule.VendaJSONArgs.ITENS.toString(), items);

			return object;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ProdutoTO toProdutoTO(JSONObject produto) {
		try {
			String codigo = produto.getString("codigo").trim();
			String descricao = produto.getString("descricao");
			String parenteCod = produto.optString("parenteCod", null);
			if (parenteCod != null) {
				parenteCod.trim();
			}
			boolean categoria = produto.getBoolean("categoria");

			return new ProdutoTO(codigo, descricao, parenteCod, categoria);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

}
