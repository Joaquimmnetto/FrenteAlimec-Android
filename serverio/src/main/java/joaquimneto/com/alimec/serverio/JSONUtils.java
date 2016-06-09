package joaquimneto.com.alimec.serverio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import joaquimneto.com.alimec.model.JSONable;

class JSONUtils {

	public static JSONObject makeComando(String nomeComando)  {
		return makeComando(nomeComando, (JSONArray) null);
	}

	public static JSONObject makeComando(String nomeComando, JSONable... args){
		return makeComando(nomeComando, arrayToJSONArray(args));
	}

	public static JSONObject makeComando(String nomeComando, JSONArray args){
		JSONObject comando = new JSONObject();
        try {
            comando.put("comando", nomeComando);
            comando.putOpt("args", args);

            return comando;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
	}

	public static JSONArray arrayToJSONArray(JSONable[] objects) {

		JSONArray result = new JSONArray();

		for (JSONable obj : objects) {
			result.put(JSONParser.toJSON(obj));
		}

		return result;
	}

	
}
