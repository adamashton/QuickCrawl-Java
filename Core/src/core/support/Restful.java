/*package core.support;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.URL;


import core.json.JSONObject;

public class Restful {
	
	public static JSONObject getJSONResult(String address) throws Exception
	{
		Log.Debug("GET " + address);
		URL url = new URL(address);
		DataInputStream dis = new DataInputStream(url.openStream());
		InputStreamReader isr = new InputStreamReader(dis);
		BufferedReader br = new BufferedReader(isr);

		StringBuilder jsonResultSB = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			jsonResultSB.append(line);
		}
		
		JSONObject result = new JSONObject(jsonResultSB.toString());
		return result;
	}
}
*/